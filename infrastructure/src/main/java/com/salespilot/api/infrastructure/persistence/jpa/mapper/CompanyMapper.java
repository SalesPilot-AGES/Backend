package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanySubscriptions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyMapper {
    private static final Long DEFAULT_UNSPECIFIED_LONG = 0L;

    public Company toDomain(CompanyEntity entity) {
        String plan = resolvePlan(entity);

        return new Company(
                entity.getId(),
                entity.getName(),
                entity.getTaxId(),
                entity.getPhone(),
                entity.getAddress(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                plan,
                List.of(),
                DEFAULT_UNSPECIFIED_LONG,
                DEFAULT_UNSPECIFIED_LONG,
                DEFAULT_UNSPECIFIED_LONG
        );
    }

    public String resolvePlan(CompanyEntity entity) {
        return entity.getSubscriptions().stream()
                .filter(CompanySubscriptions::isActive)
                .findFirst()
                .map(it -> it.getSubscriptionPlans().getName())
                .orElse(null);
    }
}
