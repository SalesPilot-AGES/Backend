package com.salespilot.api.application.usecase;

import java.util.UUID;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.repository.CollaboratorRepository;

public class GetCollaboratorByIdUseCase {
    private final CollaboratorRepository collaboratorRepository;
    private final CollaboratorAssembler assembler;

    public GetCollaboratorByIdUseCase(CollaboratorRepository collaboratorRepository, CollaboratorAssembler assembler) {
        this.collaboratorRepository = collaboratorRepository;
        this.assembler = assembler;
    }

    public CollaboratorResponseDTO execute(UUID id) {
        Collaborator collaborator = collaboratorRepository.getCollaboratorById(id)
                .orElseThrow(() -> new CollaboratorNotFoundException(id));

        return assembler.toDTO(collaborator);
    }
}
