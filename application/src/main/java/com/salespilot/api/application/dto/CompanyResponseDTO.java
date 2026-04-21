package com.salespilot.api.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.salespilot.api.domain.entity.Company;

public record CompanyResponseDTO(
        UUID id,
        String name,
        String taxId,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String activePlan
) {
    public static CompanyResponseDTO from(Company company) {
        return new CompanyResponseDTO(
                company.getId(),
                company.getName(),
                company.getTaxId(),
                company.isActive(),
                company.getCreatedAt(),
                company.getUpdatedAt(),
                company.getActivePlanName()
        );
    }
}
