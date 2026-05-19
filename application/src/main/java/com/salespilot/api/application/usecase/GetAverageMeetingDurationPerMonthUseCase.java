package com.salespilot.api.application.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.salespilot.api.application.dto.GroupAverageMeetingDurationPerMonthResponseDTO;
import com.salespilot.api.application.utils.DashboardPeriodUtils;
import com.salespilot.api.domain.repository.MeetingRepository;

public class GetAverageMeetingDurationPerMonthUseCase {
    private final MeetingRepository meetingRepository;

    public GetAverageMeetingDurationPerMonthUseCase(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public GroupAverageMeetingDurationPerMonthResponseDTO execute(String period, LocalDate startDate, LocalDate endDate) {
        LocalDateTime[] dates = DashboardPeriodUtils.dashboardPeriodUtilsToAverageMeetingDurationPerMonth(period, startDate, endDate);

        LocalDateTime start = dates[0];
        LocalDateTime end = dates[1];
    }
}
