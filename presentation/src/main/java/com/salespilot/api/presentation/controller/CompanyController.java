package com.salespilot.api.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.salespilot.api.application.dto.CompanyRequestDTO;
import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.usecase.PostCompanyUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final PostCompanyUseCase postCompanyUseCase;

    public CompanyController(PostCompanyUseCase postCompanyUseCase) {
        this.postCompanyUseCase = postCompanyUseCase;
    }
    
    @PostMapping
    public ResponseEntity<CompanyResponseDTO> create(@Valid @RequestBody CompanyRequestDTO request) {
        CompanyResponseDTO response = postCompanyUseCase.create(request.name(), request.taxId(), request.plano(), request.isActive());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
