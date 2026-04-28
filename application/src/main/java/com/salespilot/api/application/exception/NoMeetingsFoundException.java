package com.salespilot.api.application.exception;

import java.util.UUID;

public class NoMeetingsFoundException extends RuntimeException {
    public NoMeetingsFoundException() {
        super("No meetings found.");
    }
}
