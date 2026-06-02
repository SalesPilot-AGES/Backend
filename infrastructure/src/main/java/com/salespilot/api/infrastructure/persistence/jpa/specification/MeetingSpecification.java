package com.salespilot.api.infrastructure.persistence.jpa.specification;

import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

public class MeetingSpecification {

    public static Specification<MeetingEntity> titleLike(String title) {
        return (root, query, cb) -> title == null || title.isBlank() ? null
                : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<MeetingEntity> clientCompanyNameLike(String clientCompanyName) {
        return (root, query, cb) -> clientCompanyName == null || clientCompanyName.isBlank() ? null
                : cb.like(cb.lower(root.join("client").get("clientCompanyName")), "%" + clientCompanyName.toLowerCase() + "%");
    }

    public static Specification<MeetingEntity> collaboratorIdEquals(UUID collaboratorId) {
        return (root, query, cb) -> collaboratorId == null ? null
                : cb.equal(root.get("collaborator").get("id"), collaboratorId);
    }

    public static Specification<MeetingEntity> collaboratorIsActive(UUID collaboratorId) {
        return (root, query, cb) -> collaboratorId == null ? null
                : cb.equal(root.get("collaborator").get("active"), true);
    }

    public static Specification<MeetingEntity> companyIdEquals(UUID companyId) {
        return (root, query, cb) -> companyId == null ? null
                : cb.equal(root.join("collaborator").join("company").get("id"), companyId);
    }

    public static Specification<MeetingEntity> createdAtBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) ->
                cb.between(root.get("createdAt"), start, end);
    }
}