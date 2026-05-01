package com.salespilot.api.application.usecase;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class GetAllManagersUseCase {

    private final CollaboratorRepository repository;
    private final CollaboratorAssembler assembler;

    public GetAllManagersUseCase(CollaboratorRepository repository, CollaboratorAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    public Page<CollaboratorResponseDTO> execute(String name, String email, UUID companyId, Boolean active, Pageable pageable) {
        return repository.getManagers(name, email, companyId, active, pageable)
                .map(assembler::toDTO);
    }
}
