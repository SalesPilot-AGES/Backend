package com.salespilot.api.application.queryservice;

import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompanyQueryService {
    private final CompanyRepository companyRepository;

    public CompanyQueryService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company getOrThrowCompanyById(UUID id) {
        return companyRepository.getCompanyById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));
    }
}
