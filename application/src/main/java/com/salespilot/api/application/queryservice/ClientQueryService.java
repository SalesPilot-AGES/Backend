package com.salespilot.api.application.queryservice;

import com.salespilot.api.application.exception.ClientNotFoundException;
import com.salespilot.api.domain.entity.Client;
import com.salespilot.api.domain.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientQueryService {
    private final ClientRepository clientRepository;

    public ClientQueryService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClientById(UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }
}
