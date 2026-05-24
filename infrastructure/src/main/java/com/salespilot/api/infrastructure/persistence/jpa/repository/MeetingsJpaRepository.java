package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MeetingsJpaRepository extends JpaRepository<MeetingEntity, UUID>, JpaSpecificationExecutor<MeetingEntity> {
    @Query("SELECT AVG(m.durationSeconds) FROM MeetingEntity m WHERE m.durationSeconds IS NOT NULL")
    Optional<Double> findAverageDurationSeconds();

    @Query(value = """
        SELECT
            DATE_TRUNC('month', created_at) AS month,
            COUNT(*) AS total
        FROM meetings
        WHERE created_at BETWEEN :start AND :end
        GROUP BY DATE_TRUNC('month', created_at)
        ORDER BY DATE_TRUNC('month', created_at) ASC
    """, nativeQuery = true)
    List<Object[]> getMeetingsGroupedByMonth(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
