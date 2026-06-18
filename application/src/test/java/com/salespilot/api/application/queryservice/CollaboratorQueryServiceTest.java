package com.salespilot.api.application.queryservice;

import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.repository.CollaboratorRepository;
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
class CollaboratorQueryServiceTest {

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @InjectMocks
    private CollaboratorQueryService collaboratorQueryService;

    private final UUID id = UUID.randomUUID();

    @Test
    void shouldReturnEntityWhenFound() {
        Collaborator collaborator = mock(Collaborator.class);
        when(collaboratorRepository.getCollaboratorById(id)).thenReturn(Optional.of(collaborator));

        Collaborator result = collaboratorQueryService.getOrThrowById(id);

        assertNotNull(result);
        assertSame(collaborator, result);
    }

    @Test
    void shouldThrowWhenNotFound() {
        when(collaboratorRepository.getCollaboratorById(id)).thenReturn(Optional.empty());

        assertThrows(CollaboratorNotFoundException.class, () -> collaboratorQueryService.getOrThrowById(id));
    }
}
