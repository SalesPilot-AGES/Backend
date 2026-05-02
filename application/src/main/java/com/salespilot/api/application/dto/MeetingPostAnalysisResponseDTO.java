package com.salespilot.api.application.dto;

import com.salespilot.api.domain.entity.MeetingPostAnalysis;

import java.time.LocalDateTime;
import java.util.UUID;

public record MeetingPostAnalysisResponseDTO(
        UUID id,
        UUID meetingId,
        String summary,
        String actionItems,
        String sentimentAnalysis,
        LocalDateTime createdAt
) {
    public static MeetingPostAnalysisResponseDTO from(MeetingPostAnalysis meetingPostAnalysis) {
        return new MeetingPostAnalysisResponseDTO(
                meetingPostAnalysis.getId(),
                meetingPostAnalysis.getMeetingId(),
                meetingPostAnalysis.getSummary(),
                meetingPostAnalysis.getActionItems(),
                meetingPostAnalysis.getSentimentAnalysis(),
                meetingPostAnalysis.getCreatedAt()
        );
    }
}
