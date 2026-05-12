package com.salespilot.api.application.usecase;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.exception.InvalidCollaboratorRoleException;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.CompanyQueryService;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CollaboratorRole;

import java.util.UUID;

public class GetCollaboratorByIdUseCase {
    private final CollaboratorQueryService  collaboratorQueryService;
    private final CompanyQueryService companyQueryService;
    private final CollaboratorAssembler assembler;

    public GetCollaboratorByIdUseCase(CollaboratorQueryService collaboratorQueryService, CompanyQueryService companyQueryService, CollaboratorAssembler assembler) {
        this.collaboratorQueryService = collaboratorQueryService;
        this.companyQueryService = companyQueryService;
        this.assembler = assembler;
    }

    public CollaboratorResponseDTO execute(UUID id) {
        Collaborator collaborator = collaboratorQueryService.getOrThrowCollaboratorById(id);

        if(collaborator.getRole() != CollaboratorRole.MANAGER) {
            throw new InvalidCollaboratorRoleException(collaborator.getRole(), CollaboratorRole.MANAGER);
        }

        Company company = companyQueryService.getOrThrowCompanyById(collaborator.getCompanyId());

        return assembler.toDTO(
                collaborator,
                company
        );
    }
}
