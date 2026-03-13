package com.salespilot.api.infrastructure.persistence.jpa.repositories;

import java.time.Instant;

import org.springframework.stereotype.Repository;

import com.salespilot.api.domain.entities.SystemStatus;
import com.salespilot.api.domain.repositories.SystemStatusRepository;

@Repository
public class SystemStatusRepositoryImpl implements SystemStatusRepository {
    
    @Override
    public SystemStatus getCurrentStatus() {
        // just a mock implementation for demonstration purposes
        return new SystemStatus("ONLINE - Architecture Flow Demo", Instant.now().toString());
    }
}