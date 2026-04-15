package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.usecase.EditCollaboratorUseCase;
import com.salespilot.api.application.usecase.GetCollaboratorByIdUseCase;
import com.salespilot.api.application.usecase.PostCollaboratorUseCase;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.presentation.dto.CollaboratorRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Collaborators")
@RestController
@RequestMapping("/api/collaborators")
public class CollaboratorController {

    private final PostCollaboratorUseCase postCollaboratorUseCase;
    private final GetCollaboratorByIdUseCase getCollaboratorByIdUseCase;
    private final EditCollaboratorUseCase editCollaboratorUseCase;

    public CollaboratorController(PostCollaboratorUseCase postCollaboratorUseCase,
                                  EditCollaboratorUseCase editCollaboratorUseCase,
                                  GetCollaboratorByIdUseCase getCollaboratorByIdUseCase) {
        this.postCollaboratorUseCase = postCollaboratorUseCase;
        this.editCollaboratorUseCase = editCollaboratorUseCase;
        this.getCollaboratorByIdUseCase = getCollaboratorByIdUseCase;
    }

    @Operation(summary = "Cadastrar manager", description = "Cria um novo colaborador com o papel de MANAGER.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Manager criado com sucesso",
                    content = @Content(schema = @Schema(implementation = CollaboratorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content),
            @ApiResponse(responseCode = "409", description = "Colaborador já existe nesta empresa com este e-mail", content = @Content),
            @ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    })
    @PostMapping("/managers")
    public ResponseEntity<CollaboratorResponseDTO> createManager(@Valid @RequestBody CollaboratorRequestDTO request) {
        CollaboratorResponseDTO response = postCollaboratorUseCase.create(
                request.companyId(),
                request.name(),
                request.email(),
                CollaboratorRole.MANAGER,
                request.active(),
                request.preferences()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Editar manager", description = "Atualiza os dados de um colaborador com papel de MANAGER.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Manager atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = CollaboratorResponseDTO.class))),
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
            @ApiResponse(responseCode = "200", description = "Manager encontrado",
                    content = @Content(schema = @Schema(implementation = CollaboratorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Manager não encontrado", content = @Content)
    })
    @GetMapping("/managers/{id}")
    public ResponseEntity<CollaboratorResponseDTO> getManagerById(
            @Parameter(description = "UUID do manager") @PathVariable UUID id) {
        CollaboratorResponseDTO collaboratorResponseDTO = getCollaboratorByIdUseCase.execute(id);
        return ResponseEntity.ok(collaboratorResponseDTO);
    }
}
