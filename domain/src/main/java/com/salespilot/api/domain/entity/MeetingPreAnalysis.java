package com.salespilot.api.domain.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.salespilot.api.domain.valueobject.PreAnalysisRecommendedStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingPreAnalysis {
    private UUID id;
    private UUID meetingId;
    private PreAnalysisRecommendedStrategy recommendedStrategy;
    private List<String> keyPoints;
    private List<String> possibleObjections;
    private LocalDateTime createdAt;
}
