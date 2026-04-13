package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;

import java.util.UUID;

public class PostCollaboratorUseCase {
    private final CollaboratorRepository collaboratorRepository;
    private final CompanyRepository companyRepository;

    public PostCollaboratorUseCase(CollaboratorRepository collaboratorRepository, CompanyRepository companyRepository) {
        this.collaboratorRepository = collaboratorRepository;
        this.companyRepository = companyRepository;
    }

    public CollaboratorResponseDTO create(UUID companyId, String name, String email, String role, boolean active, CollaboratorPreferences collaboratorPreferences) {
        if (!companyRepository.existsById(companyId)) {
            //throw new invalidCompanyId
        }

        CollaboratorRole collaboratorRole = CollaboratorRole.valueOf(role);
        Collaborator collaborator = collaboratorRepository.create(companyId, name, email, collaboratorRole, active, collaboratorPreferences);
    }
}
