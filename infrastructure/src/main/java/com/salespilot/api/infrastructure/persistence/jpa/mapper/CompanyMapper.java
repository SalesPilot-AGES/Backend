package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import org.springframework.stereotype.Component;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;

@Component
public class CompanyMapper {
    
    public Company toDomainEntity(CompanyEntity entity) {
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
