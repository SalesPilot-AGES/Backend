package com.salespilot.api.presentation.controller;
    
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salespilot.api.application.dto.EnterpriseResponseDTO;
import com.salespilot.api.application.usecase.GetAllEnterprisesUseCase;

@RestController
@RequestMapping("/api/empresas")
public class EnterpriseController {

    private final GetAllEnterprisesUseCase getEnterpriseUseCase;

    public EnterpriseController(GetAllEnterprisesUseCase getEnterpriseUseCase) {
        this.getEnterpriseUseCase = getEnterpriseUseCase;
    }

    @GetMapping
    public ResponseEntity<Page<EnterpriseResponseDTO>> getAll(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String cnpj,
        @RequestParam(required = false) String plano,
        @RequestParam(required = false) Boolean isActive,
        Pageable pageable)
        {
        return ResponseEntity.ok(getEnterpriseUseCase.execute(nome, cnpj, plano, isActive, pageable));
    }
}
