package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Enterprise;

import java.util.UUID;

public interface EnterpriseRepository {
    Enterprise getEnterpriseById(UUID id);
}