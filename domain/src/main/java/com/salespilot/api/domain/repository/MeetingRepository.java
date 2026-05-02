package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface MeetingRepository {
    Page<Meeting> getAllMeetings(String title, String clientCompanyName, UUID collaboratorID, Pageable pageable);
    long getTotalMeetings();
    double getAverageDurationSeconds();
    Optional<Meeting> getMeetingById(UUID id);
}
