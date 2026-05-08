package com.salespilot.api.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespilot.api.domain.entity.MeetingPostAnalysis;
import com.salespilot.api.domain.valueobject.PostAnalysisActionItem;
import com.salespilot.api.domain.valueobject.PostAnalysisSentimentAnalysis;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MeetingPostAnalysisResponseDTO(
        UUID id,
        @JsonProperty("meeting_id")
        UUID meetingId,
        String summary,
        @JsonProperty("action_items")
        List<PostAnalysisActionItem> actionItems,
        @JsonProperty("sentiment_analysis")
        PostAnalysisSentimentAnalysis sentimentAnalysis,
        @JsonProperty("created_at")
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
