package com.salespilot.api.application.exception;

import java.util.UUID;

public class CollaboratorAlreadyExistsException extends RuntimeException {
    public CollaboratorAlreadyExistsException(UUID companyId, String email) {
        super("Collaborator with company id: " + companyId + " already exists with email: " + email);
    }
}
