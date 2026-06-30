package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CollaboratorJpaRepository extends JpaRepository<CollaboratorEntity, UUID>, JpaSpecificationExecutor<CollaboratorEntity> {
    boolean existsByCompanyIdAndEmail(UUID companyId, String email);
    Optional<CollaboratorEntity> findByEmail(String email);

    @Query("""
    SELECT c.active, COUNT(c)
    FROM CollaboratorEntity c
    WHERE c.role = com.salespilot.api.domain.enums.CollaboratorRole.SELLER
    GROUP BY c.active
""")
    List<Object[]> countSellersGroupedByStatus();

    @Query(value = """
        SELECT
            col.name AS seller_name,
            COUNT(m.id) AS total
        FROM collaborators col
        LEFT JOIN meetings m
            ON m.collaborator_id = col.id
            AND m.created_at BETWEEN :start AND :end
        WHERE col.role = 'SELLER'
        GROUP BY col.id, col.name
        ORDER BY total DESC, col.name ASC
    """, nativeQuery = true)
    List<Object[]> getMeetingsBySeller(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
