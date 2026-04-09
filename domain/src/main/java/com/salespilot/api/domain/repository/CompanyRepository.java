package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Company;

public interface CompanyRepository {
    Company createCompany(String nome, String cnpj, String plano, boolean is_active);
}
