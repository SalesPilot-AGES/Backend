package com.salespilot.api.domain.entity;

import com.salespilot.api.domain.valueobject.PostAnalysisActionItem;
import com.salespilot.api.domain.valueobject.PostAnalysisSentimentAnalysis;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class MeetingPostAnalysis {
    private UUID id;
    private UUID meetingId;
    private String summary;
    private List<PostAnalysisActionItem> actionItems;
    private PostAnalysisSentimentAnalysis sentimentAnalysis;
    private LocalDateTime createdAt;
}
