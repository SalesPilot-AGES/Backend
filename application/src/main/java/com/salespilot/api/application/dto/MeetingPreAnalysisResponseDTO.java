package com.salespilot.api.application.dto;

import com.salespilot.api.domain.entity.MeetingPreAnalysis;
import com.salespilot.api.domain.valueobject.PreAnalysisRecommendedStrategy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MeetingPreAnalysisResponseDTO(
        UUID id,
        PreAnalysisRecommendedStrategy recommendedStrategy,
        List<String> keyPoints,
        List<String> possibleObjections,
        LocalDateTime createdAt
) {
    public static MeetingPreAnalysisResponseDTO from (MeetingPreAnalysis meetingPreAnalysis) {
        return new MeetingPreAnalysisResponseDTO(
                meetingPreAnalysis.getId(),
                meetingPreAnalysis.getRecommendedStrategy(),
                meetingPreAnalysis.getKeyPoints(),
                meetingPreAnalysis.getPossibleObjections(),
                meetingPreAnalysis.getCreatedAt()
        );
    }
}
