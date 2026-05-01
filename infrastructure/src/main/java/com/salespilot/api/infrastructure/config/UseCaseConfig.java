package com.salespilot.api.infrastructure.config;

import com.salespilot.api.application.usecase.GetAllCompaniesUseCase;
import com.salespilot.api.application.usecase.GetCompanyByIdUseCase;
import com.salespilot.api.application.usecase.GetSystemStatusUseCase;
import com.salespilot.api.application.usecase.PostCompanyUseCase;
import com.salespilot.api.application.usecase.UpdateCompanyUseCase;
import com.salespilot.api.application.usecase.PostCollaboratorUseCase;
import com.salespilot.api.application.usecase.EditCollaboratorUseCase;
import com.salespilot.api.application.usecase.EditSellerUseCase;
import com.salespilot.api.application.usecase.GetCollaboratorByIdUseCase;
import com.salespilot.api.application.usecase.GetAllManagersUseCase;
import com.salespilot.api.application.usecase.GetAllMeetingsUseCase;

import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.domain.repository.MeetingRepository;
import com.salespilot.api.domain.repository.ClientRepository;
import com.salespilot.api.domain.repository.SystemStatusRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public GetSystemStatusUseCase getSystemStatusUseCase(SystemStatusRepository repository) {
        return new GetSystemStatusUseCase(repository);
    }

    @Bean
    public PostCompanyUseCase postCompanyUseCase(CompanyRepository repository) {
        return new PostCompanyUseCase(repository);
    }

    @Bean
    public GetAllCompaniesUseCase getAllCompaniesUseCase(CompanyRepository repository) {
        return new GetAllCompaniesUseCase(repository);
    }

    @Bean
    public GetCompanyByIdUseCase getCompanyByIdUseCase(CompanyRepository repository) {
        return new GetCompanyByIdUseCase(repository);
    }

    @Bean
    public UpdateCompanyUseCase updateCompanyUseCase(CompanyRepository repository) {
        return new UpdateCompanyUseCase(repository);
    }

    @Bean
    public PostCollaboratorUseCase postCollaboratorUseCase(CollaboratorRepository collaboratorRepository, CompanyRepository companyRepository) {
        return new PostCollaboratorUseCase(collaboratorRepository, companyRepository);
    }

    @Bean
    public EditCollaboratorUseCase editCollaboratorUseCase(CollaboratorRepository collaboratorRepository, CompanyRepository companyRepository) {
        return new EditCollaboratorUseCase(collaboratorRepository, companyRepository);
    }

    @Bean
    public GetCollaboratorByIdUseCase getCollaboratorByIdUseCase(CollaboratorRepository collaboratorRepository, CompanyRepository companyRepository) {
        return new GetCollaboratorByIdUseCase(collaboratorRepository, companyRepository);
    }

    @Bean
    public GetAllManagersUseCase getAllManagersUseCase(CollaboratorRepository repository, CompanyRepository companyRepository) {
        return new GetAllManagersUseCase(repository, companyRepository);
    }

    @Bean
    public GetAllMeetingsUseCase getAllMeetingsUseCase(MeetingRepository repository, ClientRepository clientRepository, CollaboratorRepository collaboratorRepository) {
        return new GetAllMeetingsUseCase(repository, clientRepository, collaboratorRepository);
    }

    @Bean
    public EditSellerUseCase editSellerUseCase(CollaboratorRepository collaboratorRepository, CompanyRepository companyRepository) {
        return new EditSellerUseCase(collaboratorRepository, companyRepository);
    }
}
