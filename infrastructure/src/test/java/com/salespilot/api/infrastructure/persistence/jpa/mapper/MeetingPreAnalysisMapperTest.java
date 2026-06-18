package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.MeetingPreAnalysis;
import com.salespilot.api.domain.valueobject.PreAnalysisRecommendedStrategy;
import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingPreAnalysisEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MeetingPreAnalysisMapperTest {

    private final MeetingPreAnalysisMapper mapper = new MeetingPreAnalysisMapper();

    @Test
    void shouldMapEntityToDomainWithAllValueObjects() {
        MeetingEntity meeting = new MeetingEntity();
        meeting.setId(UUID.randomUUID());

        MeetingPreAnalysisEntity entity = new MeetingPreAnalysisEntity();
        entity.setId(UUID.randomUUID());
        entity.setMeeting(meeting);
        entity.setRecommendedStrategy(new PreAnalysisRecommendedStrategy("ROI e tempo de implementação"));
        entity.setKeyPoints(List.of("Mapear processo atual", "Explorar gargalos"));
        entity.setPossibleObjections(List.of("Preço", "Prazo de rollout"));
        entity.setCreatedAt(LocalDateTime.now());

        MeetingPreAnalysis result = mapper.toDomain(entity);

        assertEquals(entity.getId(), result.getId());
        assertEquals(meeting.getId(), result.getMeetingId());
        assertNotNull(result.getRecommendedStrategy());
        assertEquals("ROI e tempo de implementação", result.getRecommendedStrategy().focus());
        assertEquals(2, result.getKeyPoints().size());
        assertEquals(2, result.getPossibleObjections().size());
        assertEquals(entity.getCreatedAt(), result.getCreatedAt());
    }
}
