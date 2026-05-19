package com.salespilot.api.application.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.salespilot.api.application.dto.CompanyNameAndTotalMeetingsDto;
import com.salespilot.api.application.dto.TopFiveCompanyByMeetingTotalResponseDto;
import com.salespilot.api.application.utils.DashboardPeriodUtils;
import com.salespilot.api.domain.repository.CompanyRepository;

public class GetTopFiveCompaniesByMeetingTotalUseCase {
    private final CompanyRepository companyRepository;

    public GetTopFiveCompaniesByMeetingTotalUseCase(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    public TopFiveCompanyByMeetingTotalResponseDto execute(String period, LocalDate start, LocalDate end){
        LocalDateTime[] range =DashboardPeriodUtils.resolve(period, start, end);

        LocalDateTime startDate = range[0]; 
        LocalDateTime endDate = range[1];

        List<CompanyNameAndTotalMeetingsDto> data = companyRepository.getTopFiveCompaniesByMeetingTotal(startDate, endDate).stream().map(this::map).toList();
        return new TopFiveCompanyByMeetingTotalResponseDto(data);
    }

    private CompanyNameAndTotalMeetingsDto map(Object[] item) {
        String name = item[0].toString();
        Long total = (Long) item[1];

        return new CompanyNameAndTotalMeetingsDto(name, total);
    }
}
