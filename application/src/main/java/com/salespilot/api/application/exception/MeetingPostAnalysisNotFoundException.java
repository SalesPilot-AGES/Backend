package com.salespilot.api.application.exception;

import java.util.UUID;

public class MeetingPostAnalysisNotFoundException extends RuntimeException {
    public MeetingPostAnalysisNotFoundException(UUID meetingId) {
        super("Meeting post analysis not found. Meeting ID: " + meetingId);
    }
}
