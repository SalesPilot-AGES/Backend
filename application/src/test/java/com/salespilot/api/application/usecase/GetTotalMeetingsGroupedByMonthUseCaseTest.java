package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.dto.MeetingsGroupedByMonthResponseDTO;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.model.MonthAndTotal;
import com.salespilot.api.domain.repository.MeetingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTotalMeetingsGroupedByMonthUseCaseTest {

    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private GetTotalMeetingsGroupedByMonthUseCase useCase;

    @Test
    void shouldReturnMappedData_whenSystemAdmin() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SYSTEM_ADMIN, UUID.randomUUID(), UUID.randomUUID());

        when(meetingRepository.getMeetingsGroupedByMonth(
                any(LocalDateTime.class), any(LocalDateTime.class), isNull(), isNull()))
                .thenReturn(List.of(new MonthAndTotal(LocalDate.of(2024, 1, 1), "Jan", 10L)));

        MeetingsGroupedByMonthResponseDTO result = useCase.execute("all", null, null, authUser);

        assertEquals(1, result.data().size());
        assertEquals("Jan", result.data().get(0).monthLabel());
        assertEquals(10L, result.data().get(0).total());
        verify(meetingRepository, times(1))
                .getMeetingsGroupedByMonth(any(LocalDateTime.class), any(LocalDateTime.class), isNull(), isNull());
    }

    @Test
    void shouldPassCompanyIdScope_whenManager() {
        UUID companyId = UUID.randomUUID();
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.MANAGER, UUID.randomUUID(), companyId);

        when(meetingRepository.getMeetingsGroupedByMonth(
                any(LocalDateTime.class), any(LocalDateTime.class), eq(companyId), isNull()))
                .thenReturn(List.of());

        useCase.execute("all", null, null, authUser);

        verify(meetingRepository, times(1))
                .getMeetingsGroupedByMonth(any(LocalDateTime.class), any(LocalDateTime.class), eq(companyId), isNull());
    }

    @Test
    void shouldPassCollaboratorIdScope_whenSeller() {
        UUID sellerId = UUID.randomUUID();
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SELLER, sellerId, UUID.randomUUID());

        when(meetingRepository.getMeetingsGroupedByMonth(
                any(LocalDateTime.class), any(LocalDateTime.class), isNull(), eq(sellerId)))
                .thenReturn(List.of());

        useCase.execute("all", null, null, authUser);

        verify(meetingRepository, times(1))
                .getMeetingsGroupedByMonth(any(LocalDateTime.class), any(LocalDateTime.class), isNull(), eq(sellerId));
    }

    @Test
    void shouldReturnEmptyList_whenRepositoryReturnsEmpty() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SYSTEM_ADMIN, UUID.randomUUID(), UUID.randomUUID());

        when(meetingRepository.getMeetingsGroupedByMonth(
                any(LocalDateTime.class), any(LocalDateTime.class), isNull(), isNull()))
                .thenReturn(List.of());

        MeetingsGroupedByMonthResponseDTO result = useCase.execute("all", null, null, authUser);

        assertTrue(result.data().isEmpty());
    }
}
