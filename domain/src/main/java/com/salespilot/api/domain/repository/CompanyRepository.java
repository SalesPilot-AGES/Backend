package com.salespilot.api.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CompanyPlan;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    boolean existsByTaxId(String taxId);
    Company createCompany(String name, String taxId, CompanyPlan plan, boolean active);
    Page<Company> getAllCompanies(String name, String taxId, CompanyPlan plan, Boolean active, Pageable pageable);
    Optional<Company> getCompanyById(UUID id);
    boolean existsById(UUID id);
}