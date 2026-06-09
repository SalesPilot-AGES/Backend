package com.salespilot.api.application.exception;

public class RefreshTokenInvalidException extends RuntimeException {
    public RefreshTokenInvalidException() {
        super("Refresh token is invalid or expired");
    }
}

