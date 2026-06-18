package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanySubscriptions;
import com.salespilot.api.infrastructure.persistence.jpa.entity.SubscriptionPlans;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyMapperTest {

    private final CompanyMapper mapper = new CompanyMapper();

    private CompanyEntity buildBaseEntity() {
        CompanyEntity entity = new CompanyEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("Acme Corp");
        entity.setTaxId("12.345.678/0001-90");
        entity.setPhone("+55 11 3333-4444");
        entity.setAddress("Rua das Flores, 100");
        entity.setActive(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    @Test
    void shouldMapEntityToDomainWithActivePlan() {
        CompanyEntity entity = buildBaseEntity();

        SubscriptionPlans plan = mock(SubscriptionPlans.class);
        when(plan.getName()).thenReturn("PRO");

        CompanySubscriptions subscription = mock(CompanySubscriptions.class);
        when(subscription.isActive()).thenReturn(true);
        when(subscription.getSubscriptionPlans()).thenReturn(plan);

        entity.setSubscriptions(List.of(subscription));

        Company result = mapper.toDomain(entity);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getTaxId(), result.getTaxId());
        assertEquals(entity.isActive(), result.isActive());
        assertEquals("PRO", result.getPlan());
    }

    @Test
    void shouldMapEntityToDomainWithNullPlanWhenNoActiveSubscription() {
        CompanyEntity entity = buildBaseEntity();

        CompanySubscriptions inactiveSub = mock(CompanySubscriptions.class);
        when(inactiveSub.isActive()).thenReturn(false);
        entity.setSubscriptions(List.of(inactiveSub));

        Company result = mapper.toDomain(entity);

        assertNull(result.getPlan());
    }
}
