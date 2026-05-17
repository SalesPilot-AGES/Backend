package com.salespilot.api.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salespilot.api.application.dto.GroupCardMetricsResponseDTO;
import com.salespilot.api.application.usecase.GetCardMetricsUseCase;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@Tag(name = "Dashboard")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final GetCardMetricsUseCase getCardMetricsUseCase;

    public DashboardController(GetCardMetricsUseCase getCardMetricsUseCase){
        this.getCardMetricsUseCase = getCardMetricsUseCase;
    }

    @GetMapping("/metrics")
    public ResponseEntity<GroupCardMetricsResponseDTO> getCardMetrics(
            @RequestParam String period,
            @RequestParam(name = "start_date", required = false) LocalDate startDate,
            @RequestParam(name = "end_date", required = false) LocalDate endDate) {
        return ResponseEntity.ok(getCardMetricsUseCase.execute(period, startDate, endDate));
    }
    
}