package com.salespilot.api.application.queryservice;

import com.salespilot.api.application.exception.ClientNotFoundException;
import com.salespilot.api.domain.entity.Client;
import com.salespilot.api.domain.repository.ClientRepository;
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
class ClientQueryServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientQueryService clientQueryService;

    private final UUID id = UUID.randomUUID();

    @Test
    void shouldReturnEntityWhenFound() {
        Client client = mock(Client.class);
        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        Client result = clientQueryService.getOrThrowById(id);

        assertNotNull(result);
        assertSame(client, result);
    }

    @Test
    void shouldThrowWhenNotFound() {
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientQueryService.getOrThrowById(id));
    }
}
