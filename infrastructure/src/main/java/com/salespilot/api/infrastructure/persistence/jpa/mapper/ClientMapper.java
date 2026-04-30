package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Client;
import com.salespilot.api.infrastructure.persistence.jpa.entity.Clients;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public Client toDomain(Clients clients) {
        return new Client(
                clients.getId(),
                clients.getCompany().getId(),
                clients.getCollaborator().getId(),
                clients.getName(),
                clients.getClientCompanyName(),
                clients.getSector(),
                clients.getOverallSentiment(),
                clients.getCreatedAt(),
                clients.getUpdatedAt()
        );
    }
}
