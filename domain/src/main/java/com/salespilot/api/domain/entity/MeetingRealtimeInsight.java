package com.salespilot.api.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingRealtimeInsight {
    private UUID id;
    private UUID meetingId;
    private String content;
    private String type;
    private String description;
    private LocalDateTime createdAt;
}
