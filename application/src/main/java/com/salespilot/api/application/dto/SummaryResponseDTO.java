package com.salespilot.api.application.dto;

public record SummaryResponseDTO(
        long totalMeetings,
        double averageDurationSeconds,
        Double successRate
) {}
