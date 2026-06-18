package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, UUID>, JpaSpecificationExecutor<CompanyEntity>  {
    Page<CompanyEntity> findAll(Specification<CompanyEntity> spec, Pageable pageable);

    boolean existsByTaxId(String taxId);

    Optional<CompanyEntity> findByTaxId(String taxId);


    @Query(value = """
        SELECT 
            c.name AS company_name,
            COUNT(m.id) AS total
        FROM meetings m
        JOIN collaborators col 
            ON m.collaborator_id = col.id
        JOIN companies c 
            ON col.company_id = c.id
        WHERE m.created_at BETWEEN :start AND :end
        GROUP BY c.id, c.name
        ORDER BY total DESC
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getTopFiveCompaniesByMeetingTotal(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


    @Query("""
        SELECT c.active, COUNT(c)
        FROM CompanyEntity c
        GROUP BY c.active
    """)
    List<Object[]> countCompaniesGroupedByStatus();

    @Query(value = """
        SELECT 
            c.id AS company_id,
            COUNT(DISTINCT col.id) AS total_collaborators,
            COUNT(DISTINCT CASE WHEN col.role = 'MANAGER' THEN col.id END) AS total_managers,
            COUNT(DISTINCT m.id) AS total_meetings
        FROM companies c
        LEFT JOIN collaborators col ON col.company_id = c.id
        LEFT JOIN meetings m ON m.collaborator_id = col.id
        WHERE c.id IN :ids
        GROUP BY c.id
    """, nativeQuery = true)
    List<Object[]> getStatsByCompanyIds(@Param("ids") List<UUID> ids);
}
