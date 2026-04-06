package com.salespilot.api.domain.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.salespilot.api.domain.entity.Company;

public interface CompanyRepository {
    Page<Company> getAllCompanies(String nome, String cnpj, String plano, Boolean isActive, Pageable pageable);
}
