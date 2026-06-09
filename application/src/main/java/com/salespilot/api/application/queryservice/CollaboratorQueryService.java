package com.salespilot.api.application.queryservice;

import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CollaboratorQueryService {
    private final CollaboratorRepository collaboratorRepository;

    public CollaboratorQueryService(CollaboratorRepository collaboratorRepository) {
        this.collaboratorRepository = collaboratorRepository;
    }

    public Collaborator getOrThrowById(UUID id) {
        return collaboratorRepository.getCollaboratorById(id)
                .orElseThrow(() -> new CollaboratorNotFoundException(id));
    }
}
