package com.salespilot.api.infrastructure.persistence.jpa.repository;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, UUID>, JpaSpecificationExecutor<CompanyEntity> {
    
}
