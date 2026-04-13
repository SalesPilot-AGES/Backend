package com.salespilot.api.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CompanyPlan;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetCompanyByIdResponseDTO(@JsonProperty("id") UUID id,
                                        @JsonProperty("name") String name,
                                        @JsonProperty("tax_id") String taxId,
                                        @JsonProperty("plan") CompanyPlan plan,
                                        @JsonProperty("is_active") boolean active,
                                        @JsonProperty("created_at") LocalDateTime createdAt) {

    public static GetCompanyByIdResponseDTO from(Company company) {
        return new GetCompanyByIdResponseDTO(
                company.getId(),
                company.getName(),
                company.getTaxId(),
                company.getPlan(),
                company.isActive(),
                company.getCreatedAt()
        );
    }
}
