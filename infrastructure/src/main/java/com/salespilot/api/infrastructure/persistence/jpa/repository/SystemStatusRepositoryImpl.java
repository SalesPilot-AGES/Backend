package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.time.Instant;

import org.springframework.stereotype.Repository;

import com.salespilot.api.domain.entity.SystemStatus;
import com.salespilot.api.domain.repository.SystemStatusRepository;

@Repository
public class SystemStatusRepositoryImpl implements SystemStatusRepository {

    @Override
    public SystemStatus getCurrentStatus() {
        // just a mock implementation for demonstration purposes
        return new SystemStatus("ONLINE - Architecture Flow Demo", Instant.now().toString());
    }
}
