package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.domain.repository.CompanyRepository;

import java.util.UUID;

public class UpdateCompanyUseCase {
    private final CompanyRepository companyRepository;

    public UpdateCompanyUseCase(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyResponseDTO execute(UUID id, String name, String plan, boolean active) {
        return companyRepository.updateCompany(id, name, plan, active)
                .map(CompanyResponseDTO::from)
                .orElseThrow(() -> new CompanyNotFoundException(id));
    }
}
