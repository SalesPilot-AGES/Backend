package com.salespilot.api.domain.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.salespilot.api.domain.entity.Enterprise;

public interface EnterpriseRepository {
    Page<Enterprise> getAllEnterprises(String nome, String cnpj, String plano, Boolean isActive, Pageable pageable);
}
