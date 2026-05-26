package com.salespilot.api.application.dto;

public sealed interface GroupCardMetricsResponse
        permits AdminGroupCardMetricsResponseDTO,
        ManagerGroupCardMetricsResponseDTO,
        SellerGroupCardMetricsResponseDTO {
}
