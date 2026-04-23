package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.SubscriptionPlan;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionPlanRepository {
    List<SubscriptionPlan> findAll();
    Optional<SubscriptionPlan> findById(UUID id);
    Optional<SubscriptionPlan> findByName(String name);
}
