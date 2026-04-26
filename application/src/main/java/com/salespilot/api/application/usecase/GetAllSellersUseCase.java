package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class GetAllSellersUseCase {

    private final CollaboratorRepository repository;
    private final CompanyRepository companyRepository;

    public GetAllSellersUseCase(CollaboratorRepository repository, CompanyRepository companyRepository) {
        this.repository = repository;
        this.companyRepository = companyRepository;
    }

    public Page<CollaboratorResponseDTO> execute(String name, String email, UUID companyId, Boolean active, Pageable pageable) {
        return repository.getSellers(name, email, companyId, active, pageable)
                .map(c -> new CollaboratorResponseDTO(
                        c.getId(),
                        c.getCompanyId(),
                        c.getName(),
                        c.getRole(),
                        c.getEmail(),
                        c.getPhone(),
                        c.isActive(),
                        c.getAverageFeeling(),
                        c.getPreferences(),
                        c.getUpdatedAt(),
                        c.getCreatedAt(),
                        companyRepository.getCompanyById(c.getCompanyId())
                                .map(CompanyResponseDTO::from)
                                .orElseThrow(() -> new CompanyNotFoundException(companyId))
                ));
    }
}
