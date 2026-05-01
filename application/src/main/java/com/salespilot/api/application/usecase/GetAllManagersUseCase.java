package com.salespilot.api.application.usecase;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class GetAllManagersUseCase {

    private final CollaboratorRepository repository;
    private final CompanyRepository companyRepository;
    private final CollaboratorAssembler assembler;

    public GetAllManagersUseCase(CollaboratorRepository repository, CompanyRepository companyRepository, CollaboratorAssembler assembler) {
        this.repository = repository;
        this.companyRepository = companyRepository;
        this.assembler = assembler;
    }

    public Page<CollaboratorResponseDTO> execute(String name, String email, UUID companyId, Boolean active, Pageable pageable) {
        Company company = companyRepository.getCompanyById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        return repository.getManagers(name, email, companyId, active, pageable)
                .map(c -> assembler.toDTO(c, company));
    }
}
