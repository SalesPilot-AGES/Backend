package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.AdminGroupCardMetricsResponseDTO;
import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.dto.GroupCardMetricsResponse;
import com.salespilot.api.application.dto.ManagerGroupCardMetricsResponseDTO;
import com.salespilot.api.application.dto.SellerGroupCardMetricsResponseDTO;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.domain.repository.MeetingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCardMetricsUseCaseTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @InjectMocks
    private GetCardMetricsUseCase useCase;

    private final UUID companyId = UUID.randomUUID();
    private final UUID sellerId = UUID.randomUUID();

    @Test
    void shouldReturnAdminGroupCard_whenSystemAdmin() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SYSTEM_ADMIN, UUID.randomUUID(), UUID.randomUUID());

        when(companyRepository.countCompaniesByActiveValue(true)).thenReturn(5L);
        when(companyRepository.countCompaniesByActiveValue(false)).thenReturn(5L);
        when(companyRepository.countCompaniesByActiveValueAndPeriod(eq(true), any(LocalDateTime.class))).thenReturn(5L);
        when(companyRepository.countCompaniesByActiveValueAndPeriod(eq(false), any(LocalDateTime.class))).thenReturn(5L);
        when(meetingRepository.countTotalMeetingsByPeriod(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(5L);
        when(collaboratorRepository.countAllActiveSellers()).thenReturn(5L);
        when(collaboratorRepository.countAllActiveSellersByPeriod(any(LocalDateTime.class))).thenReturn(5L);

        GroupCardMetricsResponse result = useCase.execute("30d", null, null, authUser);

        assertInstanceOf(AdminGroupCardMetricsResponseDTO.class, result);
        verify(companyRepository, times(1)).countCompaniesByActiveValue(true);
    }

    @Test
    void shouldReturnManagerGroupCard_whenManager() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.MANAGER, UUID.randomUUID(), companyId);

        when(collaboratorRepository.countSellersByCompanyIdAndActiveValue(eq(companyId), eq(true))).thenReturn(3L);
        when(collaboratorRepository.countSellersByCompanyIdAndActiveValue(eq(companyId), eq(false))).thenReturn(3L);
        when(collaboratorRepository.countActiveSellersByCompanyIdAndPeriod(eq(companyId), any(LocalDateTime.class))).thenReturn(3L);
        when(collaboratorRepository.countInactiveSellersByCompanyIdAndPeriod(eq(companyId), any(LocalDateTime.class))).thenReturn(3L);
        when(meetingRepository.countTotalMeetingsByCompanyIdAndPeriod(eq(companyId), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(3L);

        GroupCardMetricsResponse result = useCase.execute("30d", null, null, authUser);

        assertInstanceOf(ManagerGroupCardMetricsResponseDTO.class, result);
        verify(meetingRepository, times(2)).countTotalMeetingsByCompanyIdAndPeriod(eq(companyId), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void shouldReturnSellerGroupCard_whenSeller() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SELLER, sellerId, UUID.randomUUID());

        when(meetingRepository.countTotalMeetingsByCollaboratorIdAndPeriod(eq(sellerId), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(5L);
        when(meetingRepository.getAverageDurationByCollaboratorIdAndPeriod(eq(sellerId), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(60.0);

        GroupCardMetricsResponse result = useCase.execute("30d", null, null, authUser);

        assertInstanceOf(SellerGroupCardMetricsResponseDTO.class, result);
    }

    @Test
    void shouldCalculate100Percent_whenPreviousZeroCurrentPositive() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SELLER, sellerId, UUID.randomUUID());

        when(meetingRepository.countTotalMeetingsByCollaboratorIdAndPeriod(eq(sellerId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(10L, 0L);
        when(meetingRepository.getAverageDurationByCollaboratorIdAndPeriod(eq(sellerId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(60.0);

        GroupCardMetricsResponse result = useCase.execute("30d", null, null, authUser);

        SellerGroupCardMetricsResponseDTO sellerResult = (SellerGroupCardMetricsResponseDTO) result;
        assertEquals(100.0, sellerResult.totalMeetings().variationPercent());
        assertEquals("up", sellerResult.totalMeetings().trend());
    }

    @Test
    void shouldCalculate0Percent_whenBothZero() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SELLER, sellerId, UUID.randomUUID());

        when(meetingRepository.countTotalMeetingsByCollaboratorIdAndPeriod(eq(sellerId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(0L, 0L);
        when(meetingRepository.getAverageDurationByCollaboratorIdAndPeriod(eq(sellerId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(0.0);

        GroupCardMetricsResponse result = useCase.execute("30d", null, null, authUser);

        SellerGroupCardMetricsResponseDTO sellerResult = (SellerGroupCardMetricsResponseDTO) result;
        assertEquals(0.0, sellerResult.totalMeetings().variationPercent());
        assertEquals("neutral", sellerResult.totalMeetings().trend());
    }

    @Test
    void shouldCalculateNegativePercent_whenCurrentLessThanPrevious() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SELLER, sellerId, UUID.randomUUID());

        when(meetingRepository.countTotalMeetingsByCollaboratorIdAndPeriod(eq(sellerId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(5L, 10L);
        when(meetingRepository.getAverageDurationByCollaboratorIdAndPeriod(eq(sellerId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(60.0);

        GroupCardMetricsResponse result = useCase.execute("30d", null, null, authUser);

        SellerGroupCardMetricsResponseDTO sellerResult = (SellerGroupCardMetricsResponseDTO) result;
        assertEquals(-50.0, sellerResult.totalMeetings().variationPercent());
        assertEquals("down", sellerResult.totalMeetings().trend());
    }
}
