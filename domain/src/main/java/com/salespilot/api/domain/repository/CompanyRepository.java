package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CompanyPlan;

public interface CompanyRepository {
    boolean existsByTaxId(String taxId);

    Company createCompany(String name, String taxId, CompanyPlan plan, boolean active);
}
