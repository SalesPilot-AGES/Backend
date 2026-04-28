package com.salespilot.api.application.exception;

public class NoMeetingsFoundException extends RuntimeException {
    public NoMeetingsFoundException() {
        super("No meetings found.");
    }
}
