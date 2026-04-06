package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.PromptAssignments;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptAssignmentsJpaRepository extends JpaRepository<PromptAssignments, UUID> {
}
