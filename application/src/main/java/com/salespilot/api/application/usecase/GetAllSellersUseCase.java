package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.dto.SellerResponseDTO;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.domain.repository.MeetingPostAnalysisRepository;
import com.salespilot.api.domain.repository.MeetingRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class GetAllSellersUseCase {

    private final CollaboratorRepository repository;
    private final CompanyRepository companyRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingPostAnalysisRepository meetingPostAnalysisRepository;

    public GetAllSellersUseCase(CollaboratorRepository repository, CompanyRepository companyRepository, MeetingRepository meetingRepository, MeetingPostAnalysisRepository meetingPostAnalysisRepository) {
        this.repository = repository;
        this.companyRepository = companyRepository;
        this.meetingRepository = meetingRepository;
        this.meetingPostAnalysisRepository = meetingPostAnalysisRepository;
    }

    public Page<SellerResponseDTO> execute(String name, String email, UUID companyId, Boolean active, Pageable pageable) {
        
        return repository.getSellers(name, email, companyId, active, pageable)
                .map(c -> {
                    Long totalMeetings = meetingRepository.getTotalMeetingsByCollaborator(c.getId());
                    Double averageFeeling = meetingPostAnalysisRepository.getAverageFeelingByCollaborator(c.getId());
                    return new SellerResponseDTO(
                        c.getId(),
                        c.getCompanyId(),
                        c.getName(),
                        c.getRole(),
                        c.getEmail(),
                        c.getPhone(),
                        c.isActive(),
                        averageFeeling,
                        totalMeetings,
                        c.getPreferences(),
                        c.getCreatedAt(),
                        c.getUpdatedAt(),
                        companyRepository.getCompanyById(c.getCompanyId())
                                .map(CompanyResponseDTO::from)
                                .orElseThrow(() -> new CompanyNotFoundException(c.getCompanyId()))
                    );    
                });
    }
}
