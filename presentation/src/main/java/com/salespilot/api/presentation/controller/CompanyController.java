package com.salespilot.api.presentation.controller;
    
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.usecase.GetAllCompaniesUseCase;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final GetAllCompaniesUseCase getCompanyUseCase;

    public CompanyController(GetAllCompaniesUseCase getCompanyUseCase) {
        this.getCompanyUseCase = getCompanyUseCase;
    }

    @GetMapping
    public ResponseEntity<Page<CompanyResponseDTO>> getAll(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String taxId,
        @RequestParam(required = false) String plano,
        @RequestParam(required = false) boolean active,
        Pageable pageable)
        {
        return ResponseEntity.ok(getCompanyUseCase.execute(name, taxId, plano, active, pageable));
    }
}
