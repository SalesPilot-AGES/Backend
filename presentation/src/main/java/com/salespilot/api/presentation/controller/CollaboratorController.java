package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.usecase.PostCollaboratorUseCase;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.presentation.dto.CollaboratorRequestDTO;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public CollaboratorController(PostCollaboratorUseCase postCollaboratorUseCase) {
        this.postCollaboratorUseCase = postCollaboratorUseCase;
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

    // @PutMapping("/{id}")
    // public ResponseEntity<CollaboratorResponseDTO> editCollaborator(@PathVariable UUID id, @Valid @RequestBody CollaboratorRequestDTO request) {
    //     //TODO: process PUT request
        
    //     return entity;
    // }
}
