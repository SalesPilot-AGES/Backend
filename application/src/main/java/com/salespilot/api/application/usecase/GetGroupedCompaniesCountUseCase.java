package com.salespilot.api.application.usecase;

import java.util.ArrayList;
import java.util.List;

import com.salespilot.api.application.dto.DonutChartDataResponseDTO;
import com.salespilot.api.application.dto.GroupCompanyCountResponseDTO;
import com.salespilot.api.domain.repository.CompanyRepository;

public class GetGroupedCompaniesCountUseCase {
    private final CompanyRepository companyRepository;

    public GetGroupedCompaniesCountUseCase(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    public GroupCompanyCountResponseDTO execute(){
        List<DonutChartDataResponseDTO> donutDtoList = new ArrayList<>();
        DonutChartDataResponseDTO active = new DonutChartDataResponseDTO("Ativas", companyRepository.countCompanyActiveGrouped(true));
        DonutChartDataResponseDTO inactive = new DonutChartDataResponseDTO("Inativas", companyRepository.countCompanyActiveGrouped(false));
        donutDtoList.add(active);
        donutDtoList.add(inactive);

        GroupCompanyCountResponseDTO dto = new GroupCompanyCountResponseDTO(
            donutDtoList,
            companyRepository.countAll()
        );

        return dto;
    }
}
