package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Company;

import java.util.UUID;

public interface CompanyRepository {
    Company getCompanyById(UUID id);
}