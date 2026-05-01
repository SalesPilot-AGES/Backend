package com.salespilot.api.application.assembler;

import java.util.List;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.repository.CompanyRepository;

public class CollaboratorAssembler {
  private final CompanyRepository companyRepository;

  public CollaboratorAssembler(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
  }

  public CollaboratorResponseDTO toDTO(Collaborator collaborator) {
    CompanyResponseDTO companyDto = companyRepository.getCompanyById(collaborator.getCompanyId())
        .map(CompanyResponseDTO::from)
        .orElseThrow(() -> new CompanyNotFoundException(collaborator.getCompanyId()));

    return new CollaboratorResponseDTO(
        collaborator.getId(),
        collaborator.getCompanyId(),
        collaborator.getName(),
        collaborator.getRole(),
        collaborator.getEmail(),
        collaborator.getPhone(),
        collaborator.isActive(),
        collaborator.getAverageFeeling(),
        collaborator.getPreferences(),
        collaborator.getCreatedAt(),
        collaborator.getUpdatedAt(),
        companyDto);

  }

  public List<CollaboratorResponseDTO> toDTOList(List<Collaborator> collaborators) {
    return collaborators.stream()
        .map(this::toDTO)
        .toList();
  }
}
