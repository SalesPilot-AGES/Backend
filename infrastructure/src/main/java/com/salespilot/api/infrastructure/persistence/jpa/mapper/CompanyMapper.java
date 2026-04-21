package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyMapper {
    public Company toDomain(CompanyEntity entity) {
        return new Company(
                entity.getId(),
                entity.getName(),
                entity.getTaxId(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                null,
                List.of()
        );
    }
}
