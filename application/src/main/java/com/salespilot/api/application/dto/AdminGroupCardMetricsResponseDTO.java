package com.salespilot.api.application.dto;

public record AdminGroupCardMetricsResponseDTO(
    CardMetricsResponseDTO activeCompanies,
    CardMetricsResponseDTO inactiveCompanies,
    CardMetricsResponseDTO totalMeetings,
    CardMetricsResponseDTO activeSellers
) {}
