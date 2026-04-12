package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.GetCompanyByIdResponseDTO;
import com.salespilot.api.domain.repository.CompanyRepository;

import java.util.Optional;
import java.util.UUID;

public class GetCompanyByIdUseCase {
    private final CompanyRepository companyRepository;

    public GetCompanyByIdUseCase(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Optional<GetCompanyByIdResponseDTO> execute(UUID id) {
        return companyRepository.getCompanyById(id)
                .map(c -> new GetCompanyByIdResponseDTO(c.getId(),
                        c.getName(),
                        c.getTaxId(),
                        c.getPlan(),
                        c.isActive(),
                        c.getCreatedAt()
                ));
    }
}
