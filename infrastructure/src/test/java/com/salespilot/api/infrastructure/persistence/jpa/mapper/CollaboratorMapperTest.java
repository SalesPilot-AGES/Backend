package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CollaboratorMapperTest {

    private final CollaboratorMapper mapper = new CollaboratorMapper();

    private CollaboratorEntity buildEntity() {
        CompanyEntity company = new CompanyEntity();
        company.setId(UUID.randomUUID());

        CollaboratorEntity entity = new CollaboratorEntity();
        entity.setId(UUID.randomUUID());
        entity.setCompany(company);
        entity.setName("Ana Silva");
        entity.setEmail("ana@acme.com");
        entity.setPhone("+55 11 99999-0000");
        entity.setRole(CollaboratorRole.SELLER);
        entity.setActive(true);
        entity.setPreferences(new CollaboratorPreferences("dark", "gpt-4o"));
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    @Test
    void shouldMapEntityToDomainWithAllFields() {
        CollaboratorEntity entity = buildEntity();

        Collaborator result = mapper.toDomain(entity);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getCompany().getId(), result.getCompanyId());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getEmail(), result.getEmail());
        assertEquals(entity.getPhone(), result.getPhone());
        assertEquals(entity.getRole(), result.getRole());
        assertEquals(entity.isActive(), result.isActive());
        assertEquals(entity.getPreferences(), result.getPreferences());
        assertEquals(entity.getCreatedAt(), result.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    void shouldMapEntityListToDomainList() {
        List<CollaboratorEntity> entities = List.of(buildEntity(), buildEntity(), buildEntity());

        List<Collaborator> result = mapper.toDomainList(entities);

        assertEquals(3, result.size());
    }

    @Test
    void shouldMapEmptyListToEmptyDomainList() {
        List<Collaborator> result = mapper.toDomainList(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
