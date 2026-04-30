package com.salespilot.api.infrastructure.persistence.jpa.specification;

import com.salespilot.api.infrastructure.persistence.jpa.entity.Meetings;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class MeetingSpecification {

    public static Specification<Meetings> titleLike(String title) {
        return (root, query, cb) -> title == null || title.isBlank() ? null
                : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Meetings> clientCompanyNameLike(String clientCompanyName) {
        return (root, query, cb) -> clientCompanyName == null || clientCompanyName.isBlank() ? null
                : cb.like(cb.lower(root.join("clients").get("clientCompanyName")), "%" + clientCompanyName.toLowerCase() + "%");
    }

    public static Specification<Meetings> collaboratorIdEquals(UUID collaboratorId) {
        return (root, query, cb) -> collaboratorId == null ? null
                : cb.equal(root.get("collaborator").get("id"), collaboratorId);
    }

    public static Specification<Meetings> collaboratorIsActive(UUID collaboratorId) {
        return (root, query, cb) -> collaboratorId == null ? null
                : cb.equal(root.get("collaborator").get("active"), true);
    }
}