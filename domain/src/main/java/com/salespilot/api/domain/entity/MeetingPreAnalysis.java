package com.salespilot.api.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingPreAnalysis {
    private UUID id;
    private UUID meetingId;
    private String recommendedStrategy;
    private String keyPoints;
    private String possibleObjections;
    private LocalDateTime createdAt;
}
