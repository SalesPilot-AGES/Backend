package com.salespilot.api.application.dto;

import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;

import java.time.LocalDateTime;
import java.util.UUID;

public record SellerWithMeetingsResponseDTO (
    UUID id,
    UUID companyId,
    String name,
    CollaboratorRole role,
    String email,
    boolean active,
    String phone,
    CollaboratorPreferences preferences,
    Long totalMeetings,
    LatestMeetingsResponseDTO latestMeeting,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    CompanyResponseDTO company
) {}
