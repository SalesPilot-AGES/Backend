package com.salespilot.api.application.exception;

public class InvalidPeriodException extends RuntimeException {
    public InvalidPeriodException(String period) {
        super("period: " + period + " invalid");
    }
}