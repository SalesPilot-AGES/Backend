package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingPostAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface MeetingPostAnalysisJpaRepository extends JpaRepository<MeetingPostAnalysisEntity, UUID> {
    @Query("SELECT mpa FROM MeetingPostAnalysisEntity mpa WHERE mpa.meeting.id = :meetingId")
    Optional<MeetingPostAnalysisEntity> findByMeetingId(@Param("meetingId") UUID meetingId);

    @Query(value = "SELECT AVG(CAST(sentiment_analysis->>'score' AS DOUBLE PRECISION)) FROM meeting_post_analysis WHERE sentiment_analysis IS NOT NULL", nativeQuery = true)
    Optional<Double> findAverageSuccessRate();

    @Query(value = """
            SELECT AVG(CAST(mpa.sentiment_analysis->>'score' AS DOUBLE PRECISION))
            FROM meeting_post_analysis mpa
            JOIN meetings m ON m.id = mpa.meeting_id
            WHERE m.collaborator_id = :collaboratorId
            AND mpa.sentiment_analysis IS NOT NULL
            """, nativeQuery = true)
    Optional<Double> findAverageScoreByCollaboratorId(@Param("collaboratorId") UUID collaboratorId);

    @Query(value = """
            SELECT AVG(CAST(mpa.sentiment_analysis->>'score' AS DOUBLE PRECISION))
            FROM meeting_post_analysis mpa
            JOIN meetings m ON m.id = mpa.meeting_id
            WHERE m.collaborator_id = :collaboratorId
            AND m.created_at BETWEEN :start AND :end
            AND mpa.sentiment_analysis IS NOT NULL
            """, nativeQuery = true)
    Optional<Double> findAverageScoreByCollaboratorIdAndPeriod(@Param("collaboratorId") UUID collaboratorId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
