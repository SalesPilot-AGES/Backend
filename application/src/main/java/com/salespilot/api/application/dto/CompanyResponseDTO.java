package com.salespilot.api.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CompanyResponseDTO(
        @JsonProperty("id") UUID id,
        @JsonProperty("name") String name,
        @JsonProperty("tax_id") String taxId,
        @JsonProperty("status") String status,
        @JsonProperty("max_sellers") Integer maxSellers,
        @JsonProperty("max_managers") Integer maxManagers,
        @JsonProperty("created_at") OffsetDateTime createdAt,
        @JsonProperty("updated_at") OffsetDateTime updatedAt
) {}
