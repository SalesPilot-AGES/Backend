package com.salespilot.api.application.exception;

import java.util.UUID;

public class CollaboratorNotFoundException extends RuntimeException {
    public CollaboratorNotFoundException(UUID collaboratorId) {
        super("Collaborator not found. ID: " + collaboratorId);
    }
}
