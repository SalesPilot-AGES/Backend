package com.salespilot.api.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.UUID;

public record GetEnterpriseByIdResponseDTO(@JsonProperty("uuid") UUID id, String nome, String cnpj, String plano, @JsonProperty("is_active") boolean isActive, @JsonProperty("created_at") Timestamp createdAt) {
}
