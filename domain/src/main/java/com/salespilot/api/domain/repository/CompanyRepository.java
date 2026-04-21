package com.salespilot.api.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.salespilot.api.domain.entity.Company;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    boolean existsByTaxId(String taxId);
    Company createCompany(String name, String taxId, boolean active);
    Page<Company> getAllCompanies(String name, String taxId, Boolean active, Pageable pageable);
    Optional<Company> getCompanyById(UUID id);
    Optional<Company> updateCompany(UUID id, String name, boolean active);
}
