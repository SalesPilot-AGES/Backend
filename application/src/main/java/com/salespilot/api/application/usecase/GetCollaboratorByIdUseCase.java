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
    private CollaboratorRepository collaboratorRepository;
    private CompanyRepository companyRepository;

    public GetCollaboratorByIdUseCase(CollaboratorRepository collaboratorRepository, CompanyRepository companyRepository) {
        this.collaboratorRepository = collaboratorRepository;
        this.companyRepository = companyRepository;
    }

    public CollaboratorResponseDTO execute(UUID id) {
        /* Collaborator collaborator = collaboratorRepository.getCollaboratorById(id).map(CollaboratorResponseDTO::from); */

        Collaborator collaborator = collaboratorRepository.getCollaboratorById(id).orElseThrow(
            () -> new CollaboratorNotFoundException(id)
        );

        UUID companyId = collaborator.getCompanyId();

        CompanyResponseDTO companyDto = companyRepository.getCompanyById(companyId)
                .map(CompanyResponseDTO::from)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

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
