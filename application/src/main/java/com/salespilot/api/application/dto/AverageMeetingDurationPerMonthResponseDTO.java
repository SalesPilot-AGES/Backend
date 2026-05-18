package com.salespilot.api.application.dto;

import java.time.LocalDateTime;

public record AverageMeetingDurationPerMonthResponseDTO(
  LocalDateTime month,
  String monthLabel,
  Double avgMinutes
) {}
