package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorStatusHistoryEntity;

public interface CollaboratorStatusHistoryJpaRepository extends JpaRepository<CollaboratorStatusHistoryEntity, UUID> {

    @Query(value = """
            SELECT COUNT(*) FROM (
                SELECT DISTINCT ON (collaborator_id) collaborator_id, active
                FROM collaborator_status_history
                WHERE changed_at <= :cutoff
                ORDER BY collaborator_id, changed_at DESC
            ) snapshot
            JOIN collaborators c ON c.id = snapshot.collaborator_id
            WHERE snapshot.active = TRUE AND c.role = :role
            """, nativeQuery = true)
    Long countActiveByRoleSnapshotAt(@Param("role") String role, @Param("cutoff") LocalDateTime cutoff);
}
