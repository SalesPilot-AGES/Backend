package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface MeetingRepository {
    Long getTotalMeetingsByCollaborator(UUID collaboratorId);
    Page<Meeting> getAllMeetings(String title, String clientCompanyName, UUID collaboratorID, Pageable pageable);
    long getTotalMeetings();
    double getAverageDurationSeconds();
    Optional<Meeting> getMeetingById(UUID id);
    boolean existsById(UUID id);
    Optional<Meeting> getLatestMeetingByCollaborator(UUID CollaboratorId);
    Long countTotalMeetingsByPeriod(LocalDateTime currentStart, LocalDateTime currentEnd);
}
