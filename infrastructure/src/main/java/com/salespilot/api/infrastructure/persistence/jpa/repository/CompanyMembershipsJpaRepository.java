package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyMemberships;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyMembershipsJpaRepository extends JpaRepository<CompanyMemberships, UUID> {
}
