package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.infrastructure.persistence.jpa.entity.ClientEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MeetingMapperTest {

    private final MeetingMapper mapper = new MeetingMapper();

    private MeetingEntity buildEntity(boolean withOptionalFields) {
        CollaboratorEntity collaborator = new CollaboratorEntity();
        collaborator.setId(UUID.randomUUID());

        ClientEntity client = new ClientEntity();
        client.setId(UUID.randomUUID());

        MeetingEntity entity = new MeetingEntity();
        entity.setId(UUID.randomUUID());
        entity.setCollaborator(collaborator);
        entity.setClient(client);
        entity.setTitle("Reunião de descoberta");
        entity.setStatus("SCHEDULED");
        entity.setDurationSeconds(1800);
        entity.setMeetingType("ONLINE");
        entity.setObjective("Apresentar proposta");
        entity.setClientNeeds("Integração com CRM");
        entity.setPreviousInteractions("Demo realizada anteriormente");
        entity.setCompetitorsInvolved("Concorrente X");
        entity.setScheduledFor(LocalDateTime.now());
        entity.setCreatedAt(LocalDateTime.now());

        if (withOptionalFields) {
            entity.setStartedAt(LocalDateTime.now());
            entity.setEndedAt(LocalDateTime.now().plusMinutes(30));
        }
        return entity;
    }

    @Test
    void shouldMapEntityToDomainWithAllFields() {
        MeetingEntity entity = buildEntity(true);

        Meeting result = mapper.toDomain(entity);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getCollaborator().getId(), result.getCollaboratorId());
        assertEquals(entity.getClient().getId(), result.getClientId());
        assertEquals(entity.getTitle(), result.getTitle());
        assertEquals(entity.getStatus(), result.getStatus());
        assertEquals(entity.getDurationSeconds(), result.getDurationSeconds());
        assertEquals(entity.getMeetingType(), result.getMeetingType());
        assertNotNull(result.getStartedAt());
        assertNotNull(result.getEndedAt());
    }

    @Test
    void shouldMapEntityWithNullOptionalFieldsToDomain() {
        MeetingEntity entity = buildEntity(false);

        Meeting result = mapper.toDomain(entity);

        assertEquals(entity.getId(), result.getId());
        assertNull(result.getStartedAt());
        assertNull(result.getEndedAt());
    }
}
