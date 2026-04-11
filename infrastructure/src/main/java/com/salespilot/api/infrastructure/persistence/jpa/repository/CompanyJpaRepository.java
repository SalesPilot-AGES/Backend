package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, UUID> {

    boolean existsByTaxId(String taxId);

    Optional<CompanyEntity> findByTaxId(String taxId);

    @Query("""
    SELECT c
    FROM CompanyEntity c
    LEFT JOIN FETCH c.collaborators
    WHERE c.id = :id
""")
    Optional<CompanyEntity> findByIdWithCollaborators(@Param("id") UUID id);
}
