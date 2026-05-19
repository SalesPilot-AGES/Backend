package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.SystemStatusResponseDTO;
import com.salespilot.api.domain.entity.SystemStatus;
import com.salespilot.api.domain.repository.SystemStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetSystemStatusUseCaseTest {

    @Mock
    private SystemStatusRepository repository;

    @InjectMocks
    private GetSystemStatusUseCase useCase;

    @Test
    void shouldReturnSystemStatusWithCorrectFields() {
        when(repository.getCurrentStatus()).thenReturn(new SystemStatus("UP", "2026-05-19T10:00:00"));

        SystemStatusResponseDTO result = useCase.execute();

        assertEquals("UP", result.currentStatus());
        assertEquals("2026-05-19T10:00:00", result.checkedAt());
    }

    @Test
    void shouldDelegateToRepositoryOnEveryCall() {
        when(repository.getCurrentStatus()).thenReturn(new SystemStatus("UP", "2026-05-19T10:00:00"));

        useCase.execute();
        useCase.execute();

        verify(repository, times(2)).getCurrentStatus();
    }
}
