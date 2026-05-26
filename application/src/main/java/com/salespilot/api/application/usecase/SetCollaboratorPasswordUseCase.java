package com.salespilot.api.application.usecase;

import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.service.PasswordHasher;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.repository.CollaboratorRepository;

import java.util.UUID;

public class SetCollaboratorPasswordUseCase {
    private final CollaboratorRepository collaboratorRepository;
    private final PasswordHasher passwordHasher;

    public SetCollaboratorPasswordUseCase(CollaboratorRepository collaboratorRepository, PasswordHasher passwordHasher) {
        this.collaboratorRepository = collaboratorRepository;
        this.passwordHasher = passwordHasher;
    }

    public void execute(UUID collaboratorId, String rawPassword) {
        Collaborator collaborator = collaboratorRepository.getCollaboratorById(collaboratorId)
                .orElseThrow(() -> new CollaboratorNotFoundException(collaboratorId));

        String passwordHash = passwordHasher.hash(rawPassword);
        collaboratorRepository.updatePasswordHash(collaborator.getId(), passwordHash);
    }
}

