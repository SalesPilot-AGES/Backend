package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public Company toDomain(CompanyEntity entity) {
        if (entity == null) return null;
        
        return new Company(
            entity.getId(),
            entity.getCnpj(),
            entity.getCreatedAt(),
            entity.getName(),
            entity.getPlans(),
            entity.isActive()
        );
    }

    public CompanyEntity toEntity(Company domain) {
        if (domain == null) return null;

        return new CompanyEntity(
            domain.getId(),
            domain.getCnpj(),
            domain.getCreatedAt(),
            domain.getName(),
            domain.getPlans(),
            domain.isActive()
        );
    }
}