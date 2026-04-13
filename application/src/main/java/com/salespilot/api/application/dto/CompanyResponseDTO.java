package com.salespilot.api.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CompanyPlan;

public record CompanyResponseDTO(UUID uuid, String name, String taxId, CompanyPlan plan, boolean active, LocalDateTime createdAt) {
    public static CompanyResponseDTO from(Company company) {
        return new CompanyResponseDTO(
                company.getId(),
                company.getName(),
                company.getTaxId(),
                company.getPlan(),
                company.isActive(),
                company.getCreatedAt()
        );
    }
}
