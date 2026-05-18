package com.salespilot.api.application.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import com.salespilot.api.application.dto.MeetingsGroupedByMonthResponseDTO;
import com.salespilot.api.application.dto.MonthAndTotalDTO;
import com.salespilot.api.application.utils.DashboardPeriodUtils;
import com.salespilot.api.domain.repository.MeetingRepository;

public class GetTotalMeetingsGroupedByMonthUseCase {
    private final MeetingRepository meetingRepository;

    public GetTotalMeetingsGroupedByMonthUseCase(MeetingRepository meetingRepository){
        this.meetingRepository = meetingRepository;
    }

    public MeetingsGroupedByMonthResponseDTO execute(String period, LocalDate start, LocalDate end){
        LocalDateTime[] range =DashboardPeriodUtils.resolve(period, start, end);

        LocalDateTime startDate = range[0];
        LocalDateTime endDate = range[1];

        List<MonthAndTotalDTO> data = meetingRepository.getMeetingsGroupedByMonth(startDate, endDate).stream().map(this::map).toList();
        return new MeetingsGroupedByMonthResponseDTO(data);
    }

    private MonthAndTotalDTO map(Object[] item) {
        LocalDateTime month = (LocalDateTime) item[0];
        Long total = ((Number) item[1]).longValue();

        String monthLabel = month.getMonth().getDisplayName(TextStyle.SHORT, Locale.of("pt", "BR")).replace(".", "");

        return new MonthAndTotalDTO(month, capitalize(monthLabel),total);
    }

    private String capitalize(String monthLabel) {
        return monthLabel.substring(0, 1).toUpperCase() + monthLabel.substring(1);
    }
}
