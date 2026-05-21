package com.salespilot.api.application.dto;

public record CardMetricsResponseDTO(
    Long value,
    Double variationPercent,
    String trend
) {}
