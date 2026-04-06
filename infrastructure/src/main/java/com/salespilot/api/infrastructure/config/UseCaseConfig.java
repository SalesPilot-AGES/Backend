package com.salespilot.api.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.salespilot.api.application.usecase.GetSystemStatusUseCase;
import com.salespilot.api.application.usecase.GetAllEnterprisesUseCase;
import com.salespilot.api.domain.repository.EnterpriseRepository;
import com.salespilot.api.domain.repository.SystemStatusRepository;

@Configuration
public class UseCaseConfig {

    @Bean
    public GetSystemStatusUseCase getSystemStatusUseCase(SystemStatusRepository repository) {
        return new GetSystemStatusUseCase(repository);
    }

    @Bean
    public GetAllEnterprisesUseCase GetAllEnterprisesUseCase(EnterpriseRepository repository){
        return new GetAllEnterprisesUseCase(repository);
    }
}
