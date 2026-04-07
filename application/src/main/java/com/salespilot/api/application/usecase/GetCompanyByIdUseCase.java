package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.GetCompanyByIdResponseDTO;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;

import java.util.UUID;

public class GetCompanyByIdUseCase {
    private final CompanyRepository companyRepository;

    public GetCompanyByIdUseCase(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public GetCompanyByIdResponseDTO execute(UUID id) {
        Company company = companyRepository.getCompanyById(id);

        if(company == null) {
            throw new CompanyNotFoundException(id);
        }

        return new GetCompanyByIdResponseDTO(company.getId(), company.getName(), company.getTaxId(), company.getPlan(), company.isActive(), company.getCreatedAt());
    }
}
