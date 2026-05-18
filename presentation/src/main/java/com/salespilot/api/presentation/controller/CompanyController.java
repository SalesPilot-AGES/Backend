package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.ApiResponse;
import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.dto.PaginatedResponse;
import com.salespilot.api.application.usecase.GetAllCompaniesUseCase;
import com.salespilot.api.application.usecase.GetCompanyByIdUseCase;
import com.salespilot.api.application.usecase.PostCompanyUseCase;
import com.salespilot.api.application.usecase.UpdateCompanyUseCase;
import com.salespilot.api.presentation.dto.CompanyRequestDTO;
import com.salespilot.api.presentation.dto.UpdateCompanyRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Companies")
@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final PostCompanyUseCase postCompanyUseCase;
    private final GetCompanyByIdUseCase getCompanyByIdUseCase;
    private final GetAllCompaniesUseCase getCompanyUseCase;
    private final UpdateCompanyUseCase updateCompanyUseCase;

    private static final int MAX_PAGE_SIZE = 100;

    private static final String COMPANY_RESPONSE_EXAMPLE = """
            {
              "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
              "name": "Digital Sales Ltda",
              "tax_id": "12.345.678/0001-90",
              "active": true,
              "created_at": "2024-04-01T08:00:00",
              "updated_at": "2024-04-01T08:00:00",
              "plan": "BASIC"
            }
            """;

    public CompanyController(PostCompanyUseCase postCompanyUseCase,
                             GetCompanyByIdUseCase getCompanyByIdUseCase,
                             GetAllCompaniesUseCase getCompanyUseCase,
                             UpdateCompanyUseCase updateCompanyUseCase) {
        this.postCompanyUseCase = postCompanyUseCase;
        this.getCompanyByIdUseCase = getCompanyByIdUseCase;
        this.getCompanyUseCase = getCompanyUseCase;
        this.updateCompanyUseCase = updateCompanyUseCase;
    }

    @Operation(summary = "Cadastrar empresa", description = "Cria uma nova empresa na plataforma.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CompanyRequestDTO.class),
            examples = @ExampleObject(value = """
                    {
                      "name": "Digital Sales Ltda",
                      "tax_id": "12.345.678/0001-90",
                      "plan": "BASIC",
                      "active": true
                    }
                    """)))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Empresa criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CompanyResponseDTO.class),
                            examples = @ExampleObject(value = COMPANY_RESPONSE_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "CNPJ já cadastrado", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ApiResponse<CompanyResponseDTO>> create(@Valid @RequestBody CompanyRequestDTO request) {
        CompanyResponseDTO response = postCompanyUseCase.create(request.name(), request.taxId(), request.plan(), request.active());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, "Empresa criada com sucesso"));
    }

    @Operation(summary = "Buscar empresa por ID", description = "Retorna os dados de uma empresa pelo seu UUID.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Empresa encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CompanyResponseDTO.class),
                            examples = @ExampleObject(value = COMPANY_RESPONSE_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyResponseDTO>> getCompanyById(
            @Parameter(description = "UUID da empresa") @PathVariable UUID id) {
        return getCompanyByIdUseCase.execute(id)
                .map(company -> ResponseEntity.ok(ApiResponse.success(company, "Empresa encontrada")))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualizar empresa", description = "Atualiza nome, plano e status de ativação de uma empresa.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UpdateCompanyRequestDTO.class),
            examples = @ExampleObject(value = """
                    {
                      "name": "Digital Sales Ltda",
                      "plan": "PRO",
                      "active": true
                    }
                    """)))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Empresa atualizada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CompanyResponseDTO.class),
                            examples = @ExampleObject(value = COMPANY_RESPONSE_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyResponseDTO>> updateCompany(
            @Parameter(description = "UUID da empresa") @PathVariable UUID id,
            @Valid @RequestBody UpdateCompanyRequestDTO request) {
        CompanyResponseDTO company = updateCompanyUseCase.execute(id, request.name(), request.plan(), request.active());
        return ResponseEntity.ok(ApiResponse.success(company, "Empresa atualizada com sucesso"));
    }

    @Operation(summary = "Listar empresas", description = "Retorna uma página de empresas com filtros opcionais.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista paginada de empresas",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "content": [
                                {
                                  "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
                                  "name": "Digital Sales Ltda",
                                  "tax_id": "12.345.678/0001-90",
                                  "active": true,
                                  "created_at": "2024-04-01T08:00:00",
                                  "updated_at": "2024-04-01T08:00:00",
                                  "plan": "BASIC"
                                }
                              ],
                              "total_elements": 1,
                              "total_pages": 1,
                              "number": 0,
                              "size": 20,
                              "first": true,
                              "last": true,
                              "empty": false
                            }
                            """)))
    @GetMapping
    public ResponseEntity<PaginatedResponse<CompanyResponseDTO>> getAll(
            @Parameter(description = "Filtro por nome (parcial)") @RequestParam(required = false) String name,
            @Parameter(description = "Filtro por CNPJ (exato)") @RequestParam(required = false) String taxId,
            @Parameter(description = "Filtro por plano", example = "BASIC") @RequestParam(required = false) String plan,
            @Parameter(description = "Filtro por status de ativação") @RequestParam(required = false) Boolean active,
            Pageable pageable) {
        Pageable safePageable = normalizePageable(pageable);
        Page<CompanyResponseDTO> companies = getCompanyUseCase.execute(name, taxId, plan, active, safePageable);
        return ResponseEntity.ok(PaginatedResponse.from(companies, "Empresas listadas com sucesso"));
    }

    private Pageable normalizePageable(Pageable pageable) {
        int safePage = Math.max(pageable.getPageNumber(), 0);
        int safeSize = Math.min(Math.max(pageable.getPageSize(), 1), MAX_PAGE_SIZE);
        Sort safeSort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.unsorted();
        return PageRequest.of(safePage, safeSize, safeSort);
    }
}
