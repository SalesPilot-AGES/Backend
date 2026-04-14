package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    public Company toDomain(CompanyEntity companyEntity) {
        return new Company(companyEntity.getId(),
                companyEntity.getName(),
                companyEntity.getTaxId(),
                companyEntity.getStatus(),
                companyEntity.getMaxSellers(),
                companyEntity.getMaxManagers(),
                companyEntity.getNotes(),
                companyEntity.getCreatedAt(),
                companyEntity.getUpdatedAt());
    }
}
