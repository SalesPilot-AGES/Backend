package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.SellerResponseDTO;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.domain.repository.MeetingPostAnalysisRepository;
import com.salespilot.api.domain.repository.MeetingRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllSellersUseCaseTest {

    @Mock
    private CollaboratorRepository repository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private MeetingRepository meetingRepository;
    @Mock
    private MeetingPostAnalysisRepository meetingPostAnalysisRepository;

    @InjectMocks
    private GetAllSellersUseCase useCase;

    private final LocalDateTime now = LocalDateTime.now();
    private final Pageable pageable = PageRequest.of(0, 10);
    private final UUID sellerId = UUID.randomUUID();
    private final UUID companyId = UUID.randomUUID();
    private final CollaboratorPreferences preferences = new CollaboratorPreferences("light", "gpt-4o");

    private Collaborator buildSeller() {
        return new Collaborator(sellerId, companyId, "Ana", "ana@acme.com",
                "+55 11 88888-0000", CollaboratorRole.SELLER, true, 0, preferences, now, now);
    }

    private Company buildCompany() {
        return new Company(companyId, "Acme Corp", "12.345.678/0001-90", null, null, true, now, now, "BASIC", List.of());
    }

    @Test
    void shouldReturnPageOfSellersWithAggregatedData() {
        Page<Collaborator> sellers = new PageImpl<>(List.of(buildSeller()));

        when(repository.getSellers(null, null, null, null, pageable)).thenReturn(sellers);
        when(meetingRepository.getTotalMeetingsByCollaborator(sellerId)).thenReturn(5L);
        when(meetingPostAnalysisRepository.getAverageFeelingByCollaborator(sellerId)).thenReturn(7.5);
        when(companyRepository.getCompanyById(companyId)).thenReturn(Optional.of(buildCompany()));

        Page<SellerResponseDTO> result = useCase.execute(null, null, null, null, pageable);

        assertEquals(1, result.getTotalElements());
        SellerResponseDTO seller = result.getContent().get(0);
        assertEquals(sellerId, seller.id());
        assertEquals("Ana", seller.name());
        assertEquals(5L, seller.totalMeetings());
        assertEquals(7.5, seller.averageFeeling());
        assertEquals("Acme Corp", seller.company().name());
    }

    @Test
    void shouldReturnEmptyPageWhenNoSellers() {
        when(repository.getSellers(null, null, null, null, pageable))
                .thenReturn(new PageImpl<>(List.of()));

        Page<SellerResponseDTO> result = useCase.execute(null, null, null, null, pageable);

        assertTrue(result.isEmpty());
        verifyNoInteractions(meetingRepository, meetingPostAnalysisRepository, companyRepository);
    }

    @Test
    void shouldThrowWhenCompanyNotFoundForSeller() {
        Page<Collaborator> sellers = new PageImpl<>(List.of(buildSeller()));

        when(repository.getSellers(null, null, null, null, pageable)).thenReturn(sellers);
        when(meetingRepository.getTotalMeetingsByCollaborator(sellerId)).thenReturn(0L);
        when(meetingPostAnalysisRepository.getAverageFeelingByCollaborator(sellerId)).thenReturn(null);
        when(companyRepository.getCompanyById(companyId)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> useCase.execute(null, null, null, null, pageable));
    }

    @Test
    void shouldPassFiltersToRepository() {
        when(repository.getSellers("Ana", "ana@acme.com", companyId, true, pageable))
                .thenReturn(new PageImpl<>(List.of()));

        useCase.execute("Ana", "ana@acme.com", companyId, true, pageable);

        verify(repository).getSellers("Ana", "ana@acme.com", companyId, true, pageable);
    }
}
