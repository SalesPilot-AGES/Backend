package com.salespilot.api.application.dto;

import com.salespilot.api.domain.entity.MeetingPostAnalysis;
import com.salespilot.api.domain.valueobject.PostAnalysisActionItem;
import com.salespilot.api.domain.valueobject.PostAnalysisSentimentAnalysis;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MeetingPostAnalysisResponseDTO(
        UUID id,
        UUID meetingId,
        String summary,
        List<PostAnalysisActionItem> actionItems,
        PostAnalysisSentimentAnalysis sentimentAnalysis,
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
