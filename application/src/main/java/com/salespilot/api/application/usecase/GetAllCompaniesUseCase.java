package com.salespilot.api.application.usecase;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.domain.enums.CompanyPlan;
import com.salespilot.api.domain.repository.CompanyRepository;

public class GetAllCompaniesUseCase {

    private final CompanyRepository repository;

    public GetAllCompaniesUseCase(CompanyRepository repository) {
        this.repository = repository;
    }

    public Page<CompanyResponseDTO> execute(String name, String taxId, CompanyPlan plan, Boolean active, Pageable pageable) {
        return repository.getAllCompanies(name, taxId, plan, active, pageable)
        .map(e -> new CompanyResponseDTO(
            e.getId(),
            e.getName(),
            e.getTaxId(),
            e.getPlan(),
            e.isActive(),
            e.getCreatedAt()
        ));
    }
}
