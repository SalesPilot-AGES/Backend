package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.OptionalDouble;
import java.util.UUID;

public interface MeetingRepository {
    /*Optional<Meeting> findById(UUID id);
    List<Meeting> findByClientId(UUID clientId);
    List<Meeting> findByCollaboratorId(UUID collaboratorId);
    Meeting save(Meeting meeting);*/
    Page<Meeting> getAllMeetings(String title, String clientCompanyName, UUID collaboratorID, Pageable pageable);
    long getTotalMeetingsByCollaboratorId(UUID collaboratorId);
    OptionalDouble getAverageDurationSecondsByCollaboratorId(UUID collaboratorId);
    long getTotalMeetings();
    OptionalDouble getAverageDurationSeconds();
}
