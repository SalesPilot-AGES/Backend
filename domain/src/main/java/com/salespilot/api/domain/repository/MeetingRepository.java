package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MeetingRepository {
    Long getTotalMeetingsByCollaborator(UUID collaboratorId);
    Page<Meeting> getAllMeetings(String title, String clientCompanyName, UUID collaboratorID, Pageable pageable);
    long getTotalMeetings();
    double getAverageDurationSeconds();
}
