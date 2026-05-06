package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.MeetingRealtimeInsight;

import java.util.List;
import java.util.UUID;

public interface MeetingRealtimeInsightRepository {
    List<MeetingRealtimeInsight> findByMeetingId(UUID meetingId);
}
