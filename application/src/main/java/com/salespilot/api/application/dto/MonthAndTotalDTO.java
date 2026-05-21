package com.salespilot.api.application.dto;

import java.time.LocalDate;

public record MonthAndTotalDTO(
    LocalDate month,
    String monthLabel,
    Long total
) {
} 
