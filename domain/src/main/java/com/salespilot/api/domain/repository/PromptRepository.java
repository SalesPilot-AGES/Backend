package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Prompt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PromptRepository {
    Optional<Prompt> findById(UUID id);
    List<Prompt> findByCompanyId(UUID companyId);
    Prompt save(Prompt prompt);
}
