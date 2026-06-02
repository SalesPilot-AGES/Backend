package com.salespilot.api.application.exception;

import com.salespilot.api.domain.enums.CollaboratorRole;

public class InvalidCollaboratorRoleException extends RuntimeException {
    public InvalidCollaboratorRoleException(CollaboratorRole role, CollaboratorRole requisitionRole) {
        super("Invalid role : " + role + " for the requisition: " + requisitionRole);
    }
}
