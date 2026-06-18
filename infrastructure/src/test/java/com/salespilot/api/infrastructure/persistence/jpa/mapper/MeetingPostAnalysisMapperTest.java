package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.MeetingPostAnalysis;
import com.salespilot.api.domain.valueobject.PostAnalysisActionItem;
import com.salespilot.api.domain.valueobject.PostAnalysisSentimentAnalysis;
import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingPostAnalysisEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MeetingPostAnalysisMapperTest {

    private final MeetingPostAnalysisMapper mapper = new MeetingPostAnalysisMapper();

    private MeetingPostAnalysisEntity buildEntity(List<PostAnalysisActionItem> actionItems) {
        MeetingEntity meeting = new MeetingEntity();
        meeting.setId(UUID.randomUUID());

        MeetingPostAnalysisEntity entity = new MeetingPostAnalysisEntity();
        entity.setId(UUID.randomUUID());
        entity.setMeeting(meeting);
        entity.setSummary("Cliente demonstrou interesse na proposta.");
        entity.setActionItems(actionItems);
        entity.setSentimentAnalysis(new PostAnalysisSentimentAnalysis("positive", 0.82));
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    @Test
    void shouldMapEntityToDomainWithActionItemsAndSentiment() {
        List<PostAnalysisActionItem> items = List.of(
                new PostAnalysisActionItem("Enviar proposta revisada", false),
                new PostAnalysisActionItem("Agendar próxima etapa", false)
        );
        MeetingPostAnalysisEntity entity = buildEntity(items);

        MeetingPostAnalysis result = mapper.toDomain(entity);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getMeeting().getId(), result.getMeetingId());
        assertEquals(entity.getSummary(), result.getSummary());
        assertEquals(2, result.getActionItems().size());
        assertNotNull(result.getSentimentAnalysis());
        assertEquals("positive", result.getSentimentAnalysis().overall());
        assertEquals(entity.getCreatedAt(), result.getCreatedAt());
    }

    @Test
    void shouldMapEntityWithEmptyActionItemList() {
        MeetingPostAnalysisEntity entity = buildEntity(List.of());

        MeetingPostAnalysis result = mapper.toDomain(entity);

        assertNotNull(result.getActionItems());
        assertTrue(result.getActionItems().isEmpty());
    }
}
