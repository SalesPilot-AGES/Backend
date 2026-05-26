package com.salespilot.api.application.dto;

public record ManagerGroupCardMetricsResponseDTO(
    CardMetricsResponseDTO activeSellers,
    CardMetricsResponseDTO inactiveSellers,
    CardMetricsResponseDTO totalMeetings
) implements GroupCardMetricsResponse {}
