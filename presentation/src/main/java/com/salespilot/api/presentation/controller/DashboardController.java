package com.salespilot.api.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salespilot.api.application.dto.MeetingsGroupedByMonthResponseDTO;
import com.salespilot.api.application.usecase.GetTotalMeetingsGroupedByMonthUseCase;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@Tag(name = "Dashboard")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final GetTotalMeetingsGroupedByMonthUseCase getTotalMeetingsGroupedByMonth;

    public DashboardController(GetTotalMeetingsGroupedByMonthUseCase getTotalMeetingsGroupedByMonth){
        this.getTotalMeetingsGroupedByMonth = getTotalMeetingsGroupedByMonth;
    }

    @GetMapping("/meetings-by-month")
    public ResponseEntity<MeetingsGroupedByMonthResponseDTO> getMeetingsGroupedByMonth(
        @RequestParam(required = false) String period,
        @RequestParam(required = false) LocalDate start_date,
        @RequestParam(required = false) LocalDate end_date) {
        return ResponseEntity.ok(getTotalMeetingsGroupedByMonth.execute(period, start_date, end_date));
    }
    
}