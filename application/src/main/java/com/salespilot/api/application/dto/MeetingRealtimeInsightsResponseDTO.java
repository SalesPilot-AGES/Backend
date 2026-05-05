package com.salespilot.api.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.salespilot.api.domain.entity.MeetingRealtimeInsight;
import com.salespilot.api.domain.enums.RealtimeInsightType;

public record MeetingRealtimeInsightsResponseDTO(
    UUID id,
    RealtimeInsightType type,
    String description,
    String content,
    LocalDateTime createdAt
) {
    public static MeetingRealtimeInsightsResponseDTO from(MeetingRealtimeInsight meetingRealtimeInsights){
        return new MeetingRealtimeInsightsResponseDTO(
            meetingRealtimeInsights.getId(),
            meetingRealtimeInsights.getType(),
            meetingRealtimeInsights.getDescription(),
            meetingRealtimeInsights.getContent(),
            meetingRealtimeInsights.getCreatedAt()
        );
    }
}
