package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.ClientResponseDTO;
import com.salespilot.api.application.dto.MeetingResponseDTO;
import com.salespilot.api.application.dto.SellerMeetingResponseDTO;
import com.salespilot.api.application.exception.ClientNotFoundException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
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

    public GetAllMeetingsUseCase(MeetingRepository meetingRepository, ClientRepository clientRepository, CollaboratorRepository collaboratorRepository) {
        this.repository = meetingRepository;
        this.clientRepository = clientRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

    public Page<MeetingResponseDTO> execute(String title, String clientCompanyName, UUID collaboratorId, Pageable pageable) {
        return repository.getAllMeetings(title, clientCompanyName, collaboratorId, pageable)
                .map(m -> {
                    SellerMeetingResponseDTO seller = collaboratorRepository.getCollaboratorById(m.getCollaboratorId())
                            .map(SellerMeetingResponseDTO::from)
                            .orElseThrow(() -> new CollaboratorNotFoundException(m.getCollaboratorId()));

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
    }
}
