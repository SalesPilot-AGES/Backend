package com.salespilot.api.application.usecase;

import java.time.LocalDate;

import com.salespilot.api.application.dto.GroupAverageMeetingDurationPerMonthResponseDTO;
import com.salespilot.api.domain.repository.MeetingRepository;

public class GetAverageMeetingDurationPerMonthUseCase {
    private final MeetingRepository meetingRepository;

    public GetAverageMeetingDurationPerMonthUseCase(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public GroupAverageMeetingDurationPerMonthResponseDTO execute(String period, LocalDate startDate, LocalDate endTime) {

    }
}
