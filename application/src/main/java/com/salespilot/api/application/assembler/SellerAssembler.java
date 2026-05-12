package com.salespilot.api.application.assembler;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.dto.SellerResponseDTO;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;
import org.springframework.stereotype.Component;

@Component
public class SellerAssembler {
    public SellerResponseDTO toDTO(Collaborator collaborator, Double averageFeeling, Long totalMeetings, Company company) {
        return new SellerResponseDTO(
                collaborator.getId(),
                collaborator.getCompanyId(),
                collaborator.getName(),
                collaborator.getRole(),
                collaborator.getEmail(),
                collaborator.getPhone(),
                collaborator.isActive(),
                averageFeeling,
                totalMeetings,
                collaborator.getPreferences(),
                collaborator.getCreatedAt(),
                collaborator.getUpdatedAt(),
                CompanyResponseDTO.from(company)
        );
    }
}
