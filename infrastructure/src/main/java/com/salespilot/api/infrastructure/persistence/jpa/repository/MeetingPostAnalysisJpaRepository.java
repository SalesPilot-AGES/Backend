package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingPostAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MeetingPostAnalysisJpaRepository extends JpaRepository<MeetingPostAnalysisEntity, UUID> {
    @Query("SELECT mpa FROM MeetingPostAnalysisEntity mpa WHERE mpa.meeting.id = :meetingId")
    Optional<MeetingPostAnalysisEntity> findByMeetingId(@Param("meetingId") UUID meetingId);
}
