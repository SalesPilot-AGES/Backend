package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;

public class PostCompanyUseCase {
    private final CompanyRepository repository;

    public PostCompanyUseCase(CompanyRepository repository) {
        this.repository = repository;
    }

    public CompanyResponseDTO create(String nome, String cnpj, String plano, boolean is_active) {
        Company company = repository.createCompany(nome, cnpj, plano, is_active);
        return new CompanyResponseDTO(company.getId(), company.getName(), company.getTaxId(), company.getPlano(), company.isActive(), company.getCreatedAt().toInstant().toString());
    }
}
