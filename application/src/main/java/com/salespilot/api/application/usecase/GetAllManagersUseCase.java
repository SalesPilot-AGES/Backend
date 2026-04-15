package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class GetAllManagersUseCase {

    private final CollaboratorRepository repository;

    public GetAllManagersUseCase(CollaboratorRepository repository) {
        this.repository = repository;
    }

    public Page<CollaboratorResponseDTO> execute(String name, String email, UUID companyId, Boolean active, Pageable pageable) {
        return repository.getManagers(name, email, companyId, active, pageable)
                .map(CollaboratorResponseDTO::from);
    }
}
