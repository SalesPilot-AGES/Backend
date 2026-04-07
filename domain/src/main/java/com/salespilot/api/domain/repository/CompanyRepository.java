package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Company;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    Optional<Company> findById(UUID id);
    Company save(Company company);
}