package com.salespilot.api.application.useCases;

import com.salespilot.api.application.dtos.SystemStatusResponseDTO;
import com.salespilot.api.domain.entities.SystemStatus;
import com.salespilot.api.domain.repositories.SystemStatusRepository;

public class GetSystemStatusUseCase {

    private final SystemStatusRepository repository;

    public GetSystemStatusUseCase(SystemStatusRepository repository) {
        this.repository = repository;
    }

    public SystemStatusResponseDTO execute() {
        SystemStatus status = repository.getCurrentStatus();
        return new SystemStatusResponseDTO(status.getStatus(), status.getTimestamp());
    }
}