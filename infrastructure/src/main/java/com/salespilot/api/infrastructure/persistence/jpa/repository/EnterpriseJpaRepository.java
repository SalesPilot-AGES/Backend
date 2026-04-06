package com.salespilot.api.infrastructure.persistence.jpa.repository;
import com.salespilot.api.infrastructure.persistence.jpa.entity.EnterpriseEntity;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterpriseJpaRepository extends JpaRepository<EnterpriseEntity, UUID> {

}