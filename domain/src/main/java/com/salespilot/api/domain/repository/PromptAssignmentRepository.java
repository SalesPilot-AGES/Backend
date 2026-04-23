package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.PromptAssignment;

import java.util.List;
import java.util.UUID;

public interface PromptAssignmentRepository {
    List<PromptAssignment> findByCollaboratorId(UUID collaboratorId);
    List<PromptAssignment> findByPromptId(UUID promptId);
    PromptAssignment save(PromptAssignment assignment);
}
