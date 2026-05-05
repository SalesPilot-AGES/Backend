package com.salespilot.api.infrastructure.persistence.jpa.specification;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanySubscriptions;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;

import java.util.UUID;

public class CompanySpecification {

    public static Specification<CompanyEntity> nameLike(String name) {
        return (root, query, cb) -> name == null || name.isBlank() ? null
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<CompanyEntity> taxIdEquals(String taxId) {
        return (root, query, cb) -> taxId == null || taxId.isBlank() ? null
                : cb.equal(root.get("taxId"), taxId);
    }

    public static Specification<CompanyEntity> planEquals(String plan) {
        return (root, query, cb) -> {
            if (plan == null || plan.isBlank()) return null;
            Subquery<UUID> subquery = query.subquery(UUID.class);
            var sub = subquery.from(CompanySubscriptions.class);
            subquery.select(sub.get("company").get("id"))
                    .where(
                            cb.isTrue(sub.get("active")),
                            cb.equal(cb.lower(sub.get("subscriptionPlans").get("name")), plan.toLowerCase())
                    );
            return root.get("id").in(subquery);
        };
    }

    public static Specification<CompanyEntity> isActiveEquals(Boolean active) {
        return (root, query, cb) -> active == null ? null
                : cb.equal(root.get("active"), active);
    }
}
