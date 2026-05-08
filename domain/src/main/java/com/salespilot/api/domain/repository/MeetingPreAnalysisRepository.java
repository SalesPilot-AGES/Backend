package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.MeetingPreAnalysis;

import java.util.Optional;
import java.util.UUID;

public interface MeetingPreAnalysisRepository {
    Optional<MeetingPreAnalysis> findByMeetingId(UUID meetingId);
}
