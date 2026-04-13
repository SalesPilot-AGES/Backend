package com.salespilot.api.application.usecase;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;

import java.util.Optional;
import java.util.UUID;

public class GetCompanyByIdUseCase {
    private final CompanyRepository companyRepository;

    public GetCompanyByIdUseCase(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Optional<Company> execute(UUID id) {
        return companyRepository.getCompanyById(id);
    }
}
