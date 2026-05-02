package com.salespilot.api.infrastructure.config;

import com.salespilot.api.application.usecase.*;

import com.salespilot.api.domain.repository.*;
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
    public GetMeetingContextAndMetadataUseCase getMeetingContextAndMetadataUseCase(MeetingRepository repository, CollaboratorRepository collaboratorRepository, ClientRepository clientRepository, MeetingPreAnalysisRepository meetingPreAnalysisRepository) {
        return new GetMeetingContextAndMetadataUseCase(repository, collaboratorRepository, clientRepository, meetingPreAnalysisRepository);
    }

    @Bean
    public GetMeetingPostAnalysisUseCase getMeetingPostAnalysisUseCase(MeetingPostAnalysisRepository meetingPostAnalysisRepository) {
        return new GetMeetingPostAnalysisUseCase(meetingPostAnalysisRepository);
    }
}
