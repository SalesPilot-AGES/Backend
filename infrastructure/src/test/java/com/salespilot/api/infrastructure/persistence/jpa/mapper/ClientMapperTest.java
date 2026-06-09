package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Client;
import com.salespilot.api.infrastructure.persistence.jpa.entity.ClientEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapperTest {

    private final ClientMapper mapper = new ClientMapper();

    @Test
    void shouldMapEntityToDomainWithAllFields() {
        CompanyEntity company = new CompanyEntity();
        company.setId(UUID.randomUUID());

        CollaboratorEntity collaborator = new CollaboratorEntity();
        collaborator.setId(UUID.randomUUID());

        ClientEntity entity = new ClientEntity();
        entity.setId(UUID.randomUUID());
        entity.setCompany(company);
        entity.setCollaborator(collaborator);
        entity.setName("Carlos Lima");
        entity.setClientCompanyName("Tech Solutions");
        entity.setSector("Tecnologia");
        entity.setOverallSentiment(80);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        Client result = mapper.toDomain(entity);

        assertEquals(entity.getId(), result.getId());
        assertEquals(company.getId(), result.getCompanyId());
        assertEquals(collaborator.getId(), result.getCollaboratorId());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getClientCompanyName(), result.getClientCompanyName());
        assertEquals(entity.getSector(), result.getSector());
        assertEquals(entity.getOverallSentiment(), result.getOverallSentiment());
        assertEquals(entity.getCreatedAt(), result.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), result.getUpdatedAt());
    }
}
