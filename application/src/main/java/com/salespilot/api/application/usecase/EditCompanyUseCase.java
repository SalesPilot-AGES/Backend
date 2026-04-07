package com.salespilot.api.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.dto.EditCompanyRequestDTO;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;

@Service
public class EditCompanyUseCase {

    private final CompanyRepository repository;

    public EditCompanyUseCase(CompanyRepository repository) {
        this.repository = repository;
    }

    public CompanyResponseDTO execute(UUID id, EditCompanyRequestDTO data) {
    Company company = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

    company.updateInfo(data.name()/*, data.phone(), data.address(), data.plans(), data.isActive()*/);

    Company updatedCompany = repository.save(company);

        return new CompanyResponseDTO(
                updatedCompany.getId(),
                updatedCompany.getCnpj(),
                // updatedCompany.getCreatedAt(),
                updatedCompany.getName() //,
                // updatedCompany.getPhone(),
                // updatedCompany.getAddress(),
                // updatedCompany.getPlans(),
                // updatedCompany.isActive()
        );
    }
}