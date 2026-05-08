package com.salespilot.api.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanySubscription {
    private UUID id;
    private UUID companyId;
    private SubscriptionPlan plan;
    private boolean active;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private LocalDate renewalDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
