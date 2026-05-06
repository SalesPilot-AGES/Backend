package com.salespilot.api.application.dto;

import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;

import java.time.LocalDateTime;
import java.util.UUID;

public record SellerResponseDTO(
        UUID id,
        UUID companyId,
        String name,
        CollaboratorRole role,
        String email,
        String phone,
        boolean active,
        Integer averageFeeling,
        Long totalMeetings,
        CollaboratorPreferences preferences,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        CompanyResponseDTO company
) {}