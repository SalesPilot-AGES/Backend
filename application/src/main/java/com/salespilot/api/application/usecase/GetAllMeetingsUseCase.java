package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.*;
import com.salespilot.api.application.exception.ClientNotFoundException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.repository.ClientRepository;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.MeetingPostAnalysisRepository;
import com.salespilot.api.domain.repository.MeetingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class GetAllMeetingsUseCase {
    private final MeetingRepository repository;
    private final MeetingPostAnalysisRepository meetingPostAnalysisRepository;
    private final ClientRepository clientRepository;
    private final CollaboratorRepository collaboratorRepository;

    public GetAllMeetingsUseCase(MeetingRepository meetingRepository, MeetingPostAnalysisRepository meetingPostAnalysisRepository, ClientRepository clientRepository, CollaboratorRepository collaboratorRepository) {
        this.repository = meetingRepository;
        this.meetingPostAnalysisRepository = meetingPostAnalysisRepository;
        this.clientRepository = clientRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

    public MeetingPageResponseDTO execute(String title, String clientCompanyName, UUID collaboratorId, Pageable pageable) {
        Page<MeetingResponseDTO> page = repository.getAllMeetings(title, clientCompanyName, collaboratorId, pageable)
                .map(m -> {
                    Collaborator sellerObject = collaboratorRepository.getCollaboratorById(m.getCollaboratorId())
                            .orElseThrow(() -> new CollaboratorNotFoundException(m.getCollaboratorId()));
                    
                    SellerMeetingResponseDTO seller = SellerMeetingResponseDTO.from(sellerObject);

                    ClientMeetingResponseDTO client = clientRepository.findById(m.getClientId())
                            .map(ClientMeetingResponseDTO::from)
                            .orElseThrow(() -> new ClientNotFoundException(m.getClientId()));

                    return new MeetingResponseDTO(
                            m.getId(),
                            m.getTitle(),
                            seller,
                            client,
                            m.getMeetingType(),
                            m.getScheduledFor(),
                            m.getStartedAt(),
                            m.getEndedAt(),
                            m.getDurationSeconds(),
                            m.getStatus()
                    );
                });

        return MeetingPageResponseDTO.from(page, new SummaryResponseDTO(
                repository.getTotalMeetings(),
                repository.getAverageDurationSeconds(),
                meetingPostAnalysisRepository.getAverageSuccessRate()
        ), "Reuniões listadas com sucesso");
    }
}
