package com.salespilot.api.application.dto;

import java.util.List;

public record GroupCompanyCountResponseDTO(
    List<DonutChartDataResponseDTO> data,
    Long total
) {
}
