package com.salespilot.api.presentation.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salespilot.api.application.dto.TopFiveCompanyByMeetingTotalResponseDto;
import com.salespilot.api.application.usecase.GetTopFiveCompaniesByMeetingTotalUseCase;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Dashboard")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final GetTopFiveCompaniesByMeetingTotalUseCase getTopFiveCompaniesByMeetingTotalUseCase;

    public DashboardController(GetTopFiveCompaniesByMeetingTotalUseCase getTopFiveCompaniesByMeetingTotalUseCase){
        this.getTopFiveCompaniesByMeetingTotalUseCase = getTopFiveCompaniesByMeetingTotalUseCase;
    }

    @GetMapping("/meetings-by-company")
    public ResponseEntity<TopFiveCompanyByMeetingTotalResponseDto> getMeetingsGroupedByMonth(
        @RequestParam(required = false) String period,
        @RequestParam(required = false) LocalDate start_date,
        @RequestParam(required = false) LocalDate end_date) {
        return ResponseEntity.ok(getTopFiveCompaniesByMeetingTotalUseCase.execute(period, start_date, end_date));
    }
    
}
