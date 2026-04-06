package com.salespilot.api.application.usecase;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.domain.repository.CompanyRepository;

public class GetAllCompaniesUseCase {

    private final CompanyRepository repository;

    public GetAllCompaniesUseCase(CompanyRepository repository) {
        this.repository = repository;
    }

    public Page<CompanyResponseDTO> execute(String nome, String cnpj, String plano, Boolean isActive, Pageable pageable) {
        return repository.getAllCompanies(nome, cnpj, plano, isActive, pageable)
        .map(e -> new CompanyResponseDTO(
            e.getId(),
            e.getNome(),
            e.getCnpj(),
            e.getPlano(),
            e.isActive(),
            e.getCreatedAt()
        ));
    }
}
