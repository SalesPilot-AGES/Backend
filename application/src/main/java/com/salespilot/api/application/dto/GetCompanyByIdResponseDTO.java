package com.salespilot.api.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.enums.CompanyPlan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GetCompanyByIdResponseDTO(@JsonProperty("id") UUID id,
                                        @JsonProperty("name") String name,
                                        @JsonProperty("tax_id") String taxId,
                                        @JsonProperty("plan") CompanyPlan plan,
                                        @JsonProperty("is_active") boolean active,
                                        @JsonProperty("created_at") LocalDateTime createdAt,
                                        @JsonProperty("collaborators") List<Collaborator> collaborators) {
}
