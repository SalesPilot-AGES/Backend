package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.domain.entity.Client;
import com.salespilot.api.domain.repository.ClientRepository;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.ClientMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ClientRepositoryImpl implements ClientRepository {
    private final ClientsJpaRepository clientsJpaRepository;
    private final ClientMapper mapper;

    public ClientRepositoryImpl(ClientsJpaRepository clientsJpaRepository, ClientMapper mapper) {
        this.clientsJpaRepository = clientsJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Client> findById(UUID id) {
        return clientsJpaRepository.findById(id).map(mapper::toDomain);
    }
}
