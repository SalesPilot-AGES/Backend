package com.salespilot.api.application.usecase;

import java.util.UUID;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;

public class GetCollaboratorByIdUseCase {
    private final CollaboratorRepository collaboratorRepository;
    private final CompanyRepository companyRepository;
    private final CollaboratorAssembler assembler;

    public GetCollaboratorByIdUseCase(CollaboratorRepository collaboratorRepository, CompanyRepository companyRepository, CollaboratorAssembler assembler) {
        this.collaboratorRepository = collaboratorRepository;
        this.companyRepository = companyRepository;
        this.assembler = assembler;
    }

    public CollaboratorResponseDTO execute(UUID id) {
        Collaborator collaborator = collaboratorRepository.getCollaboratorById(id)
                .orElseThrow(() -> new CollaboratorNotFoundException(id));

        Company company = companyRepository.getCompanyById(collaborator.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException(collaborator.getCompanyId()));

        return assembler.toDTO(collaborator, company);
    }
}
