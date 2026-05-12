package com.salespilot.api.application.assembler;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.dto.LatestMeetingsResponseDTO;
import com.salespilot.api.application.dto.SellerWithMeetingsResponseDTO;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;

public class SellerWithMeetingsAssembler {
    public SellerWithMeetingsResponseDTO toDTO (Collaborator seller, Long totalMeetings, LatestMeetingsResponseDTO latestMeeting, Company company) {
        return new SellerWithMeetingsResponseDTO(
                seller.getId(),
                seller.getCompanyId(),
                seller.getName(),
                seller.getRole(),
                seller.getEmail(),
                seller.isActive(),
                seller.getPhone(),
                seller.getPreferences(),
                seller.getAverageFeeling(),
                totalMeetings,
                latestMeeting,
                seller.getCreatedAt(),
                seller.getUpdatedAt(),
                CompanyResponseDTO.from(company)
        );
    }
}
