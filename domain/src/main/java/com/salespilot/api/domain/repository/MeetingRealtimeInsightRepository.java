package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.MeetingRealtimeInsight;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetingRealtimeInsightRepository {
    Page<MeetingRealtimeInsight> findByMeetingId(UUID meetingId, Pageable pageable);
}
