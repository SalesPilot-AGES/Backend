package com.salespilot.api.domain.model;

import java.time.LocalDateTime;

public record AverageMeetingDurationPerMonth(
    LocalDateTime month,
    String monthLabel,
    Double avgMinutes
) {}