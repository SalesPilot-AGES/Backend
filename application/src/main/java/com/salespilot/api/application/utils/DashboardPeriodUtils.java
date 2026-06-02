package com.salespilot.api.application.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import com.salespilot.api.application.exception.InvalidPeriodException;

public class DashboardPeriodUtils {
    private static final String DATE_LABEL_CUSTOM = "custom";
    private static final String DATE_LABEL_SEVEN_DAYS = "7d";
    private static final String DATE_LABEL_THIRTY_DAYS = "30d";
    private static final String DATE_LABEL_MONTH = "month";
    private static final String DATE_LABEL_ALL = "all";
    private static final String DATE_LABEL_NINETY_DAYS = "90d";
    
    private DashboardPeriodUtils() {}

    public static LocalDateTime[] dashboardPeriodUtilsToCardMetrics(String period, LocalDate startDate, LocalDate endDate) {
        LocalDateTime previousEnd;
        LocalDateTime previousStart;
        LocalDateTime currentStart;
        LocalDateTime currentEnd;
        LocalDate today = LocalDate.now();
        LocalDate previousMonth = today.minusMonths(1);

        switch (period) {
            case DATE_LABEL_SEVEN_DAYS:
                currentStart = today
                    .minusDays(6)
                    .atStartOfDay();
                currentEnd = today.atTime(LocalTime.MAX);
                previousStart = currentStart.minusDays(7);
                previousEnd = currentEnd.minusDays(7);
                break;
            case DATE_LABEL_THIRTY_DAYS:      
                currentStart = today
                    .minusDays(29)
                    .atStartOfDay();
                currentEnd = today.atTime(LocalTime.MAX);
                previousStart = currentStart.minusDays(30);
                previousEnd = currentEnd.minusDays(30);
                break;
            case DATE_LABEL_MONTH:
                currentStart = today
                    .withDayOfMonth(1)
                    .atStartOfDay();
                currentEnd = today.atTime(LocalTime.MAX);  
                previousStart = previousMonth
                        .withDayOfMonth(1)
                        .atStartOfDay();
                int previousDay =
                        Math.min(
                                today.getDayOfMonth(),
                                previousMonth.lengthOfMonth()
                        );
                previousEnd = previousMonth
                        .withDayOfMonth(previousDay)
                        .atTime(LocalTime.MAX);

                break;
            case DATE_LABEL_CUSTOM:
                if (startDate == null || endDate == null) {
                    throw new InvalidPeriodException(startDate, endDate);
                }

                if (startDate.isAfter(endDate)) {
                    throw new InvalidPeriodException(startDate, endDate);
                }

                currentStart = startDate.atStartOfDay();
                currentEnd = endDate.atTime(LocalTime.MAX);

                long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

                previousStart = currentStart.minusDays(days);
                previousEnd = currentEnd.minusDays(days);
                break;
            default:
                throw new InvalidPeriodException(startDate, endDate);
        }

        return new LocalDateTime[] { previousStart, previousEnd, currentStart, currentEnd };
    }

    public static LocalDateTime[] resolve(String period, LocalDate startDate, LocalDate endDate) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start;

        switch (period) {
            case DATE_LABEL_ALL ->
                start = LocalDateTime.of(1970, 1, 1, 0, 0);
            case DATE_LABEL_THIRTY_DAYS ->
                start = end.minusDays(30);
            case DATE_LABEL_NINETY_DAYS ->
                start = end.minusDays(90);
            case DATE_LABEL_CUSTOM -> {
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

    public static LocalDateTime[] dashboardPeriodUtilsToAverageMeetingDurationPerMonth(String period, LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        LocalDateTime end = today.atTime(LocalTime.MAX);
        LocalDateTime start;

        switch (period) {
            case DATE_LABEL_SEVEN_DAYS ->
                start = today.minusDays(7).atStartOfDay();
            case DATE_LABEL_THIRTY_DAYS ->
                start = today.minusDays(30).atStartOfDay();
            case DATE_LABEL_NINETY_DAYS ->
                start = today.minusDays(90).atStartOfDay();
            case DATE_LABEL_CUSTOM -> {
                if (startDate == null || endDate == null) {
                    throw new InvalidPeriodException(startDate, endDate);
                }
                if(endDate.isBefore(startDate)) {
                    throw new InvalidPeriodException(startDate, endDate);
                }
                start = startDate.atStartOfDay();
                end = endDate.atTime(LocalTime.MAX);
            }
            default -> throw new InvalidPeriodException(startDate, endDate);
        }

        return new LocalDateTime[] { start, end };
    }
}
