package com.salespilot.api.infrastructure.persistence.jpa.specification;

import com.salespilot.api.infrastructure.persistence.jpa.entity.Meetings;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class MeetingSpecification {

    public static Specification<Meetings> collaboratorIdEquals(UUID collaboratorId) {
        return (root, query, cb) -> collaboratorId == null ? null
                : cb.equal(root.get("collaborator").get("id"), collaboratorId);
    }
}