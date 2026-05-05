package com.salespilot.api.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MeetingContextMetadataResponseDTO(
        UUID id,
        String title,
        String status,
        String meetingType,
        String objective,
        String clientNeeds,
        String previousInteractions,
        String competitorsInvolved,
        LocalDateTime scheduledFor,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Integer durationSeconds,
        SellerMeetingResponseDTO seller,
        ClientMeetingResponseDTO client,
        MeetingPreAnalysisResponseDTO preAnalysis,
        LocalDateTime createdAt
) {
}
