package com.salespilot.api.application.usecase;

import com.salespilot.api.application.assembler.MeetingAssembler;
import com.salespilot.api.application.dto.MeetingPageResponseDTO;
import com.salespilot.api.application.dto.MeetingResponseDTO;
import com.salespilot.api.application.dto.SummaryResponseDTO;
import com.salespilot.api.application.queryservice.ClientQueryService;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.domain.entity.Client;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.repository.MeetingPostAnalysisRepository;
import com.salespilot.api.domain.repository.MeetingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class GetAllMeetingsUseCase {
    private final MeetingRepository repository;
    private final CollaboratorQueryService collaboratorQueryService;
    private final ClientQueryService clientQueryService;
    private final MeetingPostAnalysisRepository meetingPostAnalysisRepository;
    private final MeetingAssembler assembler;

    public GetAllMeetingsUseCase(MeetingRepository repository, CollaboratorQueryService collaboratorQueryService, ClientQueryService clientQueryService, MeetingPostAnalysisRepository meetingPostAnalysisRepository, MeetingAssembler assembler) {
        this.repository = repository;
        this.collaboratorQueryService = collaboratorQueryService;
        this.clientQueryService = clientQueryService;
        this.meetingPostAnalysisRepository = meetingPostAnalysisRepository;
        this.assembler = assembler;
    }

    public MeetingPageResponseDTO execute(String title, String clientCompanyName, UUID collaboratorId, Pageable pageable) {
        Page<MeetingResponseDTO> page = repository.getAllMeetings(title, clientCompanyName, collaboratorId, pageable)
                .map(m -> {
                    Collaborator seller = collaboratorQueryService.getOrThrowById(m.getCollaboratorId());

                    Client client = clientQueryService.getOrThrowById(m.getClientId());

                    return assembler.toDTO(
                            m,
                            seller,
                            client
                    );
                });

        return MeetingPageResponseDTO.from(page, new SummaryResponseDTO(
                repository.getTotalMeetings(),
                repository.getAverageDurationSeconds(),
                meetingPostAnalysisRepository.getAverageSuccessRate()
        ));
    }
}
