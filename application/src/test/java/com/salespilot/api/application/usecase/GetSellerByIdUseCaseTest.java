package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.SellerWithMeetingsResponseDTO;
import com.salespilot.api.application.exception.ClientNotFoundException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.application.exception.InvalidCollaboratorRoleException;
import com.salespilot.api.domain.entity.Client;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.ClientRepository;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.domain.repository.MeetingRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetSellerByIdUseCaseTest {

    @Mock
    private CollaboratorRepository collaboratorRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private GetSellerByIdUseCase useCase;

    private final LocalDateTime now = LocalDateTime.now();
    private final UUID sellerId = UUID.randomUUID();
    private final UUID companyId = UUID.randomUUID();
    private final UUID meetingId = UUID.randomUUID();
    private final UUID clientId = UUID.randomUUID();
    private final CollaboratorPreferences preferences = new CollaboratorPreferences("light", "gpt-4o");

    private Collaborator buildSeller() {
        return new Collaborator(sellerId, companyId, "Ana", "ana@acme.com",
                "+55 11 88888-0000", CollaboratorRole.SELLER, true, 0, preferences, now, now);
    }

    private Collaborator buildCollaboratorWithRole(CollaboratorRole role) {
        return new Collaborator(sellerId, companyId, "Ana", "ana@acme.com",
                "+55 11 88888-0000", role, true, 0, preferences, now, now);
    }

    private Company buildCompany() {
        return new Company(companyId, "Acme Corp", "12.345.678/0001-90", null, null, true, now, now, "BASIC", List.of());
    }

    private Meeting buildMeeting() {
        return new Meeting(meetingId, sellerId, clientId, "Sales Call", "DONE",
                3600, null, "PRESENTIAL", null, null, null, now, now, now, now);
    }

    private Client buildClient() {
        return new Client(clientId, companyId, sellerId, "Cliente A", "Tech Corp",
                "Technology", 8, now, now);
    }

    @Test
    void shouldReturnSellerWithLatestMeetingAndTotalMeetings() {
        when(collaboratorRepository.getCollaboratorById(sellerId)).thenReturn(Optional.of(buildSeller()));
        when(companyRepository.getCompanyById(companyId)).thenReturn(Optional.of(buildCompany()));
        when(meetingRepository.getLatestMeetingByCollaborator(sellerId)).thenReturn(Optional.of(buildMeeting()));
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(buildClient()));
        when(meetingRepository.getTotalMeetingsByCollaborator(sellerId)).thenReturn(10L);

        SellerWithMeetingsResponseDTO result = useCase.execute(sellerId);

        assertEquals(sellerId, result.id());
        assertEquals("Ana", result.name());
        assertEquals(CollaboratorRole.SELLER, result.role());
        assertEquals("Acme Corp", result.company().name());
        assertEquals(10L, result.totalMeetings());
        assertNotNull(result.latestMeeting());
        assertEquals("Sales Call", result.latestMeeting().title());
        assertEquals("Cliente A", result.latestMeeting().client().name());
    }

    @Test
    void shouldReturnSellerWithNullLatestMeetingWhenNoMeetings() {
        when(collaboratorRepository.getCollaboratorById(sellerId)).thenReturn(Optional.of(buildSeller()));
        when(companyRepository.getCompanyById(companyId)).thenReturn(Optional.of(buildCompany()));
        when(meetingRepository.getLatestMeetingByCollaborator(sellerId)).thenReturn(Optional.empty());
        when(meetingRepository.getTotalMeetingsByCollaborator(sellerId)).thenReturn(0L);

        SellerWithMeetingsResponseDTO result = useCase.execute(sellerId);

        assertNull(result.latestMeeting());
        assertEquals(0L, result.totalMeetings());
    }

    @Test
    void shouldThrowWhenCollaboratorNotFound() {
        when(collaboratorRepository.getCollaboratorById(sellerId)).thenReturn(Optional.empty());

        assertThrows(CollaboratorNotFoundException.class, () -> useCase.execute(sellerId));

        verifyNoInteractions(companyRepository, meetingRepository, clientRepository);
    }

    @Test
    void shouldThrowWhenCollaboratorRoleIsNotSeller() {
        when(collaboratorRepository.getCollaboratorById(sellerId))
                .thenReturn(Optional.of(buildCollaboratorWithRole(CollaboratorRole.MANAGER)));

        assertThrows(InvalidCollaboratorRoleException.class, () -> useCase.execute(sellerId));

        verifyNoInteractions(companyRepository, meetingRepository, clientRepository);
    }

    @Test
    void shouldThrowWhenCompanyNotFound() {
        when(collaboratorRepository.getCollaboratorById(sellerId)).thenReturn(Optional.of(buildSeller()));
        when(companyRepository.getCompanyById(companyId)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> useCase.execute(sellerId));
    }

    @Test
    void shouldThrowWhenClientNotFoundForLatestMeeting() {
        when(collaboratorRepository.getCollaboratorById(sellerId)).thenReturn(Optional.of(buildSeller()));
        when(companyRepository.getCompanyById(companyId)).thenReturn(Optional.of(buildCompany()));
        when(meetingRepository.getLatestMeetingByCollaborator(sellerId)).thenReturn(Optional.of(buildMeeting()));
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> useCase.execute(sellerId));
    }
}
