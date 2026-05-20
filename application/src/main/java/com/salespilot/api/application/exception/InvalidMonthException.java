package com.salespilot.api.application.exception;

public class InvalidMonthException extends RuntimeException {
    public InvalidMonthException() {
        super("Month invalid");
    }
}
