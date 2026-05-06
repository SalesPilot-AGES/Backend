package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import org.springframework.stereotype.Component;

import com.salespilot.api.domain.entity.MeetingRealtimeInsight;
import com.salespilot.api.domain.enums.RealtimeInsightType;

import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingRealtimeInsightsEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.RealtimeInsightTypeEntity;

@Component
public class MeetingRealtimeInsightMapper {
    public MeetingRealtimeInsight toDomain(MeetingRealtimeInsightsEntity entity){
        return new MeetingRealtimeInsight(
            entity.getId(),
            entity.getMeeting().getId(),
            entity.getContent(),
            mapType(entity.getType()),
            entity.getDescription(),
            entity.getCreatedAt()
        );
    }

    private RealtimeInsightType mapType(RealtimeInsightTypeEntity type){
        return RealtimeInsightType.valueOf(type.name());
    }
}
