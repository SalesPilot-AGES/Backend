package com.salespilot.api.application.usecase;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.exception.CollaboratorAlreadyExistsException;
import com.salespilot.api.application.exception.InvalidCollaboratorRoleException;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.CompanyQueryService;
import com.salespilot.api.application.utils.CollaboratorAccessUtils;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;

import java.util.UUID;

public class EditCollaboratorUseCase {
    private final CollaboratorRepository repository;
    private final CollaboratorQueryService collaboratorQueryService;
    private final CompanyQueryService companyQueryService;
    private final CollaboratorAssembler assembler;

    public EditCollaboratorUseCase(CollaboratorRepository repository, CollaboratorQueryService collaboratorQueryService, CompanyQueryService companyQueryService, CollaboratorAssembler assembler) {
        this.repository = repository;
        this.collaboratorQueryService = collaboratorQueryService;
        this.companyQueryService = companyQueryService;
        this.assembler = assembler;
    }

    public CollaboratorResponseDTO execute(UUID companyId, UUID collaboratorId, String name, String email, String phone, boolean active, CollaboratorPreferences collaboratorPreferences, CollaboratorRole role, AuthUserDTO authUser){
        CollaboratorAccessUtils.grantAccess(companyId, authUser);

        Company company = companyQueryService.getOrThrowById(companyId);

        Collaborator existing = collaboratorQueryService.getOrThrowById(collaboratorId);

        if(existing.getRole() != role) {
            throw new InvalidCollaboratorRoleException(existing.getRole(), role);
        }

        boolean companyChanged = !existing.getCompanyId().equals(companyId);
        boolean emailChanged = !existing.getEmail().equals(email);

        if ((companyChanged || emailChanged) && repository.existsByCompanyIdAndEmail(companyId, email)) {
             throw new CollaboratorAlreadyExistsException(companyId, email);
        }

        Collaborator collaborator = repository.update(companyId, collaboratorId, name, email, phone, active, collaboratorPreferences);

        return assembler.toDTO(collaborator, company);
    }
}
