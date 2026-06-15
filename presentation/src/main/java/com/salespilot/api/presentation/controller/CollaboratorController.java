package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.ApiResponse;
import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.dto.PaginatedResponse;
import com.salespilot.api.application.dto.SellerResponseDTO;
import com.salespilot.api.application.dto.SellerWithMeetingsResponseDTO;
import com.salespilot.api.application.usecase.EditCollaboratorUseCase;
import com.salespilot.api.application.usecase.GetAllManagersUseCase;
import com.salespilot.api.application.usecase.GetAllSellersUseCase;
import com.salespilot.api.application.usecase.GetCollaboratorByIdUseCase;
import com.salespilot.api.application.usecase.GetSellerByIdUseCase;
import com.salespilot.api.application.usecase.PostCollaboratorUseCase;
import com.salespilot.api.application.usecase.SetCollaboratorPasswordUseCase;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.presentation.dto.CollaboratorPasswordRequestDTO;
import com.salespilot.api.presentation.dto.CollaboratorRequestDTO;
import com.salespilot.api.presentation.dto.CollaboratorUpdateRequestDTO;
import com.salespilot.api.presentation.utils.JwtUtils;
import com.salespilot.api.presentation.utils.PageableUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Collaborators")
@RestController
@RequestMapping("/api/collaborators")
public class CollaboratorController {

    private final PostCollaboratorUseCase postCollaboratorUseCase;
    private final EditCollaboratorUseCase editCollaboratorUseCase;
    private final GetCollaboratorByIdUseCase getCollaboratorByIdUseCase;
    private final GetAllManagersUseCase getAllManagersUseCase;
    private final GetAllSellersUseCase getAllSellersUseCase;
    private final GetSellerByIdUseCase getSellerByIdUseCase;
    private final SetCollaboratorPasswordUseCase setCollaboratorPasswordUseCase;

    private static final String COLLABORATOR_REQUEST_EXAMPLE = """
            {
              "company_id": "b1c2d3e4-f5a6-7890-2345-67890abcdef1",
              "name": "Gabriel Ribeiro",
              "email": "gabriel@digitalsales.com",
              "phone": "+55 (11) 98888-7777",
              "active": true,
              "preferences": {
                "theme": "light",
                "default_model": "gpt-4o"
              }
            }
            """;

    private static final String MANAGER_RESPONSE_EXAMPLE = """
            {
              "id": "c3d4e5f6-a7b8-9012-3456-7890abcdef12",
              "company_id": "b1c2d3e4-f5a6-7890-2345-67890abcdef1",
              "name": "Gabriel Ribeiro",
              "role": "MANAGER",
              "email": "gabriel@digitalsales.com",
              "active": true,
              "preferences": {
                "theme": "light",
                "default_model": "gpt-4o"
              },
              "created_at": "2024-04-02T10:01:00",
              "company": {
                "id": "b1c2d3e4-f5a6-7890-2345-67890abcdef1",
                "name": "Digital Sales",
                "tax_id": "12.345.678/0001-90",
                "active": true,
                "created_at": "2024-04-01T08:00:00",
                "updated_at": "2024-04-01T08:00:00",
                "plan": "PRO"
              }
            }
            """;

        private static final String SELLER_RESPONSE_EXAMPLE = """
            {
              "id": "c3d4e5f6-a7b8-9012-3456-7890abcdef12",
              "company_id": "b1c2d3e4-f5a6-7890-2345-67890abcdef1",
              "name": "Gabriel Ribeiro",
              "role": "SELLER",
              "email": "gabriel@digitalsales.com",
              "phone": "+55 (11) 98888-7777",
              "active": true,
              "preferences": {
                "theme": "light",
                "default_model": "gpt-4o"
              },
              "totalMeetings": 0,
              "created_at": "2024-04-02T10:01:00",
              "company": {
                "id": "b1c2d3e4-f5a6-7890-2345-67890abcdef1",
                "name": "Digital Sales",
                "tax_id": "12.345.678/0001-90",
                "active": true,
                "created_at": "2024-04-01T08:00:00",
                "updated_at": "2024-04-01T08:00:00",
                "plan": "PRO"
              }
            }
            """;

    private static final String COLLABORATOR_PASSWORD_REQUEST_EXAMPLE = """
            {
              "password": "ChangeMe123!"
            }
            """;

    public CollaboratorController(PostCollaboratorUseCase postCollaboratorUseCase,
            EditCollaboratorUseCase editCollaboratorUseCase,
            GetCollaboratorByIdUseCase getCollaboratorByIdUseCase,
            GetAllManagersUseCase getAllManagersUseCase,
            GetAllSellersUseCase getAllSellersUseCase,
            GetSellerByIdUseCase getSellerByIdUseCase,
            SetCollaboratorPasswordUseCase setCollaboratorPasswordUseCase) {
        this.postCollaboratorUseCase = postCollaboratorUseCase;
        this.editCollaboratorUseCase = editCollaboratorUseCase;
        this.getCollaboratorByIdUseCase = getCollaboratorByIdUseCase;
        this.getAllManagersUseCase = getAllManagersUseCase;
        this.getAllSellersUseCase = getAllSellersUseCase;
        this.getSellerByIdUseCase = getSellerByIdUseCase;
        this.setCollaboratorPasswordUseCase = setCollaboratorPasswordUseCase;
    }

    @Operation(summary = "Cadastrar manager", description = "Cria um novo colaborador com o papel de MANAGER.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorRequestDTO.class), examples = @ExampleObject(value = COLLABORATOR_REQUEST_EXAMPLE)))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Manager criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorResponseDTO.class), examples = @ExampleObject(value = MANAGER_RESPONSE_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Colaborador já existe nesta empresa com este e-mail", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    })
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PostMapping("/managers")
    public ResponseEntity<ApiResponse<CollaboratorResponseDTO>> createManager(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CollaboratorRequestDTO request) {

        AuthUserDTO authUser = JwtUtils.toAuthUserDTO(jwt);
        CollaboratorResponseDTO response = postCollaboratorUseCase.create(
                request.companyId(),
                request.name(),
                request.email(),
                CollaboratorRole.MANAGER,
                request.active(),
                request.phone(),
                request.preferences(),
                authUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Manager criado com sucesso"));
    }

    @Operation(summary = "Editar manager", description = "Atualiza os dados de um colaborador com papel de MANAGER.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorUpdateRequestDTO.class), examples = @ExampleObject(value = COLLABORATOR_REQUEST_EXAMPLE)))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Manager atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorResponseDTO.class), examples = @ExampleObject(value = MANAGER_RESPONSE_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Manager não encontrado", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Role inválida", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Colaborador já existe nesta empresa com este e-mail", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    })
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PutMapping("/managers/{id}")
    public ResponseEntity<ApiResponse<CollaboratorResponseDTO>> editManager(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "UUID do manager") @PathVariable UUID id,
            @Valid @RequestBody CollaboratorUpdateRequestDTO request) {

        AuthUserDTO authUser = JwtUtils.toAuthUserDTO(jwt);
        CollaboratorResponseDTO response = editCollaboratorUseCase.execute(
                request.companyId(),
                id,
                request.name(),
                request.email(),
                request.phone(),
                request.active(),
                request.preferences(),
                CollaboratorRole.MANAGER,
                authUser);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(response, "Manager atualizado com sucesso"));
    }

    @Operation(summary = "Buscar manager por ID", description = "Retorna os dados de um manager pelo seu UUID.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Manager encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorResponseDTO.class), examples = @ExampleObject(value = MANAGER_RESPONSE_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Manager não encontrado", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Role inválida", content = @Content)
    })
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER')")
    @GetMapping("/managers/{id}")
    public ResponseEntity<ApiResponse<CollaboratorResponseDTO>> getManagerById(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "UUID do manager") @PathVariable UUID id) {
        AuthUserDTO authUser = JwtUtils.toAuthUserDTO(jwt);
        CollaboratorResponseDTO collaboratorResponseDTO = getCollaboratorByIdUseCase.execute(id, authUser);
        return ResponseEntity.ok(ApiResponse.success(collaboratorResponseDTO, "Manager encontrado"));
    }

    @Operation(summary = "Listar managers", description = "Retorna uma página de managers com filtros opcionais.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Managers listados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
    })
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping("/managers")
    public ResponseEntity<ApiResponse<PaginatedResponse<CollaboratorResponseDTO>>> getManagers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UUID companyId,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {
        Page<CollaboratorResponseDTO> managers = getAllManagersUseCase.execute(name, email, companyId, active,
                PageableUtils.normalize(pageable));
        return ResponseEntity.ok(ApiResponse.success(PaginatedResponse.from(managers), "Managers listados com sucesso"));
    }

    @Operation(summary = "Buscar sellers", description = "Retorna todos os sellers com filtros opcionais.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sellers encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SellerResponseDTO.class), examples = @ExampleObject(value = SELLER_RESPONSE_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Seller não encontrado", content = @Content)
    })
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER')")
    @GetMapping("/sellers")
    public ResponseEntity<ApiResponse<PaginatedResponse<SellerResponseDTO>>> getSellers(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "Filtro por nome (contendo)") @RequestParam(required = false) String name,
            @Parameter(description = "Filtro por email (exato)") @RequestParam(required = false) String email,
            @Parameter(description = "Filtro por companyId") @RequestParam(required = false) UUID companyId,
            @Parameter(description = "Filtro por status (ativo ou inativo)") @RequestParam(required = false) Boolean active,
            @Parameter(description = "Paginação e ordenação") Pageable pageable) {

        AuthUserDTO authUser = JwtUtils.toAuthUserDTO(jwt);
        Page<SellerResponseDTO> sellers = getAllSellersUseCase.execute(name, email, companyId, active,
                PageableUtils.normalize(pageable), authUser);
        return ResponseEntity.ok(ApiResponse.success(PaginatedResponse.from(sellers), "Sellers listados com sucesso"));
    }


    @Operation(summary = "Cadastrar seller", description = "Cria um novo colaborador com o papel de SELLER.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorRequestDTO.class), examples = @ExampleObject(value = COLLABORATOR_REQUEST_EXAMPLE)))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Seller criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorResponseDTO.class), examples = @ExampleObject(value = SELLER_RESPONSE_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Colaborador já existe nesta empresa com este e-mail", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    })
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER')")
    @PostMapping("/sellers")
    public ResponseEntity<ApiResponse<CollaboratorResponseDTO>> createSeller(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CollaboratorRequestDTO request) {

        AuthUserDTO authUser = JwtUtils.toAuthUserDTO(jwt);
        CollaboratorResponseDTO response = postCollaboratorUseCase.create(
                request.companyId(),
                request.name(),
                request.email(),
                CollaboratorRole.SELLER,
                request.active(),
                request.phone(),
                request.preferences(),
                authUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Seller criado com sucesso"));
    }

    @Operation(summary = "Editar seller", description = "Atualiza os dados de um colaborador com papel de SELLER.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorUpdateRequestDTO.class), examples = @ExampleObject(value = COLLABORATOR_REQUEST_EXAMPLE)))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Seller atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorResponseDTO.class), examples = @ExampleObject(value = SELLER_RESPONSE_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Seller não encontrado", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Colaborador já existe nesta empresa com este e-mail", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    })
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER')")
    @PutMapping("/sellers/{id}")
    public ResponseEntity<ApiResponse<CollaboratorResponseDTO>> editSeller(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "UUID do seller") @PathVariable UUID id,
            @Valid @RequestBody CollaboratorUpdateRequestDTO request) {

        AuthUserDTO authUser = JwtUtils.toAuthUserDTO(jwt);
        CollaboratorResponseDTO response = editCollaboratorUseCase.execute(
                request.companyId(),
                id,
                request.name(),
                request.email(),
                request.phone(),
                request.active(),
                request.preferences(),
                CollaboratorRole.SELLER,
                authUser);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(response, "Seller atualizado com sucesso"));
    }

    @Operation(summary = "Buscar seller por ID", description = "Retorna os dados de um seller pelo seu UUID.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Seller encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SellerWithMeetingsResponseDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Seller não encontrado", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Role inválida", content = @Content),
    })
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER')")
    @GetMapping("/sellers/{id}")
    public ResponseEntity<ApiResponse<SellerWithMeetingsResponseDTO>> getSellerById(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "UUID do seller") @PathVariable UUID id) {

        AuthUserDTO authUser = JwtUtils.toAuthUserDTO(jwt);
        SellerWithMeetingsResponseDTO sellerWithMeetingsResponseDTO = getSellerByIdUseCase.execute(id, authUser);
        return ResponseEntity.ok(ApiResponse.success(sellerWithMeetingsResponseDTO, "Seller encontrado"));
    }

    @Operation(summary = "Definir senha do colaborador", description = "Define ou atualiza a senha de um colaborador.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorPasswordRequestDTO.class), examples = @ExampleObject(value = COLLABORATOR_PASSWORD_REQUEST_EXAMPLE)))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Senha definida com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Colaborador não encontrado", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    })
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER', 'SELLER')")
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> setPassword(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "UUID do colaborador") @PathVariable UUID id,
            @Valid @RequestBody CollaboratorPasswordRequestDTO request) {

        AuthUserDTO authUser = JwtUtils.toAuthUserDTO(jwt);
        setCollaboratorPasswordUseCase.execute(id, request.password(), authUser);
        return ResponseEntity.noContent().build();
    }
}
