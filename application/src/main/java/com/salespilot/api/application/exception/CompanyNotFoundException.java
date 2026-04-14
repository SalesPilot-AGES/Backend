package com.salespilot.api.application.exception;

import java.util.UUID;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(UUID companyId) {
        super("Company not found. ID: " + companyId);
    }
}
