package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @Query(value = """
                SELECT COUNT(*)
                FROM (
                    SELECT DISTINCT ON (csh.collaborator_id)
                        csh.collaborator_id,
                        csh.active
                    FROM collaborator_status_history csh
                    JOIN collaborators c ON c.id = csh.collaborator_id
                    WHERE c.company_id = :company_id
                      AND c.role = :role
                      AND csh.created_at <= :period
                    ORDER BY csh.collaborator_id, csh.created_at DESC
                ) latest_status
                WHERE latest_status.active = :active
            """, nativeQuery = true)
    Long countByCompanyIdAndRoleAndActiveSnapshotAt(
            @Param("company_id") UUID companyId,
            @Param("role") String role,
            @Param("active") boolean active,
            @Param("period") LocalDateTime period
    );
}
