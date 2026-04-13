package com.salespilot.api.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.salespilot.api.domain.entity.Company;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    Page<Company> getAllCompanies(String name, String taxId, String status, Pageable pageable);
    Optional<Company> getCompanyById(UUID id);
}
