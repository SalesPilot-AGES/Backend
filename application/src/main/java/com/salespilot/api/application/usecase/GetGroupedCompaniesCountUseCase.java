package com.salespilot.api.application.usecase;

import java.util.List;

import com.salespilot.api.application.dto.CompanyStatusCountDTO;
import com.salespilot.api.application.dto.GroupCompanyCountResponseDTO;
import com.salespilot.api.domain.repository.CompanyRepository;

public class GetGroupedCompaniesCountUseCase {
    private final CompanyRepository companyRepository;

    public GetGroupedCompaniesCountUseCase(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    public GroupCompanyCountResponseDTO execute() {

        List<CompanyStatusCountDTO> data = companyRepository.countCompaniesGroupedByStatus()
            .stream()
            .map(this::mapToDto)
            .toList();

        Long total = data.stream()
            .mapToLong(CompanyStatusCountDTO::value)
            .sum();

        return new GroupCompanyCountResponseDTO(
            data,
            total
        );
    }

    private CompanyStatusCountDTO mapToDto(Object[] item) {

        boolean active = (Boolean) item[0];
        long count = ((Number) item[1]).longValue();

        return new CompanyStatusCountDTO(
                active ? "Ativas" : "Inativas",
                count
        );
    }
}
