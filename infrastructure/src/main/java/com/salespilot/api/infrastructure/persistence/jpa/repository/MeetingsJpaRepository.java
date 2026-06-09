package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeetingsJpaRepository extends JpaRepository<MeetingEntity, UUID>, JpaSpecificationExecutor<MeetingEntity> {
    @Query("SELECT AVG(m.durationSeconds) FROM MeetingEntity m WHERE m.durationSeconds IS NOT NULL")
    Optional<Double> findAverageDurationSeconds();
    
    @Query(value = """
                SELECT
                    DATE_TRUNC('month', m.created_at) AS month,
                    COUNT(*) AS total
                FROM meetings m
                JOIN collaborators c ON c.id = m.collaborator_id
                WHERE m.created_at BETWEEN :start AND :end
                  AND (:company_id IS NULL OR c.company_id = :company_id)
                  AND (:collaborator_id IS NULL OR m.collaborator_id = :collaborator_id)
                GROUP BY DATE_TRUNC('month', m.created_at)
                ORDER BY DATE_TRUNC('month', m.created_at) ASC
            """, nativeQuery = true)
    List<Object[]> getMeetingsGroupedByMonth(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("company_id") UUID companyId,
            @Param("collaborator_id") UUID collaboratorId
    );

    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT 
                AVG(m.durationSeconds) 
            FROM MeetingEntity m 
            WHERE m.durationSeconds IS NOT NULL 
                AND m.collaborator.id = :collaborator_id 
                AND m.createdAt BETWEEN :start AND :end""")
    Optional<Double> findAverageDurationSecondsByCollaboratorAndPeriod(
            @Param("collaborator_id") UUID collaboratorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
        SELECT DATE_TRUNC('month', created_at) AS month,
        ROUND(AVG(duration_seconds) / 60.0, 1) AS avg_minutes
        FROM meetings
        WHERE created_at BETWEEN :start AND :end
        GROUP BY month
        ORDER BY month ASC
        """, nativeQuery = true)
    List<Object[]> findAverageDurationPerMonth(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
