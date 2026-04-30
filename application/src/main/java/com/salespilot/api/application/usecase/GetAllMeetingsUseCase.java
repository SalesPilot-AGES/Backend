package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.*;
import com.salespilot.api.application.exception.ClientNotFoundException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.repository.ClientRepository;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.MeetingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class GetAllMeetingsUseCase {
    private final MeetingRepository repository;
    private final ClientRepository clientRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final int TEMPORARY_DEFAULT_SUCCESS_RATE = 0;

    public GetAllMeetingsUseCase(MeetingRepository meetingRepository, ClientRepository clientRepository, CollaboratorRepository collaboratorRepository) {
        this.repository = meetingRepository;
        this.clientRepository = clientRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

    public MeetingPageResponseDTO execute(String title, String clientCompanyName, UUID collaboratorId, Pageable pageable) {
        Page<MeetingResponseDTO> page = repository.getAllMeetings(title, clientCompanyName, collaboratorId, pageable)
                .map(m -> {
                    Collaborator sellerObject = collaboratorRepository.getCollaboratorById(m.getCollaboratorId())
                            .orElseThrow(() -> new CollaboratorNotFoundException(m.getCollaboratorId()));
                    
                    SellerMeetingResponseDTO seller = SellerMeetingResponseDTO.from(sellerObject);

                    ClientResponseDTO client = clientRepository.findById(m.getClientId())
                            .map(ClientResponseDTO::from)
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
                TEMPORARY_DEFAULT_SUCCESS_RATE
        ));
    }
}
