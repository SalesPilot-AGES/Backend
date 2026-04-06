package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.AuditLogs;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogsJpaRepository extends JpaRepository<AuditLogs, UUID> {
}
