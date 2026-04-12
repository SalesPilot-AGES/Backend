package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorEntity;

public interface CollaboratorJpaRepository extends JpaRepository<CollaboratorEntity, UUID> {

    boolean existsByCompanyIdAndEmail(UUID companyId, String email);
}

