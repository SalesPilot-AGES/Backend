package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanySubscriptions;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanySubscriptionsJpaRepository extends JpaRepository<CompanySubscriptions, UUID> {
    Optional<CompanySubscriptions> findFirstByCompanyAndActiveTrue(CompanyEntity company);
}
