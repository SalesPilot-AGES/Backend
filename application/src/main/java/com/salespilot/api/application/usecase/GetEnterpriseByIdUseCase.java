package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.GetEnterpriseByIdResponseDTO;
import com.salespilot.api.application.exception.EnterpriseNotFoundException;
import com.salespilot.api.domain.entity.Enterprise;
import com.salespilot.api.domain.repository.EnterpriseRepository;

import java.util.UUID;

public class GetEnterpriseByIdUseCase {
    private final EnterpriseRepository enterpriseRepository;

    public GetEnterpriseByIdUseCase(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }

    public GetEnterpriseByIdResponseDTO execute(UUID id) {
        Enterprise enterprise = enterpriseRepository.getEnterpriseById(id);

        if(enterprise == null) {
            throw new EnterpriseNotFoundException(id);
        }

        return new GetEnterpriseByIdResponseDTO(enterprise.getId(), enterprise.getNome(), enterprise.getCnpj(), enterprise.getPlano(), enterprise.isActive(), enterprise.getCreatedAt());
    }
}
