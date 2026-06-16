package com.salespilot.api.application.usecase;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.CompanyQueryService;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;

public class GetAuthenticatedUserUseCase {
    private final CollaboratorQueryService collaboratorQueryService;
    private final CompanyQueryService companyQueryService;
    private final CollaboratorAssembler assembler;

    public GetAuthenticatedUserUseCase(CollaboratorQueryService collaboratorQueryService, CompanyQueryService companyQueryService, CollaboratorAssembler assembler) {
        this.collaboratorQueryService = collaboratorQueryService;
        this.companyQueryService = companyQueryService;
        this.assembler = assembler;
    }

    public CollaboratorResponseDTO execute(AuthUserDTO authUser) {
        Collaborator collaborator = collaboratorQueryService.getOrThrowById(authUser.id());
        Company company = companyQueryService.getOrThrowById(collaborator.getCompanyId());

        return assembler.toDTO(collaborator, company);
    }
}
