package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.MeetingPostAnalysis;
import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingPostAnalysisEntity;
import org.springframework.stereotype.Component;

@Component
public class MeetingPostAnalysisMapper {
    public MeetingPostAnalysis toDomain(MeetingPostAnalysisEntity entity) {
        return new MeetingPostAnalysis(
                entity.getId(),
                entity.getMeeting().getId(),
                entity.getSummary(),
                entity.getActionItems(),
                entity.getSentimentAnalysis(),
                entity.getCreatedAt()
        );
    }
}
