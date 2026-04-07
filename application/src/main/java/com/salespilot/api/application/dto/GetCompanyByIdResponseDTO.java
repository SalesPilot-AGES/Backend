package com.salespilot.api.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.UUID;

public record GetCompanyByIdResponseDTO(UUID id, String name, @JsonProperty("tax_id") String taxId, String plan, @JsonProperty("is_active") boolean active, @JsonProperty("created_at") Timestamp createdAt) {
}
