package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.DashboardStatusCountDTO;
import com.salespilot.api.application.dto.GroupStatusCountResponseDTO;
import com.salespilot.api.domain.model.StatusCount;
import com.salespilot.api.domain.repository.CompanyRepository;

import java.util.List;

public class GetGroupedCompaniesCountUseCase {
    private final CompanyRepository companyRepository;

    public GetGroupedCompaniesCountUseCase(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    public GroupStatusCountResponseDTO execute() {

        List<DashboardStatusCountDTO> data = companyRepository
            .countCompaniesGroupedByStatus()
            .stream()
            .map(this::mapToDto)
            .toList();

        Long total = data.stream()
            .mapToLong(DashboardStatusCountDTO::value)
            .sum();

        return new GroupStatusCountResponseDTO(data, total);
    }

    private DashboardStatusCountDTO mapToDto(StatusCount item) {
        return new DashboardStatusCountDTO(item.active() ? "Ativas" : "Inativas", item.total());
    }
}
