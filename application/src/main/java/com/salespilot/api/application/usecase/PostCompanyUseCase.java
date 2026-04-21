package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.exception.TaxIdAlreadyExists;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CompanyPlan;
import com.salespilot.api.domain.repository.CompanyRepository;

public class PostCompanyUseCase {
    private final CompanyRepository repository;

    public PostCompanyUseCase(CompanyRepository repository) {
        this.repository = repository;
    }

    public CompanyResponseDTO create(String name, String taxId, String plan, boolean active) {
        if (repository.existsByTaxId(taxId)) {
            throw new TaxIdAlreadyExists();
        }

        CompanyPlan companyPlan = CompanyPlan.valueOf(plan.toUpperCase());
        Company company = repository.createCompany(name, taxId, companyPlan, active);
        return CompanyResponseDTO.from(company);
    }
}
