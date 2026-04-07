package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.GetCompanyByIdResponseDTO;
import com.salespilot.api.application.usecase.GetCompanyByIdUseCase;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final GetCompanyByIdUseCase getCompanyByIdUseCase;

    public CompanyController(GetCompanyByIdUseCase getCompanyByIdUseCase) {
        this.getCompanyByIdUseCase = getCompanyByIdUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetCompanyByIdResponseDTO> getCompanyById(@PathVariable UUID id) {
        return getCompanyByIdUseCase.execute(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
