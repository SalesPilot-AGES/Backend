package com.salespilot.api.application.exception;

import java.util.UUID;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(UUID clientId) {
        super("Client not found. ID: " + clientId);
    }
}
