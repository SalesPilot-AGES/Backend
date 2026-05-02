package com.salespilot.api.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.salespilot.api.domain.valueobject.PreAnalysisKeyPoints;
import com.salespilot.api.domain.valueobject.PreAnalysisPossibleObjections;
import com.salespilot.api.domain.valueobject.PreAnalysisRecommendedStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingPreAnalysis {
    private UUID id;
    private UUID meetingId;
    private PreAnalysisRecommendedStrategy recommendedStrategy;
    private PreAnalysisKeyPoints keyPoints;
    private PreAnalysisPossibleObjections possibleObjections;
    private LocalDateTime createdAt;
}
