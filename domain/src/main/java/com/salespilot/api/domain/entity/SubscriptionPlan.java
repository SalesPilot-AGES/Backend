package com.salespilot.api.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubscriptionPlan {
    private UUID id;
    private String name;
    private String description;
    private int priceCents;
    private String status;
    private LocalDateTime createdAt;
}
