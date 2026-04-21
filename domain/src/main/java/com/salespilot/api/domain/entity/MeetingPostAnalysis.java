package com.salespilot.api.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingPostAnalysis {
    private UUID id;
    private UUID meetingId;
    private String summary;
    private String actionItems;
    private String sentimentAnalysis;
    private LocalDateTime createdAt;
}
