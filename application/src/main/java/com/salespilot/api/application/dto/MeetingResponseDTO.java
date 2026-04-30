package com.salespilot.api.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MeetingResponseDTO(
        UUID id,
        String title,
        SellerMeetingResponseDTO seller,
        ClientMeetingResponseDTO client,
        String meetingType,
        LocalDateTime scheduledFor,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Integer durationSeconds,
        String status
) {}
