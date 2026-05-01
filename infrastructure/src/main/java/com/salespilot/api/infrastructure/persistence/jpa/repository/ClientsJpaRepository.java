package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.Clients;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientsJpaRepository extends JpaRepository<Clients, UUID> {
}
