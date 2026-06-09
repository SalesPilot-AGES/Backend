package com.salespilot.api.application.dto;

import java.util.List;

public record GroupAverageMeetingDurationPerMonthResponseDTO(
    List<AverageMeetingDurationPerMonthResponseDTO> data
) {}
