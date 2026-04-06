package com.salespilot.api.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.salespilot.api.application.usecase.GetEnterpriseByIdUseCase;
import com.salespilot.api.application.usecase.GetSystemStatusUseCase;
import com.salespilot.api.domain.repository.EnterpriseRepository;
import com.salespilot.api.domain.repository.SystemStatusRepository;

@Configuration
public class UseCaseConfig {

    @Bean
    public GetSystemStatusUseCase getSystemStatusUseCase(SystemStatusRepository repository) {
        return new GetSystemStatusUseCase(repository);
    }

    @Bean
    public GetEnterpriseByIdUseCase getEnterpriseByIdUseCase(EnterpriseRepository repository){
        return new GetEnterpriseByIdUseCase(repository);
    }
}
