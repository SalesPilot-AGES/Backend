package com.salespilot.api.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.salespilot.api.domain.valueobject.PostAnalysisActionItemList;
import com.salespilot.api.domain.valueobject.PostAnalysisSentimentAnalysis;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingPostAnalysis {
    private UUID id;
    private UUID meetingId;
    private String summary;
    private PostAnalysisActionItemList actionItems;
    private PostAnalysisSentimentAnalysis sentimentAnalysis;
    private LocalDateTime createdAt;
}
