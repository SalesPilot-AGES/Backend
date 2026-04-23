package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {
    Optional<Client> findById(UUID id);
    List<Client> findByCompanyId(UUID companyId);
    List<Client> findByCollaboratorId(UUID collaboratorId);
    Client save(Client client);
}
