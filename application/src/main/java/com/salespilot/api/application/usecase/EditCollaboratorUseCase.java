package com.salespilot.api.application.usecase;

import java.util.UUID;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.exception.CollaboratorAlreadyExistsException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;

public class EditCollaboratorUseCase {
    private final CollaboratorRepository repository;
    private final CompanyRepository companyRepository;

    public EditCollaboratorUseCase(CollaboratorRepository repository, CompanyRepository companyRepository){
        this.repository = repository;
        this.companyRepository = companyRepository;
    }

    public CollaboratorResponseDTO execute(UUID companyId, UUID collaboratorId, String name, String email, boolean active, CollaboratorPreferences collaboratorPreferences){
        CompanyResponseDTO companyDto = companyRepository.getCompanyById(companyId)
                .map(CompanyResponseDTO::from)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        Collaborator existing = repository.getCollaboratorById(collaboratorId)
                .orElseThrow(() -> new CollaboratorNotFoundException(collaboratorId));

        if (!existing.getEmail().equals(email) && repository.existsByCompanyIdAndEmail(companyId, email)) {
            throw new CollaboratorAlreadyExistsException(companyId, email);
        }

        Collaborator collaborator = repository.update(companyId, collaboratorId, name, email, active, collaboratorPreferences);

        return new CollaboratorResponseDTO(
                collaborator.getId(),
                collaborator.getCompanyId(),
                collaborator.getName(),
                collaborator.getRole(),
                collaborator.getEmail(),
                collaborator.getPhone(),
                collaborator.isActive(),
                collaborator.getAverageFeeling(),
                collaborator.getPreferences(),
                collaborator.getCreatedAt(),
                collaborator.getUpdatedAt(),
                companyDto
        );
    }
}
