package com.salespilot.api.application.usecase;

import java.util.List;

import com.salespilot.api.application.dto.CompanyStatusCountDTO;
import com.salespilot.api.application.dto.GroupCompanyCountResponseDTO;
import com.salespilot.api.domain.model.CompanyStatusCount;
import com.salespilot.api.domain.repository.CompanyRepository;

public class GetGroupedCompaniesCountUseCase {
    private final CompanyRepository companyRepository;

    public GetGroupedCompaniesCountUseCase(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    public GroupCompanyCountResponseDTO execute() {

        List<CompanyStatusCountDTO> data = companyRepository
            .countCompaniesGroupedByStatus()
            .stream()
            .map(this::mapToDto)
            .toList();

        Long total = data.stream()
            .mapToLong(CompanyStatusCountDTO::value)
            .sum();

        return new GroupCompanyCountResponseDTO(data, total);
    }

    private CompanyStatusCountDTO mapToDto(CompanyStatusCount item) {
        return new CompanyStatusCountDTO(item.active() ? "Ativas" : "Inativas", item.total());
    }
}
