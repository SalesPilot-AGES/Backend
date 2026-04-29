package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.usecase.EditCollaboratorUseCase;
import com.salespilot.api.application.usecase.GetAllManagersUseCase;
import com.salespilot.api.application.usecase.GetCollaboratorByIdUseCase;
import com.salespilot.api.application.usecase.PostCollaboratorUseCase;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.presentation.dto.CollaboratorRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.salespilot.api.presentation.utils.PageableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@Tag(name = "Collaborators")
@RestController
@RequestMapping("/api/collaborators")
public class CollaboratorController {

    private final PostCollaboratorUseCase postCollaboratorUseCase;
    private final EditCollaboratorUseCase editCollaboratorUseCase;
    private final GetCollaboratorByIdUseCase getCollaboratorByIdUseCase;
    private final GetAllManagersUseCase getAllManagersUseCase;

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
              "average_feeling": null,
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
              "average_feeling": 0,
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

    public CollaboratorController(PostCollaboratorUseCase postCollaboratorUseCase,
            EditCollaboratorUseCase editCollaboratorUseCase,
            GetCollaboratorByIdUseCase getCollaboratorByIdUseCase,
            GetAllManagersUseCase getAllManagersUseCase) {
        this.postCollaboratorUseCase = postCollaboratorUseCase;
        this.editCollaboratorUseCase = editCollaboratorUseCase;
        this.getCollaboratorByIdUseCase = getCollaboratorByIdUseCase;
        this.getAllManagersUseCase = getAllManagersUseCase;
    }

    @Operation(summary = "Cadastrar manager", description = "Cria um novo colaborador com o papel de MANAGER.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorRequestDTO.class), examples = @ExampleObject(value = COLLABORATOR_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Manager criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorResponseDTO.class), examples = @ExampleObject(value = MANAGER_RESPONSE_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content),
            @ApiResponse(responseCode = "409", description = "Colaborador já existe nesta empresa com este e-mail", content = @Content),
            @ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    })
    @PostMapping("/managers")
    public ResponseEntity<CollaboratorResponseDTO> createManager(
            @Valid @RequestBody CollaboratorRequestDTO request) {
        CollaboratorResponseDTO response = postCollaboratorUseCase.create(
                request.companyId(),
                request.name(),
                request.email(),
                CollaboratorRole.MANAGER,
                request.active(),
                request.phone(),
                request.preferences(),
                null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Editar manager", description = "Atualiza os dados de um colaborador com papel de MANAGER.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorRequestDTO.class), examples = @ExampleObject(value = COLLABORATOR_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Manager atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorResponseDTO.class), examples = @ExampleObject(value = MANAGER_RESPONSE_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Manager não encontrado", content = @Content),
            @ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    })
    @PutMapping("/managers/{id}")
    public ResponseEntity<CollaboratorResponseDTO> editCollaborator(
            @Parameter(description = "UUID do manager") @PathVariable UUID id,
            @Valid @RequestBody CollaboratorRequestDTO request) {
        CollaboratorResponseDTO response = editCollaboratorUseCase.execute(
                request.companyId(),
                id,
                request.name(),
                request.email(),
                request.active(),
                request.preferences());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Buscar manager por ID", description = "Retorna os dados de um manager pelo seu UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Manager encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorResponseDTO.class), examples = @ExampleObject(value = MANAGER_RESPONSE_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Manager não encontrado", content = @Content)
    })
    @GetMapping("/managers/{id}")
    public ResponseEntity<CollaboratorResponseDTO> getManagerById(
            @Parameter(description = "UUID do manager") @PathVariable UUID id) {
        CollaboratorResponseDTO collaboratorResponseDTO = getCollaboratorByIdUseCase.execute(id);
        return ResponseEntity.ok(collaboratorResponseDTO);
    }

    @GetMapping("/managers")
    public ResponseEntity<Page<CollaboratorResponseDTO>> getManagers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UUID companyId,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {
        return ResponseEntity
                .ok(getAllManagersUseCase.execute(name, email, companyId, active, PageableUtils.normalize(pageable)));
    }

    @Operation(summary = "Cadastrar seller", description = "Cria um novo colaborador com o papel de SELLER.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorRequestDTO.class), examples = @ExampleObject(value = COLLABORATOR_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Seller criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollaboratorResponseDTO.class), examples = @ExampleObject(value = SELLER_RESPONSE_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content),
            @ApiResponse(responseCode = "409", description = "Colaborador já existe nesta empresa com este e-mail", content = @Content),
            @ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    })
    @PostMapping("/sellers")
    public ResponseEntity<CollaboratorResponseDTO> createSeller(
            @Valid @RequestBody CollaboratorRequestDTO request) {
        CollaboratorResponseDTO response = postCollaboratorUseCase.create(
                request.companyId(),
                request.name(),
                request.email(),
                CollaboratorRole.SELLER,
                request.active(),
                request.phone(),
                request.preferences(),
                0);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
