package com.salespilot.api.application.usecase;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.domain.repository.CompanyRepository;

public class GetAllCompaniesUseCase {

    private final CompanyRepository repository;

    public GetAllCompaniesUseCase(CompanyRepository repository) {
        this.repository = repository;
    }

    public Page<CompanyResponseDTO> execute(String name, String taxId, Boolean active, Pageable pageable) {
        return repository.getAllCompanies(name, taxId, active, pageable)
                .map(CompanyResponseDTO::from);
    }
}
