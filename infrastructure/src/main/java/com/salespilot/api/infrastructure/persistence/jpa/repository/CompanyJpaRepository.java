package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, UUID> {

    boolean existsByTaxId(String taxId);
}