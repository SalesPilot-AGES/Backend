package com.salespilot.api.application.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("Access denied");
    }
}
