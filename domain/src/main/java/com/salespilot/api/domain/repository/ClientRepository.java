package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Client;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {
    Optional<Client> findById(UUID id);
}
