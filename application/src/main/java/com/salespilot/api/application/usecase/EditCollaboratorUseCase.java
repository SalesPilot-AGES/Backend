package com.salespilot.api.application.usecase;

import java.util.UUID;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.exception.CollaboratorAlreadyExistsException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;

public class EditCollaboratorUseCase {
    private final CollaboratorRepository repository;
    private final CollaboratorAssembler assembler;

    public EditCollaboratorUseCase(CollaboratorRepository repository, CollaboratorAssembler assembler){
        this.repository = repository;
        this.assembler = assembler;
    }

    public CollaboratorResponseDTO execute(UUID companyId, UUID collaboratorId, String name, String email, boolean active, CollaboratorPreferences collaboratorPreferences){
        Collaborator existing = repository.getCollaboratorById(collaboratorId)
                .orElseThrow(() -> new CollaboratorNotFoundException(collaboratorId));

        if (!existing.getEmail().equals(email) && repository.existsByCompanyIdAndEmail(companyId, email)) {
            throw new CollaboratorAlreadyExistsException(companyId, email);
        }

        Collaborator collaborator = repository.update(companyId, collaboratorId, name, email, active, collaboratorPreferences);

        return assembler.toDTO(collaborator);
    }
}
