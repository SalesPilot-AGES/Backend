package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeetingRepository {
    /*Optional<Meeting> findById(UUID id);
    List<Meeting> findByCollaboratorId(UUID collaboratorId);
    List<Meeting> findByClientId(UUID clientId);
    Meeting save(Meeting meeting);*/
    Page<Meeting> getAllMeetings(String title, String clientCompanyName, UUID collaboratorID, Pageable pageable);
}
