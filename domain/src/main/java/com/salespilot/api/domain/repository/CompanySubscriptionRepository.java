package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.CompanySubscription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanySubscriptionRepository {
    Optional<CompanySubscription> findActiveByCompanyId(UUID companyId);
    List<CompanySubscription> findByCompanyId(UUID companyId);
    CompanySubscription save(CompanySubscription subscription);
}
