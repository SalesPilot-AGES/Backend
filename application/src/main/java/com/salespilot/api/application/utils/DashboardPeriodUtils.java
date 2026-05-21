package com.salespilot.api.application.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.salespilot.api.application.exception.InvalidPeriodException;

public class DashboardPeriodUtils {
    public static LocalDateTime[] resolve(String period, LocalDate startDate, LocalDate endDate) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start;

        switch (period) {
            case "all" ->
                start = LocalDateTime.of(1970, 1, 1, 0, 0);
            case "30d" ->
                start = end.minusDays(30);
            case "90d" ->
                start = end.minusDays(90);
            case "custom" -> {
                if (startDate == null || endDate == null) {
                    throw new InvalidPeriodException(startDate, endDate);
                }
                start = startDate.atStartOfDay();
                end = endDate.atTime(23, 59, 59);
            }
            default -> throw new InvalidPeriodException(startDate, endDate);
        }

        return new LocalDateTime[] { start, end };
    }
}
