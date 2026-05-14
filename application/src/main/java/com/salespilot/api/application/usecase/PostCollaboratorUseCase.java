package com.salespilot.api.application.usecase;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.exception.CollaboratorAlreadyExistsException;
import com.salespilot.api.application.queryservice.CompanyQueryService;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;

import java.util.UUID;

public class PostCollaboratorUseCase {
    private final CollaboratorRepository collaboratorRepository;
    private final CompanyQueryService companyQueryService;
    private final CollaboratorAssembler assembler;

    public PostCollaboratorUseCase(CollaboratorRepository collaboratorRepository, CompanyQueryService companyQueryService, CollaboratorAssembler assembler) {
        this.collaboratorRepository = collaboratorRepository;
        this.companyQueryService = companyQueryService;
        this.assembler = assembler;
    }

    public CollaboratorResponseDTO create(UUID companyId, String name, String email, CollaboratorRole role, boolean active, String phone, CollaboratorPreferences collaboratorPreferences, Integer averageFeeling) {
        if(collaboratorRepository.existsByCompanyIdAndEmail(companyId, email)) {
            throw new CollaboratorAlreadyExistsException(companyId, email);
        }

        Company company = companyQueryService.getOrThrowById(companyId);

        Collaborator collaborator = collaboratorRepository.create(companyId, name, email, role, active, phone, collaboratorPreferences, averageFeeling);

        return assembler.toDTO(collaborator, company);
    }
}
