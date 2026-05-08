package com.salespilot.api.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.salespilot.api.domain.entity.Company;

public record CompanyResponseDTO(
        UUID id,
        String name,
        String taxId,
        String phone,
        String address,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String plan
) {
    public static CompanyResponseDTO from(Company company) {
        return new CompanyResponseDTO(
                company.getId(),
                company.getName(),
                company.getTaxId(),
                company.getPhone(),
                company.getAddress(),
                company.isActive(),
                company.getCreatedAt(),
                company.getUpdatedAt(),
                company.getPlan()
        );
    }
}
