package com.salespilot.api.infrastructure.persistence.jpa.specification;

import org.springframework.data.jpa.domain.Specification;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;

public class CompanySpecification {

    public static Specification<CompanyEntity> nomeLike(String nome){
        return (root, query, cb) -> nome == null || nome.isBlank() ? null
        : cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<CompanyEntity> cnpjEquals(String cnpj){
        return (root, query, cb) -> cnpj == null || cnpj.isBlank() ? null
        : cb.equal(root.get("cnpj"), cnpj);
    }

    public static Specification<CompanyEntity> planoEquals(String plano){
        return (root, query, cb) -> plano == null || plano.isBlank() ? null
        : cb.equal(root.get("plano"), plano);
    }

    public static Specification<CompanyEntity> isActiveEquals(Boolean isActive){
        return (root, query, cb) -> isActive == null ? null
        : cb.equal(root.get("isActive"), isActive);
    }
}
