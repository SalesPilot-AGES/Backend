package com.salespilot.api.application.usecase;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.salespilot.api.application.dto.ClientResponseDTO;
import com.salespilot.api.application.dto.SellerResponseDTO;
import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.dto.LatestMeetingsResponseDTO;
import com.salespilot.api.application.exception.ClientNotFoundException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.repository.ClientRepository;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;

public class GetSellerByIdUseCase {
    private final CollaboratorRepository collaboratorRepository;
    private final CompanyRepository companyRepository;
    private final ClientRepository clientRepository;

    public GetSellerByIdUseCase(CollaboratorRepository collaboratorRepository, CompanyRepository companyRepository, ClientRepository clientRepository) {
        this.collaboratorRepository = collaboratorRepository;
        this.companyRepository = companyRepository;
        this.clientRepository = clientRepository;
    }

    public SellerResponseDTO execute(UUID id) {
        Collaborator collaborator = collaboratorRepository.getCollaboratorById(id).orElseThrow(
            () -> new CollaboratorNotFoundException(id)
        );

        UUID companyId = collaborator.getCompanyId();

        CompanyResponseDTO companyDto = companyRepository.getCompanyById(companyId)
                .map(CompanyResponseDTO::from)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        List<LatestMeetingsResponseDTO> latestMeetings = collaborator.getMeetings().stream()
            .map(m -> new LatestMeetingsResponseDTO(
                m.getId(),
                m.getTitle(),
                m.getStatus(),
                m.getStartedAt(),
                m.getDurationSeconds(),
                clientRepository.findById(m.getClientId()).map(ClientResponseDTO::from).orElseThrow(() -> new ClientNotFoundException(m.getClientId()))
        )).collect(Collectors.toList());

        return new SellerResponseDTO(
                collaborator.getId(),
                collaborator.getCompanyId(),
                collaborator.getName(),
                collaborator.getRole(),
                collaborator.getEmail(),
                collaborator.isActive(),
                collaborator.getPhone(),
                collaborator.getPreferences(),
                collaborator.getAverageFeeling(),
                collaborator.getTotalMeetings(),
                latestMeetings,
                collaborator.getCreatedAt(),
                collaborator.getUpdatedAt(),
                companyDto
        );
    }
}
