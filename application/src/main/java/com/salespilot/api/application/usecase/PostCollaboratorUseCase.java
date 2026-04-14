package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.exception.CompanyNotFoundException;
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

    public CollaboratorResponseDTO create(UUID companyId, String name, String email, CollaboratorRole role, boolean active, CollaboratorPreferences collaboratorPreferences) {
        CompanyResponseDTO companyDto = companyRepository.getCompanyById(companyId)
                .map(CompanyResponseDTO::from)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));;

        Collaborator collaborator = collaboratorRepository.create(companyId, name, email, role, active, collaboratorPreferences);

        return new CollaboratorResponseDTO(
                collaborator.getId(),
                collaborator.getCompanyId(),
                collaborator.getName(),
                collaborator.getRole(),
                collaborator.getEmail(),
                collaborator.isActive(),
                collaborator.getPreferences(),
                collaborator.getCreatedAt(),
                companyDto
        );
    }
}
