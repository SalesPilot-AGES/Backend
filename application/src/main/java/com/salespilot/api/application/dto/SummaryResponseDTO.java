package com.salespilot.api.application.dto;

public record SummaryResponseDTO(
        Long totalMeetings,
        Double averageDurationSeconds
        //falta success rate
) {}
