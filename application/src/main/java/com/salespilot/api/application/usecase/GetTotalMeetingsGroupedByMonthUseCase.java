package com.salespilot.api.application.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.salespilot.api.application.dto.MeetingsGroupedByMonthResponseDTO;
import com.salespilot.api.application.dto.MonthAndTotalDTO;
import com.salespilot.api.application.utils.DashboardPeriodUtils;
import com.salespilot.api.domain.model.MonthAndTotal;
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

    private MonthAndTotalDTO map(MonthAndTotal item) {
        return new MonthAndTotalDTO(item.month(), item.monthLabel(), item.total());
    }
}
