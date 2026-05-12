package com.salespilot.api.application.queryservice;

import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CollaboratorQueryService {
    private final CollaboratorRepository repository;

    public CollaboratorQueryService(CollaboratorRepository repository) {
        this.repository = repository;
    }

    public Collaborator getCollaboratorById(UUID id) {
        return repository.getCollaboratorById(id)
                .orElseThrow(() -> new CollaboratorNotFoundException(id));
    }
}
