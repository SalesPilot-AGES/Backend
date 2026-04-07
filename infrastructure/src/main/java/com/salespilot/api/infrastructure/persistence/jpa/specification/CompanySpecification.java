package com.salespilot.api.infrastructure.persistence.jpa.specification;

import org.springframework.data.jpa.domain.Specification;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;

public class CompanySpecification {

    public static Specification<CompanyEntity> nameLike(String name){
        return (root, query, cb) -> name == null || name.isBlank() ? null
        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<CompanyEntity> taxIdEquals(String taxId){
        return (root, query, cb) -> taxId == null || taxId.isBlank() ? null
        : cb.equal(root.get("taxId"), taxId);
    }

    public static Specification<CompanyEntity> planoEquals(String plano){
        return (root, query, cb) -> plano == null || plano.isBlank() ? null
        : cb.equal(root.get("plano"), plano);
    }

    public static Specification<CompanyEntity> isActiveEquals(Boolean active){
        return (root, query, cb) -> active == null ? null
        : cb.equal(root.get("active"), active);
    }
}
