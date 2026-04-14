package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.domain.repository.CompanyRepository;

import java.util.Optional;
import java.util.UUID;

public class GetCompanyByIdUseCase {
    private final CompanyRepository companyRepository;

    public GetCompanyByIdUseCase(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Optional<CompanyResponseDTO> execute(UUID id) {
        return companyRepository.getCompanyById(id).map(CompanyResponseDTO::from);
    }
}
