package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.queryservice.CompanyQueryService;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;

import java.util.Optional;
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
