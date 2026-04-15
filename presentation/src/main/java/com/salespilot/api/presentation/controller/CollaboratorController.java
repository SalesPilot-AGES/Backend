package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.usecase.EditCollaboratorUseCase;
import com.salespilot.api.application.usecase.GetCollaboratorByIdUseCase;
import com.salespilot.api.application.usecase.PostCollaboratorUseCase;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.presentation.dto.CollaboratorRequestDTO;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/collaborators")
public class CollaboratorController {
    private final PostCollaboratorUseCase postCollaboratorUseCase;
    private final GetCollaboratorByIdUseCase getCollaboratorByIdUseCase;
    private final EditCollaboratorUseCase editCollaboratorUseCase;

    public CollaboratorController(PostCollaboratorUseCase postCollaboratorUseCase, EditCollaboratorUseCase editCollaboratorUseCase, GetCollaboratorByIdUseCase getCollaboratorByIdUseCase) {
        this.postCollaboratorUseCase = postCollaboratorUseCase;
        this.editCollaboratorUseCase = editCollaboratorUseCase;
        this.getCollaboratorByIdUseCase = getCollaboratorByIdUseCase;
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<CollaboratorResponseDTO> editCollaborator(@PathVariable UUID id, @Valid @RequestBody CollaboratorRequestDTO request) {
        CollaboratorResponseDTO response = editCollaboratorUseCase.execute(
            request.companyId(), 
            id,
            request.name(), 
            request.email(), 
            request.active(), 
            request.preferences());
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/managers/{id}")
    public ResponseEntity<CollaboratorResponseDTO> getManagerById(@PathVariable UUID id) {
        CollaboratorResponseDTO collaboratorResponseDTO = getCollaboratorByIdUseCase.execute(id);

        return ResponseEntity.ok(collaboratorResponseDTO);
    }
}
