package com.salespilot.api.application.dto;

import java.util.List;

public record MeetingsGroupedByMonthResponseDTO(
    List<MonthAndTotalDTO> data
) {
} 