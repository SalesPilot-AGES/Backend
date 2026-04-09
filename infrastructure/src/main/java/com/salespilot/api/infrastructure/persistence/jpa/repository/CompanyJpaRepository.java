package com.salespilot.api.infrastructure.persistence.jpa.repository;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, UUID> {

} 
