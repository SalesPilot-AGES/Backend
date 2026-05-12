package com.salespilot.api.application.usecase;

import com.salespilot.api.application.assembler.SellerAssembler;
import com.salespilot.api.application.dto.SellerResponseDTO;
import com.salespilot.api.application.queryservice.CompanyQueryService;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.MeetingPostAnalysisRepository;
import com.salespilot.api.domain.repository.MeetingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class GetAllSellersUseCase {

    private final CollaboratorRepository repository;
    private final CompanyQueryService companyQueryService;
    private final MeetingRepository meetingRepository;
    private final MeetingPostAnalysisRepository meetingPostAnalysisRepository;
    private final SellerAssembler assembler;

    public GetAllSellersUseCase(CollaboratorRepository repository, CompanyQueryService companyQueryService, MeetingRepository meetingRepository, MeetingPostAnalysisRepository meetingPostAnalysisRepository, SellerAssembler assembler) {
        this.repository = repository;
        this.companyQueryService = companyQueryService;
        this.meetingRepository = meetingRepository;
        this.meetingPostAnalysisRepository = meetingPostAnalysisRepository;
        this.assembler = assembler;
    }

    public Page<SellerResponseDTO> execute(String name, String email, UUID companyId, Boolean active, Pageable pageable) {

        return repository.getSellers(name, email, companyId, active, pageable)
                .map(c -> {
                    Long totalMeetings = meetingRepository.getTotalMeetingsByCollaborator(c.getId());
                    Double averageFeeling = meetingPostAnalysisRepository.getAverageFeelingByCollaborator(c.getId());
                    Company company = companyQueryService.getOrThrowCompanyById(c.getCompanyId());

                    return assembler.toDTO(
                            c,
                            averageFeeling,
                            totalMeetings,
                            company
                    );
                });
    }
}
