package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanySubscriptions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyMapper {
    public Company toDomain(CompanyEntity entity) {
        String activePlanName = entity.getSubscriptions().stream()
                .filter(CompanySubscriptions::isActive)
                .findFirst()
                .map(it -> it.getSubscriptionPlans().getName())
                .orElse(null);

        return new Company(
                entity.getId(),
                entity.getName(),
                entity.getTaxId(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                activePlanName,
                List.of()
        );
    }
}
