package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingPreAnalysisEntity;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingPreAnalysisJpaRepository extends JpaRepository<MeetingPreAnalysisEntity, UUID> {
    Optional<MeetingPreAnalysisEntity> findByMeetingId(UUID meetingId);
}
