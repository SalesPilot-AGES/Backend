package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.GetEnterpriseByIdResponseDTO;
import com.salespilot.api.application.usecase.GetEnterpriseByIdUseCase;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empresas")
public class EnterpriseController {

    private final GetEnterpriseByIdUseCase getEnterpriseByIdUseCase;

    public EnterpriseController(GetEnterpriseByIdUseCase getEnterpriseByIdUseCase) {
        this.getEnterpriseByIdUseCase = getEnterpriseByIdUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetEnterpriseByIdResponseDTO> getEnterpriseById(@PathVariable UUID id) {
        return ResponseEntity.ok(getEnterpriseByIdUseCase.execute(id));
    }
}
