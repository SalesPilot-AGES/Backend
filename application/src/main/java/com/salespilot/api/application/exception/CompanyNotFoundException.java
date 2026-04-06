package com.salespilot.api.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(UUID companyId) {
        super("Company not found: " + companyId);
    }
}
