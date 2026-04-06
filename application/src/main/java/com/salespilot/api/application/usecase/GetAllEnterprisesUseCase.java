package com.salespilot.api.application.usecase;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.salespilot.api.application.dto.EnterpriseResponseDTO;
import com.salespilot.api.domain.repository.EnterpriseRepository;

public class GetAllEnterprisesUseCase {

    private final EnterpriseRepository repository;

    public GetAllEnterprisesUseCase(EnterpriseRepository repository) {
        this.repository = repository;
    }

    public Page<EnterpriseResponseDTO> execute(String nome, String cnpj, String plano, Boolean isActive, Pageable pageable) {
        return repository.getAllEnterprises(nome, cnpj, plano, isActive, pageable)
        .map(e -> new EnterpriseResponseDTO(
            e.getId(),
            e.getNome(),
            e.getCnpj(),
            e.getPlano(),
            e.isActive(),
            e.getCreatedAt()
        ));
    }
}
