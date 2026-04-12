package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, UUID>, JpaSpecificationExecutor<CompanyEntity>  {

    Page<CompanyEntity> findAll(Specification<CompanyEntity> spec, Pageable pageable);
    
    boolean existsByTaxId(String taxId);

    Optional<CompanyEntity> findByTaxId(String taxId);
}
