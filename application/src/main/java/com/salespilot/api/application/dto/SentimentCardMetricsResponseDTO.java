package com.salespilot.api.application.dto;

public record SentimentCardMetricsResponseDTO(
    Double value,
    Double variationPercent,
    String trend
) {}
