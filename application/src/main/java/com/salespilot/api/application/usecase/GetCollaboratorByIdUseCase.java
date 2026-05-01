package com.salespilot.api.application.usecase;

import java.util.UUID;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.CompanyQueryService;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;

public class GetCollaboratorByIdUseCase {
    private final CollaboratorQueryService collaboratorQueryService;
    private final CompanyQueryService companyQueryService;
    private final CollaboratorAssembler assembler;

    public GetCollaboratorByIdUseCase(CollaboratorQueryService collaboratorQueryService, CompanyQueryService companyQueryService, CollaboratorAssembler assembler) {
        this.collaboratorQueryService = collaboratorQueryService;
        this.companyQueryService = companyQueryService;
        this.assembler = assembler;
    }

    public CollaboratorResponseDTO execute(UUID id) {
        Collaborator collaborator = collaboratorQueryService.getCollaboratorById(id);

        Company company = companyQueryService.getCompanyById(collaborator.getCompanyId());

        return assembler.toDTO(collaborator, company);
    }
}
