package com.salespilot.api.presentation.controller;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.salespilot.api.application.usecase.EditCompanyUseCase;
import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.dto.EditCompanyRequestDTO;

@RestController
@RequestMapping("/api/empresas")
public class CompanyController {

    private final EditCompanyUseCase editCompanyUseCase;

    public CompanyController(EditCompanyUseCase editCompanyUseCase) {
        this.editCompanyUseCase = editCompanyUseCase;
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> update(
            @PathVariable UUID id, 
            @RequestBody EditCompanyRequestDTO request
    ) {
        CompanyResponseDTO response = editCompanyUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }
}