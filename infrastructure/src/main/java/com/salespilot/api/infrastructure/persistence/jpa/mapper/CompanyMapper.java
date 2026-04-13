package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyMapper {
    public Company toDomain(CompanyEntity companyEntity) {
        return new Company(companyEntity.getId(),
                companyEntity.getName(),
                companyEntity.getTaxId(),
                companyEntity.getPlan(),
                companyEntity.isActive(),
                companyEntity.getCreatedAt(),
                List.of());
    }
}
