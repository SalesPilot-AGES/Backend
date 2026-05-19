package com.salespilot.api.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record LatestMeetingsResponseDTO(
    UUID id,
    String title,
    String status,
    LocalDateTime startedAt,
    Integer durationSeconds,
    ClientResponseDTO client
) {}
