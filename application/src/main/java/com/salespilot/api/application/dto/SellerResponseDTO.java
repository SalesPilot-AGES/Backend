package com.salespilot.api.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;

public record SellerResponseDTO(
    UUID id,
    UUID companyId,
    String name,
    CollaboratorRole role,
    String email,
    boolean active,
    String phone,
    CollaboratorPreferences preferences,
    Integer averageFeeling,
    Integer totalMeetings,
    List<LatestMeetingsResponseDTO> latestMeetings,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    CompanyResponseDTO company
) {}
