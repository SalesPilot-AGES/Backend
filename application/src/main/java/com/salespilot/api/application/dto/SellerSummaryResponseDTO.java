package com.salespilot.api.application.dto;

public record SellerSummaryResponseDTO(
        Long totalMeetings,
        Double averageDurationSeconds,
        Integer successRate
) {}
