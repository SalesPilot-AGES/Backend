package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyStatusHistoryEntity;

public interface CompanyStatusHistoryJpaRepository extends JpaRepository<CompanyStatusHistoryEntity, UUID> {

    @Query(value = """
            SELECT COUNT(*) FROM (
                SELECT DISTINCT ON (company_id) active
                FROM company_status_history
                WHERE changed_at <= :cutoff
                ORDER BY company_id, changed_at DESC
            ) snapshot
            WHERE snapshot.active = :active
            """, nativeQuery = true)
    Long countActiveSnapshotAt(@Param("active") boolean active, @Param("cutoff") LocalDateTime cutoff);
}
