package com.salespilot.api.application.dto;

public record GroupCardMetricsResponseDTO(
    CardMetricsResponseDTO activeCompanies,
    CardMetricsResponseDTO inactiveCompanies,
    CardMetricsResponseDTO totalMeetings,
    CardMetricsResponseDTO activeSellers
) {}
