package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingEntity;
import org.springframework.stereotype.Component;

@Component
public class MeetingMapper {
    public Meeting toDomain(MeetingEntity meetingEntity) {
        return new Meeting(
                meetingEntity.getId(),
                meetingEntity.getCollaborator().getId(),
                meetingEntity.getClient().getId(),
                meetingEntity.getTitle(),
                meetingEntity.getStatus(),
                meetingEntity.getDurationSeconds(),
                meetingEntity.getObjective(),
                meetingEntity.getMeetingType(),
                meetingEntity.getClientNeeds(),
                meetingEntity.getPreviousInteractions(),
                meetingEntity.getCompetitorsInvolved(),
                meetingEntity.getScheduledFor(),
                meetingEntity.getStartedAt(),
                meetingEntity.getEndedAt(),
                meetingEntity.getCreatedAt()
        );
    }
}
