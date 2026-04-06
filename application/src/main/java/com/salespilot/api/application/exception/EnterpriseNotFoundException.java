package com.salespilot.api.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EnterpriseNotFoundException extends RuntimeException {
    public EnterpriseNotFoundException(UUID enterpriseId) {
        super("Enterprise not found: " + enterpriseId);
    }
}
