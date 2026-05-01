package com.salespilot.api.application.usecase;

import java.util.UUID;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.exception.CollaboratorAlreadyExistsException;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.CompanyQueryService;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;

public class EditCollaboratorUseCase {
    private final CollaboratorRepository repository;
    private final CompanyQueryService companyQueryService;
    private final CollaboratorQueryService collaboratorQueryService;
    private final CollaboratorAssembler assembler;

    public EditCollaboratorUseCase(CollaboratorRepository repository, CompanyQueryService companyQueryService, CollaboratorQueryService collaboratorQueryService, CollaboratorAssembler assembler) {
        this.repository = repository;
        this.companyQueryService = companyQueryService;
        this.collaboratorQueryService = collaboratorQueryService;
        this.assembler = assembler;
    }

    public CollaboratorResponseDTO execute(UUID companyId, UUID collaboratorId, String name, String email, boolean active, CollaboratorPreferences collaboratorPreferences) {
        Collaborator existing = collaboratorQueryService.getCollaboratorById(collaboratorId);

        if (!existing.getEmail().equals(email) && repository.existsByCompanyIdAndEmail(companyId, email)) {
            throw new CollaboratorAlreadyExistsException(companyId, email);
        }

        Company company = companyQueryService.getCompanyById(companyId);

        Collaborator collaborator = repository.update(companyId, collaboratorId, name, email, active, collaboratorPreferences);

        return assembler.toDTO(collaborator, company);
    }
}
