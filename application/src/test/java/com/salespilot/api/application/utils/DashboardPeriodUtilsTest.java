package com.salespilot.api.application.utils;

import com.salespilot.api.application.exception.InvalidPeriodException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DashboardPeriodUtilsTest {

    @Test
    void cardMetrics_7d_currentStartIs6DaysAgo() {
        LocalDate today = LocalDate.now();
        LocalDateTime[] result = DashboardPeriodUtils.dashboardPeriodUtilsToCardMetrics("7d", null, null);
        assertEquals(today.minusDays(6).atStartOfDay(), result[2]);
    }

    @Test
    void cardMetrics_7d_previousStartIs13DaysAgo() {
        LocalDate today = LocalDate.now();
        LocalDateTime[] result = DashboardPeriodUtils.dashboardPeriodUtilsToCardMetrics("7d", null, null);
        assertEquals(today.minusDays(13).atStartOfDay(), result[0]);
    }

    @Test
    void cardMetrics_30d_currentStartIs29DaysAgo() {
        LocalDate today = LocalDate.now();
        LocalDateTime[] result = DashboardPeriodUtils.dashboardPeriodUtilsToCardMetrics("30d", null, null);
        assertEquals(today.minusDays(29).atStartOfDay(), result[2]);
    }

    @Test
    void cardMetrics_month_currentStartIsFirstOfMonth() {
        LocalDate today = LocalDate.now();
        LocalDateTime[] result = DashboardPeriodUtils.dashboardPeriodUtilsToCardMetrics("month", null, null);
        assertEquals(today.withDayOfMonth(1).atStartOfDay(), result[2]);
    }

    @Test
    void cardMetrics_month_previousStartIsFirstOfPreviousMonth() {
        LocalDate today = LocalDate.now();
        LocalDateTime[] result = DashboardPeriodUtils.dashboardPeriodUtilsToCardMetrics("month", null, null);
        assertEquals(today.minusMonths(1).withDayOfMonth(1).atStartOfDay(), result[0]);
    }

    @Test
    void cardMetrics_custom_validDates_currentStartMatchesStartDate() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(5);
        LocalDate endDate = today;
        LocalDateTime[] result = DashboardPeriodUtils.dashboardPeriodUtilsToCardMetrics("custom", startDate, endDate);
        assertEquals(startDate.atStartOfDay(), result[2]);
    }

    @Test
    void cardMetrics_custom_validDates_previousRangeSpansSameDuration() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(5);
        LocalDate endDate = today;
        LocalDateTime[] result = DashboardPeriodUtils.dashboardPeriodUtilsToCardMetrics("custom", startDate, endDate);
        long currentDays = ChronoUnit.DAYS.between(result[2], result[3]);
        long previousDays = ChronoUnit.DAYS.between(result[0], result[1]);
        assertEquals(currentDays, previousDays);
    }

    @Test
    void cardMetrics_custom_nullStart_throwsInvalidPeriod() {
        LocalDate endDate = LocalDate.now();
        assertThrows(InvalidPeriodException.class,
                () -> DashboardPeriodUtils.dashboardPeriodUtilsToCardMetrics("custom", null, endDate));
    }

    @Test
    void cardMetrics_custom_nullEnd_throwsInvalidPeriod() {
        LocalDate startDate = LocalDate.now();
        assertThrows(InvalidPeriodException.class,
                () -> DashboardPeriodUtils.dashboardPeriodUtilsToCardMetrics("custom", startDate, null));
    }

    @Test
    void cardMetrics_custom_startAfterEnd_throwsInvalidPeriod() {
        LocalDate today = LocalDate.now();
        assertThrows(InvalidPeriodException.class,
                () -> DashboardPeriodUtils.dashboardPeriodUtilsToCardMetrics("custom", today, today.minusDays(1)));
    }

    @Test
    void cardMetrics_unknownPeriod_throwsInvalidPeriod() {
        assertThrows(InvalidPeriodException.class,
                () -> DashboardPeriodUtils.dashboardPeriodUtilsToCardMetrics("xyz", null, null));
    }

    @Test
    void resolve_all_startIs1970() {
        LocalDateTime[] result = DashboardPeriodUtils.resolve("all", null, null);
        assertEquals(LocalDateTime.of(1970, 1, 1, 0, 0), result[0]);
    }

    @Test
    void resolve_30d_startIs30DaysAgo() {
        LocalDateTime expected = LocalDateTime.now().minusDays(30);
        LocalDateTime[] result = DashboardPeriodUtils.resolve("30d", null, null);
        assertTrue(result[0].isAfter(expected.minusSeconds(1)));
        assertTrue(result[0].isBefore(expected.plusSeconds(1)));
    }

    @Test
    void resolve_90d_startIs90DaysAgo() {
        LocalDateTime expected = LocalDateTime.now().minusDays(90);
        LocalDateTime[] result = DashboardPeriodUtils.resolve("90d", null, null);
        assertTrue(result[0].isAfter(expected.minusSeconds(1)));
        assertTrue(result[0].isBefore(expected.plusSeconds(1)));
    }

    @Test
    void resolve_custom_validDates() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(3);
        LocalDate endDate = today;
        LocalDateTime[] result = DashboardPeriodUtils.resolve("custom", startDate, endDate);
        assertEquals(startDate.atStartOfDay(), result[0]);
        assertEquals(endDate.atTime(23, 59, 59), result[1]);
    }

    @Test
    void resolve_custom_nullDates_throwsInvalidPeriod() {
        assertThrows(InvalidPeriodException.class,
                () -> DashboardPeriodUtils.resolve("custom", null, LocalDate.now()));
    }

    @Test
    void resolve_unknownPeriod_throwsInvalidPeriod() {
        assertThrows(InvalidPeriodException.class,
                () -> DashboardPeriodUtils.resolve("abc", null, null));
    }

    @Test
    void avgDuration_7d_startIs7DaysAgo() {
        LocalDate today = LocalDate.now();
        LocalDateTime[] result = DashboardPeriodUtils.dashboardPeriodUtilsToAverageMeetingDurationPerMonth("7d", null, null);
        assertEquals(today.minusDays(7).atStartOfDay(), result[0]);
    }

    @Test
    void avgDuration_30d_startIs30DaysAgo() {
        LocalDate today = LocalDate.now();
        LocalDateTime[] result = DashboardPeriodUtils.dashboardPeriodUtilsToAverageMeetingDurationPerMonth("30d", null, null);
        assertEquals(today.minusDays(30).atStartOfDay(), result[0]);
    }

    @Test
    void avgDuration_90d_startIs90DaysAgo() {
        LocalDate today = LocalDate.now();
        LocalDateTime[] result = DashboardPeriodUtils.dashboardPeriodUtilsToAverageMeetingDurationPerMonth("90d", null, null);
        assertEquals(today.minusDays(90).atStartOfDay(), result[0]);
    }

    @Test
    void avgDuration_custom_validDates() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(10);
        LocalDate endDate = today;
        LocalDateTime[] result = DashboardPeriodUtils.dashboardPeriodUtilsToAverageMeetingDurationPerMonth("custom", startDate, endDate);
        assertEquals(startDate.atStartOfDay(), result[0]);
        assertEquals(endDate.atTime(LocalTime.MAX), result[1]);
    }

    @Test
    void avgDuration_custom_endBeforeStart_throwsInvalidPeriod() {
        LocalDate today = LocalDate.now();
        assertThrows(InvalidPeriodException.class,
                () -> DashboardPeriodUtils.dashboardPeriodUtilsToAverageMeetingDurationPerMonth("custom", today, today.minusDays(1)));
    }

    @Test
    void avgDuration_custom_nullDates_throwsInvalidPeriod() {
        assertThrows(InvalidPeriodException.class,
                () -> DashboardPeriodUtils.dashboardPeriodUtilsToAverageMeetingDurationPerMonth("custom", null, LocalDate.now()));
    }

    @Test
    void avgDuration_unknownPeriod_throwsInvalidPeriod() {
        assertThrows(InvalidPeriodException.class,
                () -> DashboardPeriodUtils.dashboardPeriodUtilsToAverageMeetingDurationPerMonth("abc", null, null));
    }
}
