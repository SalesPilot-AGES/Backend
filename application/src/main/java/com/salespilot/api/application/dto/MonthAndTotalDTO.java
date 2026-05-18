package com.salespilot.api.application.dto;

import java.time.LocalDateTime;

public record MonthAndTotalDTO(
    LocalDateTime month,
    String monthLabel,
    Long total
) {
} 
