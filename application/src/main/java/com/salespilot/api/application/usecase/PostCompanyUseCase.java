package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.exception.TaxIdAlreadyExists;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;

public class PostCompanyUseCase {
    private final CompanyRepository repository;

    public PostCompanyUseCase(CompanyRepository repository) {
        this.repository = repository;
    }

    public CompanyResponseDTO create(
            String name,
            String taxId,
            String status,
            Integer maxSellers,
            Integer maxManagers,
            String notes) {
        if (taxId != null && !taxId.isBlank() && repository.existsByTaxId(taxId)) {
            throw new TaxIdAlreadyExists();
        }

        String normalizedStatus = status == null || status.isBlank() ? "active" : status.toLowerCase();
        int normalizedMaxSellers = maxSellers == null ? 0 : Math.max(maxSellers, 0);
        int normalizedMaxManagers = maxManagers == null ? 0 : Math.max(maxManagers, 0);

        Company company = repository.createCompany(
                name,
                taxId,
                normalizedStatus,
                normalizedMaxSellers,
                normalizedMaxManagers,
                notes);

        return new CompanyResponseDTO(
                company.getId(),
                company.getName(),
                company.getTaxId(),
                company.getStatus(),
                company.getMaxSellers(),
                company.getMaxManagers(),
                company.getCreatedAt(),
                company.getUpdatedAt());
    }
}
