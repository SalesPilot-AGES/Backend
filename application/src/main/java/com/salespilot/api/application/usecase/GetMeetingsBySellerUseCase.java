package com.salespilot.api.application.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.salespilot.api.application.dto.MeetingsBySellerResponseDto;
import com.salespilot.api.application.dto.SellerNameAndTotalMeetingsDto;
import com.salespilot.api.application.utils.DashboardPeriodUtils;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.model.SellerNameAndTotalMeetings;

public class GetMeetingsBySellerUseCase {
    private final CollaboratorRepository collaboratorRepository;

    public GetMeetingsBySellerUseCase(CollaboratorRepository collaboratorRepository){
        this.collaboratorRepository = collaboratorRepository;
    }

    public MeetingsBySellerResponseDto execute(String period, LocalDate start, LocalDate end){
        LocalDateTime[] range = DashboardPeriodUtils.resolve(period, start, end);

        LocalDateTime startDate = range[0];
        LocalDateTime endDate = range[1];

        List<SellerNameAndTotalMeetingsDto> data = collaboratorRepository.getMeetingsBySeller(startDate, endDate).stream().map(this::map).toList();
        return new MeetingsBySellerResponseDto(data);
    }

    private SellerNameAndTotalMeetingsDto map(SellerNameAndTotalMeetings item) {
        return new SellerNameAndTotalMeetingsDto(item.sellerName(), item.total());
    }
}
