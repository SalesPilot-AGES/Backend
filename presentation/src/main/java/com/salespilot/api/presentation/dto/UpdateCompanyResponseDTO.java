package com.salespilot.api.presentation.dto;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.domain.enums.CompanyPlan;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateCompanyResponseDTO(
        UUID id,
        String name,
        String taxId,
        CompanyPlan plan,
        boolean active,
        LocalDateTime createdAt
) {
    public static UpdateCompanyResponseDTO from(CompanyResponseDTO company) {
        return new UpdateCompanyResponseDTO(
                company.id(),
                company.name(),
                company.taxId(),
                company.plan(),
                company.active(),
                company.createdAt()
        );
    }
}

