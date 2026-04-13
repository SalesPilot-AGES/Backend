package com.salespilot.api.application.usecase;

import java.time.LocalDateTime;
import java.util.UUID;

import com.salespilot.api.application.dto.EditCollaboratorDTO;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.CollaboratorRepository;

public class EditCollaboratorUseCase {
    private final CollaboratorRepository repository;

    public EditCollaboratorUseCase(CollaboratorRepository repository){
        this.repository = repository;
    }

    public EditCollaboratorDTO execute(UUID uuid, UUID companyId, String name, CollaboratorRole role, String email, boolean active, String preferences, LocalDateTime createdAt, Company company){
        Collaborator collaborator = repository.editCollaborator(companyId, name, email, active, preferences);
        return new EditCollaboratorDTO(collaborator.getId(), collaborator.getCompanyId(), collaborator.getName(), collaborator.getRole(), collaborator.getEmail(), collaborator.isActive(), collaborator.getPreferences(), collaborator.getCreatedAt(), company);
    }
}
