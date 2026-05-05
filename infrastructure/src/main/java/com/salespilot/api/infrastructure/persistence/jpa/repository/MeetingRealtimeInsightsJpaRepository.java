package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingRealtimeInsightsEntity;

import java.util.List;
import java.util.UUID;


import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRealtimeInsightsJpaRepository extends JpaRepository<MeetingRealtimeInsightsEntity, UUID> {

    List<MeetingRealtimeInsightsEntity> findByMeetingIdOrderByCreatedAtDesc(UUID meetingId);
}
