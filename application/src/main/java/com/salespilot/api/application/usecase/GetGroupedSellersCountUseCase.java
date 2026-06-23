package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.DashboardStatusCountDTO;
import com.salespilot.api.application.dto.GroupStatusCountResponseDTO;
import com.salespilot.api.domain.model.StatusCount;
import com.salespilot.api.domain.repository.CollaboratorRepository;

import java.util.List;

public class GetGroupedSellersCountUseCase {
    private final CollaboratorRepository collaboratorRepository;

    public GetGroupedSellersCountUseCase(CollaboratorRepository collaboratorRepository) {
        this.collaboratorRepository = collaboratorRepository;
    }

    public GroupStatusCountResponseDTO execute() {

        List<DashboardStatusCountDTO> data = collaboratorRepository
                .countSellersGroupedByStatus()
                .stream()
                .map(this::mapToDto)
                .toList();

        Long total = data.stream()
                .mapToLong(DashboardStatusCountDTO::value)
                .sum();

        return new GroupStatusCountResponseDTO(data, total);
    }

    private DashboardStatusCountDTO mapToDto(StatusCount item) {
        return new DashboardStatusCountDTO(item.active() ? "Ativos" : "Inativos", item.total());
    }
}
