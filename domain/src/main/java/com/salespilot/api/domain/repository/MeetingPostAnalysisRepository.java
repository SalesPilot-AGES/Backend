package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.MeetingPostAnalysis;

import java.util.Optional;
import java.util.UUID;

public interface MeetingPostAnalysisRepository {
    Optional<MeetingPostAnalysis> findByMeetingId(UUID meetingId);
}
