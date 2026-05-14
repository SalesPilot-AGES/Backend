package com.salespilot.api.application.assembler;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;
import org.springframework.stereotype.Component;

@Component
public class CollaboratorAssembler {
    public CollaboratorResponseDTO toDTO(Collaborator collaborator, Company company){
        return new CollaboratorResponseDTO(
                collaborator.getId(),
                collaborator.getCompanyId(),
                collaborator.getName(),
                collaborator.getRole(),
                collaborator.getEmail(),
                collaborator.getPhone(),
                collaborator.isActive(),
                collaborator.getPreferences(),
                collaborator.getCreatedAt(),
                collaborator.getUpdatedAt(),
                CompanyResponseDTO.from(company)
        );
    }
}
