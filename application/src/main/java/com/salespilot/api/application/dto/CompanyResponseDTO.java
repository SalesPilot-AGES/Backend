package com.salespilot.api.application.dto;

import java.sql.Timestamp;
import java.util.UUID;

public record CompanyResponseDTO(UUID uuid, String name, String taxId, String plano, boolean isActive, Timestamp createdAt) {}
