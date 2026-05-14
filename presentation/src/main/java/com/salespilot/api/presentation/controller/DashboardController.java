package com.salespilot.api.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salespilot.api.application.dto.GroupCompanyCountResponseDTO;
import com.salespilot.api.application.usecase.GetGroupedCompaniesCountUseCase;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@Tag(name = "Dashboard")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final GetGroupedCompaniesCountUseCase getGroupedCompaniesCountUseCase;

    public DashboardController(GetGroupedCompaniesCountUseCase getGroupedCompaniesCountUseCase){
        this.getGroupedCompaniesCountUseCase = getGroupedCompaniesCountUseCase;
    }

    @GetMapping("/companies-status")
    public ResponseEntity<GroupCompanyCountResponseDTO> getGroupedCompaniesCount() {
        return ResponseEntity.ok(getGroupedCompaniesCountUseCase.execute());
    }
    
}
