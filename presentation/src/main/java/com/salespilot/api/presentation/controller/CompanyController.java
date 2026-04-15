package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.usecase.GetAllCompaniesUseCase;
import com.salespilot.api.application.usecase.GetCompanyByIdUseCase;
import com.salespilot.api.application.usecase.PostCompanyUseCase;
import com.salespilot.api.application.usecase.UpdateCompanyUseCase;
import com.salespilot.api.domain.enums.CompanyPlan;
import com.salespilot.api.presentation.dto.CompanyRequestDTO;
import com.salespilot.api.presentation.dto.UpdateCompanyRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Empresa criada com sucesso",
                    content = @Content(schema = @Schema(implementation = CompanyResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado", content = @Content),
            @ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CompanyResponseDTO> create(@Valid @RequestBody CompanyRequestDTO request) {
        CompanyResponseDTO response = postCompanyUseCase.create(request.name(), request.taxId(), request.plan(), request.active());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Buscar empresa por ID", description = "Retorna os dados de uma empresa pelo seu UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empresa encontrada",
                    content = @Content(schema = @Schema(implementation = CompanyResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> getCompanyById(
            @Parameter(description = "UUID da empresa") @PathVariable UUID id) {
        return getCompanyByIdUseCase.execute(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualizar empresa", description = "Atualiza nome, plano e status de ativação de uma empresa.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empresa atualizada",
                    content = @Content(schema = @Schema(implementation = CompanyResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content),
            @ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> updateCompany(
            @Parameter(description = "UUID da empresa") @PathVariable UUID id,
            @Valid @RequestBody UpdateCompanyRequestDTO request) {
        CompanyResponseDTO company = updateCompanyUseCase.execute(id, request.name(), request.plan(), request.active());
        return ResponseEntity.ok(company);
    }

    @Operation(summary = "Listar empresas", description = "Retorna uma página de empresas com filtros opcionais.")
    @ApiResponse(responseCode = "200", description = "Lista paginada de empresas")
    @GetMapping
    public ResponseEntity<Page<CompanyResponseDTO>> getAll(
            @Parameter(description = "Filtro por nome (parcial)") @RequestParam(required = false) String name,
            @Parameter(description = "Filtro por CNPJ (exato)") @RequestParam(required = false) String taxId,
            @Parameter(description = "Filtro por plano") @RequestParam(required = false) CompanyPlan plan,
            @Parameter(description = "Filtro por status de ativação") @RequestParam(required = false) Boolean active,
            Pageable pageable) {
        Pageable safePageable = normalizePageable(pageable);
        return ResponseEntity.ok(getCompanyUseCase.execute(name, taxId, plan, active, safePageable));
    }

    private Pageable normalizePageable(Pageable pageable) {
        int safePage = Math.max(pageable.getPageNumber(), 0);
        int safeSize = Math.min(Math.max(pageable.getPageSize(), 1), MAX_PAGE_SIZE);
        Sort safeSort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.unsorted();
        return PageRequest.of(safePage, safeSize, safeSort);
    }
}
