package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import java.sql.Timestamp;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;

public class CompanyMapper {
    
    public static Company toDomainEntity(CompanyEntity entity) {
        return new Company (
            entity.getId(),
            entity.getNome(),
            entity.getCnpj(),
            entity.getPlano(),
            entity.isActive(),
            entity.getCreatedAt()
        );
    }
}
