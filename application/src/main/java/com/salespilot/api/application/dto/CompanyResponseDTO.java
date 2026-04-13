package com.salespilot.api.application.dto;

import java.time.Instant;
import java.util.UUID;

import com.salespilot.api.domain.enums.CompanyPlan;

public record CompanyResponseDTO(UUID uuid, String name, String taxId, CompanyPlan plan, boolean active, Instant createdAt) {}
