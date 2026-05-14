package com.salespilot.api.application.usecase;

import com.salespilot.api.application.assembler.SellerWithMeetingsAssembler;
import com.salespilot.api.application.dto.ClientResponseDTO;
import com.salespilot.api.application.dto.LatestMeetingsResponseDTO;
import com.salespilot.api.application.dto.SellerWithMeetingsResponseDTO;
import com.salespilot.api.application.exception.InvalidCollaboratorRoleException;
import com.salespilot.api.application.queryservice.ClientQueryService;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.CompanyQueryService;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.MeetingRepository;

import java.util.UUID;

public class GetSellerByIdUseCase {
    private final CollaboratorQueryService collaboratorQueryService;
    private final ClientQueryService clientQueryService;
    private final CompanyQueryService companyQueryService;
    private final MeetingRepository meetingRepository;
    private final SellerWithMeetingsAssembler assembler;

    public GetSellerByIdUseCase(CollaboratorQueryService collaboratorQueryService, ClientQueryService clientQueryService, CompanyQueryService companyQueryService, MeetingRepository meetingRepository, SellerWithMeetingsAssembler assembler) {
        this.collaboratorQueryService = collaboratorQueryService;
        this.clientQueryService = clientQueryService;
        this.companyQueryService = companyQueryService;
        this.meetingRepository = meetingRepository;
        this.assembler = assembler;
    }

    public SellerWithMeetingsResponseDTO execute(UUID id) {
        Collaborator collaborator = collaboratorQueryService.getOrThrowById(id);

        if(collaborator.getRole() != CollaboratorRole.SELLER) {
            throw new InvalidCollaboratorRoleException(collaborator.getRole(), CollaboratorRole.SELLER);
        }

        Company company = companyQueryService.getOrThrowById(collaborator.getCompanyId());

        LatestMeetingsResponseDTO latestMeeting = meetingRepository
                .getLatestMeetingByCollaborator(collaborator.getId())
                .map(m -> new LatestMeetingsResponseDTO(
                        m.getId(),
                        m.getTitle(),
                        m.getStatus(),
                        m.getStartedAt(),
                        m.getDurationSeconds(),
                        ClientResponseDTO.from(clientQueryService.getOrThrowById(m.getClientId()))
                ))
                .orElse(null);

        Long totalMeetings = meetingRepository.getTotalMeetingsByCollaborator(collaborator.getId());

        return assembler.toDTO(collaborator, totalMeetings, latestMeeting, company);
    }
}
