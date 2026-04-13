package com.salespilot.api.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespilot.api.domain.entity.Company;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetCompanyByIdResponseDTO(@JsonProperty("id") UUID id,
                                        @JsonProperty("name") String name,
                                        @JsonProperty("tax_id") String taxId,
                                        @JsonProperty("status") String status,
                                        @JsonProperty("max_sellers") Integer maxSellers,
                                        @JsonProperty("max_managers") Integer maxManagers,
                                        @JsonProperty("notes") String notes,
                                        @JsonProperty("created_at") LocalDateTime createdAt,
                                        @JsonProperty("updated_at") LocalDateTime updatedAt) {

    public static GetCompanyByIdResponseDTO from(Company company) {
        return new GetCompanyByIdResponseDTO(
                company.getId(),
                company.getName(),
                company.getTaxId(),
                company.getStatus(),
                company.getMaxSellers(),
                company.getMaxManagers(),
                company.getNotes(),
                company.getCreatedAt(),
                company.getUpdatedAt()
        );
    }
}
