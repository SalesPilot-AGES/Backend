package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;


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
        GROUP BY c.name
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
}
