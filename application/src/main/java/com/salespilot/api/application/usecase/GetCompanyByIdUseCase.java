package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.queryservice.CompanyQueryService;

import java.util.UUID;

public class GetCompanyByIdUseCase {
    private final CompanyQueryService companyQueryService;

    public GetCompanyByIdUseCase(CompanyQueryService companyQueryService) {
        this.companyQueryService = companyQueryService;
    }

    public CompanyResponseDTO execute(UUID id) {
        return CompanyResponseDTO.from(companyQueryService.getCompanyById(id));
    }
}
