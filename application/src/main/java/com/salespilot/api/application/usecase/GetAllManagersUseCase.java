package com.salespilot.api.application.usecase;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.queryservice.CompanyQueryService;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class GetAllManagersUseCase {

    private final CollaboratorRepository repository;
    private final CompanyQueryService companyQueryService;
    private final CollaboratorAssembler assembler;

    public GetAllManagersUseCase(CollaboratorRepository repository, CompanyQueryService companyQueryService, CollaboratorAssembler assembler) {
        this.repository = repository;
        this.companyQueryService = companyQueryService;
        this.assembler = assembler;
    }

    public Page<CollaboratorResponseDTO> execute(String name, String email, UUID companyId, Boolean active, Pageable pageable) {
        return repository.getManagers(name, email, companyId, active, pageable)
                .map(c -> {
                    Company company = companyQueryService.getOrThrowCompanyById(c.getCompanyId());

                    return assembler.toDTO(c, company);
                });
    }
}
