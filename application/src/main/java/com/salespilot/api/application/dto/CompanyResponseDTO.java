package com.salespilot.api.application.dto;

import com.salespilot.api.domain.entity.Company;

import java.time.LocalDateTime;
import java.util.UUID;

public record CompanyResponseDTO(
        UUID id,
        String name,
        String taxId,
        String phone,
        String address,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String plan,
        Long totalMeetings,
        Long totalCollaborators,
        Long totalManagers
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
                company.getPlan(),
                company.getTotalMeetings(),
                company.getTotalCollaborators(),
                company.getTotalManagers()
        );
    }
}
