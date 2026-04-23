package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Meeting;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeetingRepository {
    Optional<Meeting> findById(UUID id);
    List<Meeting> findByCollaboratorId(UUID collaboratorId);
    List<Meeting> findByClientId(UUID clientId);
    Meeting save(Meeting meeting);
}
