package com.salespilot.api.domain.model;

import java.time.LocalDate;

public record MonthAndTotal(
    LocalDate month,
    String monthLabel,
    Long total
) {
} 