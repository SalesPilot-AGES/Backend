package com.salespilot.api.application.usecase;

import java.util.Optional;
import java.util.UUID;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.dto.EditCompanyRequestDTO;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;

public class EditCompanyUseCase {

    private final CompanyRepository repository;

    public EditCompanyUseCase(CompanyRepository repository) {
        this.repository = repository;
    }

    public Optional<CompanyResponseDTO> execute(UUID id, EditCompanyRequestDTO data) {
    return repository.findById(id)
      .map(company -> {
        company.updateInfo(data.name(), data.plan(), data.active());

        Company updatedCompany = repository.save(company);

          return new CompanyResponseDTO(
                  updatedCompany.getId(),
                  updatedCompany.getTaxId(),
                  updatedCompany.getCreatedAt(),
                  updatedCompany.getName(),
                  updatedCompany.getPlan(),
                  updatedCompany.isActive()
          );
      });
    }
}