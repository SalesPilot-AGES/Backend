package com.salespilot.api.presentation.controller;

import java.util.UUID;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
        return editCompanyUseCase.execute(id, request)
          .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}