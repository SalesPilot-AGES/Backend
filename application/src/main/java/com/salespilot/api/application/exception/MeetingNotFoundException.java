package com.salespilot.api.application.exception;

import java.util.UUID;

public class MeetingNotFoundException extends RuntimeException{
    public MeetingNotFoundException(UUID meetingId) {
        super("Meeting not found. ID: " + meetingId);
    }
}
