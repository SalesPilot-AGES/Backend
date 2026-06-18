package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.model.AverageMeetingDurationPerMonth;
import com.salespilot.api.domain.model.MonthAndTotal;
import com.salespilot.api.infrastructure.InfrastructureTestApplication;
import com.salespilot.api.infrastructure.persistence.jpa.entity.ClientEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = InfrastructureTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@Testcontainers
@Transactional
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.flyway.enabled=true",
        "spring.flyway.locations=classpath:db/migration",
        "spring.flyway.schemas=public,auth",
        "spring.flyway.create-schemas=true",
        "spring.jpa.properties.hibernate.default_schema=public"
})
class MeetingRepositoryImplTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    MeetingRepositoryImpl meetingRepository;

    @Autowired
    MeetingsJpaRepository meetingsJpaRepository;

    @Autowired
    CompanyJpaRepository companyJpaRepository;

    @Autowired
    CollaboratorJpaRepository collaboratorJpaRepository;

    @Autowired
    ClientsJpaRepository clientsJpaRepository;

    CompanyEntity company;
    CollaboratorEntity collaborator;
    ClientEntity client;

    @BeforeEach
    void setUp() {
        company = companyJpaRepository.saveAndFlush(new CompanyEntity("Test Corp", "11.111.111/0001-11", true));

        collaborator = new CollaboratorEntity(company, "Ana", "ana@test.com", CollaboratorRole.SELLER, true, null, null);
        collaborator = collaboratorJpaRepository.saveAndFlush(collaborator);

        client = new ClientEntity();
        client.setId(UUID.randomUUID());
        client.setCompany(company);
        client.setCollaborator(collaborator);
        client.setName("Test Client");
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());
        client = clientsJpaRepository.saveAndFlush(client);
    }

    MeetingEntity createMeeting(String title, LocalDateTime createdAt) {
        MeetingEntity m = new MeetingEntity();
        m.setId(UUID.randomUUID());
        m.setCollaborator(collaborator);
        m.setClient(client);
        m.setTitle(title);
        m.setStatus("SCHEDULED");
        m.setCreatedAt(createdAt);
        return meetingsJpaRepository.saveAndFlush(m);
    }

    // ---------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------

    @Test
    void shouldFindMeetingById() {
        MeetingEntity meeting = createMeeting("Reunião de Descoberta", LocalDateTime.now());

        Optional<Meeting> result = meetingRepository.getMeetingById(meeting.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Reunião de Descoberta");
    }

    @Test
    void shouldReturnEmptyWhenMeetingNotFound() {
        Optional<Meeting> result = meetingRepository.getMeetingById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFilterMeetingsByTitle() {
        createMeeting("Reunião de Descoberta", LocalDateTime.now());
        createMeeting("Apresentação de Proposta", LocalDateTime.now());

        Page<Meeting> result = meetingRepository.getAllMeetings(
                "Descoberta", null, null, null, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Reunião de Descoberta");
    }

    @Test
    void shouldFilterMeetingsByCollaboratorId() {
        CollaboratorEntity secondCollaborator = new CollaboratorEntity(
                company, "Bob", "bob@test.com", CollaboratorRole.SELLER, true, null, null);
        secondCollaborator = collaboratorJpaRepository.saveAndFlush(secondCollaborator);

        ClientEntity secondClient = new ClientEntity();
        secondClient.setId(UUID.randomUUID());
        secondClient.setCompany(company);
        secondClient.setCollaborator(secondCollaborator);
        secondClient.setName("Bob Client");
        secondClient.setCreatedAt(LocalDateTime.now());
        secondClient.setUpdatedAt(LocalDateTime.now());
        secondClient = clientsJpaRepository.saveAndFlush(secondClient);

        createMeeting("Ana's Meeting", LocalDateTime.now());

        MeetingEntity bobMeeting = new MeetingEntity();
        bobMeeting.setId(UUID.randomUUID());
        bobMeeting.setCollaborator(secondCollaborator);
        bobMeeting.setClient(secondClient);
        bobMeeting.setTitle("Bob's Meeting");
        bobMeeting.setStatus("SCHEDULED");
        bobMeeting.setCreatedAt(LocalDateTime.now());
        meetingsJpaRepository.saveAndFlush(bobMeeting);

        Page<Meeting> result = meetingRepository.getAllMeetings(
                null, null, collaborator.getId(), null, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldCountMeetingsByPeriod() {
        createMeeting("First Meeting", LocalDateTime.now());
        createMeeting("Second Meeting", LocalDateTime.now());

        Long count = meetingRepository.countTotalMeetingsByPeriod(
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1));

        assertThat(count).isEqualTo(2L);
    }

    @Test
    void shouldReturnGroupedMeetingsByMonth() {
        createMeeting("First Meeting", LocalDateTime.now());
        createMeeting("Second Meeting", LocalDateTime.now());

        List<MonthAndTotal> result = meetingRepository.getMeetingsGroupedByMonth(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                null,
                null);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).total()).isEqualTo(2L);
    }

    @Test
    void shouldCheckExistsById() {
        MeetingEntity meeting = createMeeting("Check Meeting", LocalDateTime.now());

        assertThat(meetingRepository.existsById(meeting.getId())).isTrue();
        assertThat(meetingRepository.existsById(UUID.randomUUID())).isFalse();
    }

    @Test
    void shouldCountMeetingsByCollaboratorAndPeriod() {
        createMeeting("Meeting A", LocalDateTime.now());
        createMeeting("Meeting B", LocalDateTime.now());

        Long count = meetingRepository.countTotalMeetingsByCollaboratorIdAndPeriod(
                collaborator.getId(),
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1));

        assertThat(count).isEqualTo(2L);
    }

    @Test
    void shouldGetAverageDurationPerMonth() {
        MeetingEntity m = new MeetingEntity();
        m.setId(UUID.randomUUID());
        m.setCollaborator(collaborator);
        m.setClient(client);
        m.setTitle("Duration Meeting");
        m.setStatus("COMPLETED");
        m.setDurationSeconds(1800);
        m.setCreatedAt(LocalDateTime.now());
        meetingsJpaRepository.saveAndFlush(m);

        List<AverageMeetingDurationPerMonth> result = meetingRepository.groupAverageMeetingDurationPerMonth(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1));

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).avgMinutes()).isNotNull();
    }
}
