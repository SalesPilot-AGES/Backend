package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Company;

public interface CompanyRepository {
    boolean existsByTaxId(String taxId);

    Company createCompany(String name, String taxId, String plan, boolean active);
}
