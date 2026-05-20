package com.salespilot.api.application.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.salespilot.api.application.dto.AverageMeetingDurationPerMonthResponseDTO;
import com.salespilot.api.application.dto.GroupAverageMeetingDurationPerMonthResponseDTO;
import com.salespilot.api.application.utils.DashboardPeriodUtils;
import com.salespilot.api.domain.repository.MeetingRepository;
import com.salespilot.api.domain.model.AverageMeetingDurationPerMonth;

public class GetAverageMeetingDurationPerMonthUseCase {
    private final MeetingRepository meetingRepository;

    public GetAverageMeetingDurationPerMonthUseCase(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public GroupAverageMeetingDurationPerMonthResponseDTO execute(String period, LocalDate startDate, LocalDate endDate) {
        LocalDateTime[] dates = DashboardPeriodUtils.dashboardPeriodUtilsToAverageMeetingDurationPerMonth(period, startDate, endDate);

        LocalDateTime start = dates[0];
        LocalDateTime end = dates[1];

        List<AverageMeetingDurationPerMonth> averageMeetingsDurationPerMonth = meetingRepository.groupAverageMeetingDurationPerMonth(start, end);

        if(averageMeetingsDurationPerMonth == null) {
            return null;
        }

        List<AverageMeetingDurationPerMonthResponseDTO> response = averageMeetingsDurationPerMonth
                .stream()
                .map(m -> new AverageMeetingDurationPerMonthResponseDTO(m.month(), m.monthLabel(), m.avgMinutes()))
                .toList();
        return new GroupAverageMeetingDurationPerMonthResponseDTO(response);
    }
}
