package com.salespilot.api.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salespilot.api.application.dto.SystemStatusResponseDTO;
import com.salespilot.api.application.usecase.GetSystemStatusUseCase;

@RestController
@RequestMapping("/api/v1/system")
public class SystemStatusController {

    private final GetSystemStatusUseCase getSystemStatusUseCase;

    public SystemStatusController(GetSystemStatusUseCase getSystemStatusUseCase) {
        this.getSystemStatusUseCase = getSystemStatusUseCase;
    }

    @GetMapping("/ping")
    public ResponseEntity<SystemStatusResponseDTO> ping() {
        return ResponseEntity.ok(getSystemStatusUseCase.execute());
    }
}
