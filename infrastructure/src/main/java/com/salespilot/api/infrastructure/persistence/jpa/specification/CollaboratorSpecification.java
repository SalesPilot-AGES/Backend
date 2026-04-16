package com.salespilot.api.infrastructure.persistence.jpa.specification;

import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CollaboratorSpecification {

    public static Specification<CollaboratorEntity> roleEquals(CollaboratorRole role) {
        return (root, query, cb) -> role == null ? null
                : cb.equal(root.get("role"), role);
    }

    public static Specification<CollaboratorEntity> nameLike(String name) {
        return (root, query, cb) -> name == null || name.isBlank() ? null
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<CollaboratorEntity> emailEquals(String email) {
        return (root, query, cb) -> email == null || email.isBlank() ? null
                : cb.equal(cb.lower(root.get("email")), email.toLowerCase());
    }

    public static Specification<CollaboratorEntity> companyIdEquals(UUID companyId) {
        return (root, query, cb) -> companyId == null ? null
                : cb.equal(root.get("company").get("id"), companyId);
    }

    public static Specification<CollaboratorEntity> isActiveEquals(Boolean active) {
        return (root, query, cb) -> active == null ? null
                : cb.equal(root.get("active"), active);
    }
}
