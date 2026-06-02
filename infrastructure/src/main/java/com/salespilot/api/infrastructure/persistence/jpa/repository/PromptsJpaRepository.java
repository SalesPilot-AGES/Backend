package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.Prompt;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptsJpaRepository extends JpaRepository<Prompt, UUID> {
}
