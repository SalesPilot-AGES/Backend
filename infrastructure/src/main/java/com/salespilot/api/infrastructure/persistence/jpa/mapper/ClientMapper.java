package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Client;
import com.salespilot.api.infrastructure.persistence.jpa.entity.Clients;

public class ClientMapper {
    public Client toDomain(Clients entity) {
        return new Client(
            entity.getId(), 
            entity.getCompany().getId(), 
            entity.getCollaborator().getId(), 
            entity.getName(), 
            entity.getClientCompanyName(), 
            entity.getSector(), 
            entity.getOverallSentiment(), 
            entity.getCreatedAt(), entity.getUpdatedAt()
        );
    } 
}
