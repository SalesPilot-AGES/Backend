package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.SystemStatusResponseDTO;
import com.salespilot.api.domain.entity.SystemStatus;
import com.salespilot.api.domain.repository.SystemStatusRepository;

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
