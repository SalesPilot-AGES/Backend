package com.salespilot.api.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CollaboratorRole;

public record EditCollaboratorDTO(UUID uuid, UUID companyId, String name, CollaboratorRole role, String email, boolean active, String preferences, LocalDateTime createdAt, Company company) {}
