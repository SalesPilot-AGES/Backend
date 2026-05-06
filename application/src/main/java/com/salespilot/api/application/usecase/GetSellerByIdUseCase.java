package com.salespilot.api.application.usecase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.salespilot.api.application.dto.ClientResponseDTO;
import com.salespilot.api.application.dto.SellerWithMeetingsResponseDTO;
import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.dto.LatestMeetingsResponseDTO;
import com.salespilot.api.application.exception.ClientNotFoundException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.application.exception.InvalidCollaboratorRoleException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.ClientRepository;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.domain.repository.MeetingRepository;

public class GetSellerByIdUseCase {
    private final CollaboratorRepository collaboratorRepository;
    private final CompanyRepository companyRepository;
    private final ClientRepository clientRepository;
    private final MeetingRepository meetingRepository;

    public GetSellerByIdUseCase(CollaboratorRepository collaboratorRepository, CompanyRepository companyRepository, ClientRepository clientRepository, MeetingRepository meetingRepository) {
        this.collaboratorRepository = collaboratorRepository;
        this.companyRepository = companyRepository;
        this.clientRepository = clientRepository;
        this.meetingRepository = meetingRepository;
    }

    public SellerWithMeetingsResponseDTO execute(UUID id) {
        Collaborator collaborator = collaboratorRepository.getCollaboratorById(id).orElseThrow(
            () -> new CollaboratorNotFoundException(id)
        );

        if(collaborator.getRole() != CollaboratorRole.SELLER) {
            throw new InvalidCollaboratorRoleException(collaborator.getRole(), CollaboratorRole.SELLER);
        }

        UUID companyId = collaborator.getCompanyId();

        CompanyResponseDTO companyDto = companyRepository.getCompanyById(companyId)
                .map(CompanyResponseDTO::from)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        Optional<Meeting> latestMeetings = meetingRepository.getLatestMeetingByCollaborator(collaborator.getId());
            
        List<LatestMeetingsResponseDTO> latestMeetingsResponse =
            latestMeetings
            .stream()
            .map(m -> new LatestMeetingsResponseDTO(
                m.getId(),
                m.getTitle(),
                m.getStatus(),
                m.getStartedAt(),
                m.getDurationSeconds(),
                clientRepository.findById(m.getClientId())
                    .map(ClientResponseDTO::from)
                    .orElseThrow(() -> new ClientNotFoundException(m.getClientId()))
        )).collect(Collectors.toList());

        Long totalMeetings = meetingRepository.getTotalMeetingsByCollaborator(collaborator.getId());

        return new SellerWithMeetingsResponseDTO (
                collaborator.getId(),
                collaborator.getCompanyId(),
                collaborator.getName(),
                collaborator.getRole(),
                collaborator.getEmail(),
                collaborator.isActive(),
                collaborator.getPhone(),
                collaborator.getPreferences(),
                collaborator.getAverageFeeling(),
                totalMeetings,
                latestMeetingsResponse,
                collaborator.getCreatedAt(),
                collaborator.getUpdatedAt(),
                companyDto
        );
    }
}
