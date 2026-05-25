package com.salespilot.api.application.dto;

public record SellerGroupCardMetricsResponseDTO(
    CardMetricsResponseDTO totalMeetings,
    CardMetricsResponseDTO averageDuration
) {}
