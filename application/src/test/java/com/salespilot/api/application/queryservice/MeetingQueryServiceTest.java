package com.salespilot.api.application.queryservice;

import com.salespilot.api.application.exception.MeetingNotFoundException;
import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.repository.MeetingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingQueryServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private MeetingQueryService meetingQueryService;

    private final UUID id = UUID.randomUUID();

    @Test
    void shouldReturnEntityWhenFound() {
        Meeting meeting = mock(Meeting.class);
        when(meetingRepository.getMeetingById(id)).thenReturn(Optional.of(meeting));

        Meeting result = meetingQueryService.getOrThrowById(id);

        assertNotNull(result);
        assertSame(meeting, result);
    }

    @Test
    void shouldThrowWhenNotFound() {
        when(meetingRepository.getMeetingById(id)).thenReturn(Optional.empty());

        assertThrows(MeetingNotFoundException.class, () -> meetingQueryService.getOrThrowById(id));
    }
}
