package com.salespilot.api.domain.entity;

import com.salespilot.api.domain.enums.RealtimeInsightType;
import com.salespilot.api.domain.valueobject.InsightDescription;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class MeetingRealtimeInsight {
    private UUID id;
    private UUID meetingId;
    private String content;
    private RealtimeInsightType type;
    private InsightDescription description;
    private LocalDateTime createdAt;
}
