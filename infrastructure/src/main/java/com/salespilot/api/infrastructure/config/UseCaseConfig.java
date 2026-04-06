package com.salespilot.api.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.salespilot.api.application.usecase.GetCompanyByIdUseCase;
import com.salespilot.api.application.usecase.GetSystemStatusUseCase;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.domain.repository.SystemStatusRepository;

@Configuration
public class UseCaseConfig {

    @Bean
    public GetSystemStatusUseCase getSystemStatusUseCase(SystemStatusRepository repository) {
        return new GetSystemStatusUseCase(repository);
    }

    @Bean
    public GetCompanyByIdUseCase getCompanyByIdUseCase(CompanyRepository repository){
        return new GetCompanyByIdUseCase(repository);
    }
}
