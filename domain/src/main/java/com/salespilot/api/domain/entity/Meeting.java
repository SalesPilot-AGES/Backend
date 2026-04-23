package com.salespilot.api.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Meeting {
    private UUID id;
    private UUID collaboratorId;
    private UUID clientId;
    private String title;
    private String status;
    private Integer durationSeconds;
    private String objective;
    private String meetingType;
    private String clientNeeds;
    private String previousInteractions;
    private String competitorsInvolved;
    private LocalDateTime scheduledFor;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime createdAt;
}
