package com.salespilot.api.application.usecase;

import java.util.UUID;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;

public class GetCollaboratorByIdUseCase {
    private final CollaboratorRepository collaboratorRepository;
    private final CompanyRepository companyRepository;

    public GetCollaboratorByIdUseCase(CollaboratorRepository collaboratorRepository, CompanyRepository companyRepository) {
        this.collaboratorRepository = collaboratorRepository;
        this.companyRepository = companyRepository;
    }

    public CollaboratorResponseDTO execute(UUID id) {
        Collaborator collaborator = collaboratorRepository.getCollaboratorById(id).orElseThrow(
            () -> new CollaboratorNotFoundException(id)
        );

        UUID companyId = collaborator.getCompany().getId();

        CompanyResponseDTO companyDto = companyRepository.getCompanyById(companyId)
                .map(CompanyResponseDTO::from)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        return new CollaboratorResponseDTO(
                collaborator.getId(),
                collaborator.getCompany().getId(),
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
