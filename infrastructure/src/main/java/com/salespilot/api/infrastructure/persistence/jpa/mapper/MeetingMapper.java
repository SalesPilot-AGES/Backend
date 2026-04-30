package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.infrastructure.persistence.jpa.entity.Meetings;
import org.springframework.stereotype.Component;

@Component
public class MeetingMapper {
    public Meeting toDomain(Meetings meetings) {
        return new Meeting(
                meetings.getId(),
                meetings.getCollaborator().getId(),
                meetings.getClients().getId(),
                meetings.getTitle(),
                meetings.getStatus(),
                meetings.getDurationSeconds(),
                meetings.getObjective(),
                meetings.getMeetingType(),
                meetings.getClientNeeds(),
                meetings.getPreviousInteractions(),
                meetings.getCompetitorsInvolved(),
                meetings.getScheduledFor(),
                meetings.getStartedAt(),
                meetings.getEndedAt(),
                meetings.getCreatedAt()
        );
    }
}
