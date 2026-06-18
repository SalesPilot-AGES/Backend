package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.MeetingRealtimeInsight;
import com.salespilot.api.domain.enums.RealtimeInsightType;
import com.salespilot.api.domain.valueobject.InsightDescription;
import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingRealtimeInsightsEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.RealtimeInsightTypeEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MeetingRealtimeInsightMapperTest {

    private final MeetingRealtimeInsightMapper mapper = new MeetingRealtimeInsightMapper();

    private MeetingRealtimeInsightsEntity buildEntity(RealtimeInsightTypeEntity type) {
        MeetingEntity meeting = new MeetingEntity();
        meeting.setId(UUID.randomUUID());

        MeetingRealtimeInsightsEntity entity = new MeetingRealtimeInsightsEntity();
        entity.setId(UUID.randomUUID());
        entity.setMeeting(meeting);
        entity.setType(type);
        entity.setDescription(new InsightDescription("Urgência de integração destacada"));
        entity.setContent("A equipe precisa conectar CRM e ERP ainda neste trimestre.");
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    @Test
    void shouldMapKeyPointEntityToDomain() {
        MeetingRealtimeInsightsEntity entity = buildEntity(RealtimeInsightTypeEntity.KEY_POINT);

        MeetingRealtimeInsight result = mapper.toDomain(entity);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getMeeting().getId(), result.getMeetingId());
        assertEquals(RealtimeInsightType.KEY_POINT, result.getType());
        assertEquals(entity.getDescription(), result.getDescription());
        assertEquals(entity.getContent(), result.getContent());
        assertEquals(entity.getCreatedAt(), result.getCreatedAt());
    }

    @Test
    void shouldMapActionItemEntityToDomain() {
        MeetingRealtimeInsightsEntity entity = buildEntity(RealtimeInsightTypeEntity.ACTION_ITEM);

        MeetingRealtimeInsight result = mapper.toDomain(entity);

        assertEquals(RealtimeInsightType.ACTION_ITEM, result.getType());
    }
}
