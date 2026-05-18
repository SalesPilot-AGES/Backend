package com.salespilot.api.infrastructure.config;

import com.salespilot.api.application.usecase.EditCollaboratorUseCase;
import com.salespilot.api.application.usecase.GetAllManagersUseCase;
import com.salespilot.api.application.usecase.GetAllMeetingsUseCase;
import com.salespilot.api.application.usecase.GetAllSellersUseCase;
import com.salespilot.api.application.usecase.GetAverageMeetingDurationPerMonthUseCase;
import com.salespilot.api.application.usecase.GetCollaboratorByIdUseCase;
import com.salespilot.api.application.usecase.PostCollaboratorUseCase;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.salespilot.api.application.usecase.GetAllCompaniesUseCase;
import com.salespilot.api.application.usecase.GetCompanyByIdUseCase;
import com.salespilot.api.application.usecase.GetSellerByIdUseCase;
import com.salespilot.api.application.usecase.GetSystemStatusUseCase;
import com.salespilot.api.application.usecase.PostCompanyUseCase;
import com.salespilot.api.application.usecase.UpdateCompanyUseCase;
import com.salespilot.api.application.usecase.GetMeetingContextAndMetadataUseCase;
import com.salespilot.api.application.usecase.GetMeetingInsightUseCase;
import com.salespilot.api.application.usecase.GetMeetingPostAnalysisUseCase;

import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.domain.repository.MeetingRepository;
import com.salespilot.api.domain.repository.ClientRepository;
import com.salespilot.api.domain.repository.SystemStatusRepository;
import com.salespilot.api.domain.repository.MeetingPreAnalysisRepository;
import com.salespilot.api.domain.repository.MeetingRealtimeInsightRepository;
import com.salespilot.api.domain.repository.MeetingPostAnalysisRepository;

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
    public GetAllSellersUseCase getAllSellersUseCase(CollaboratorRepository repository, CompanyRepository companyRepository, MeetingRepository meetingRepository, MeetingPostAnalysisRepository meetingPostAnalysisRepository) {
        return new GetAllSellersUseCase(repository, companyRepository, meetingRepository, meetingPostAnalysisRepository);
    }
    

    @Bean
    public GetSellerByIdUseCase getSellerByIdUseCase(CollaboratorRepository collaboratorRepository, CompanyRepository companyRepository, ClientRepository clientRepository, MeetingRepository meetingRepository) {
        return new GetSellerByIdUseCase(collaboratorRepository, companyRepository, clientRepository, meetingRepository);
    }

    @Bean
    public GetAllMeetingsUseCase getAllMeetingsUseCase(MeetingRepository repository, MeetingPostAnalysisRepository meetingPostAnalysisRepository, ClientRepository clientRepository, CollaboratorRepository collaboratorRepository) {
        return new GetAllMeetingsUseCase(repository, meetingPostAnalysisRepository, clientRepository, collaboratorRepository);
    }

    @Bean
    public GetMeetingContextAndMetadataUseCase getMeetingContextAndMetadataUseCase(MeetingRepository repository, CollaboratorRepository collaboratorRepository, ClientRepository clientRepository, MeetingPreAnalysisRepository meetingPreAnalysisRepository) {
        return new GetMeetingContextAndMetadataUseCase(repository, collaboratorRepository, clientRepository, meetingPreAnalysisRepository);
    }

    @Bean
    public GetMeetingPostAnalysisUseCase getMeetingPostAnalysisUseCase(MeetingPostAnalysisRepository meetingPostAnalysisRepository) {
        return new GetMeetingPostAnalysisUseCase(meetingPostAnalysisRepository);
    }

    @Bean
    public GetMeetingInsightUseCase getMeetingInsightUseCase(MeetingRealtimeInsightRepository meetingRealtimeInsightRepository, MeetingRepository meetingRepository) {
        return new GetMeetingInsightUseCase(meetingRealtimeInsightRepository, meetingRepository);
    }

    @Bean
    public GetAverageMeetingDurationPerMonthUseCase getAverageMeetingDurationPerMonthUseCase(MeetingRepository meetingRepository) {
        return new GetAverageMeetingDurationPerMonthUseCase(meetingRepository);
    }
}
