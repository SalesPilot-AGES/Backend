package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Client;
import com.salespilot.api.infrastructure.persistence.jpa.entity.ClientEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public Client toDomain(ClientEntity clientEntity) {
        return new Client(
                clientEntity.getId(),
                clientEntity.getCompany().getId(),
                clientEntity.getCollaborator().getId(),
                clientEntity.getName(),
                clientEntity.getClientCompanyName(),
                clientEntity.getSector(),
                clientEntity.getOverallSentiment(),
                clientEntity.getCreatedAt(),
                clientEntity.getUpdatedAt()
        );
    }
}
