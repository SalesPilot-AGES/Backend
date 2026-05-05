package com.salespilot.api.application.exception;

import java.util.UUID;

public class MeetingPreAnalysisNotFoundException extends RuntimeException {
    public MeetingPreAnalysisNotFoundException(UUID meetingId) {
        super("Meeting pre analysis not found. Meeting ID: " + meetingId);
    }
}
