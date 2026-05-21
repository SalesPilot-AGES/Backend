package com.salespilot.api.infrastructure.config;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.assembler.MeetingAssembler;
import com.salespilot.api.application.assembler.MeetingContextMetadataAssembler;
import com.salespilot.api.application.assembler.SellerAssembler;
import com.salespilot.api.application.assembler.SellerWithMeetingsAssembler;
import com.salespilot.api.application.queryservice.ClientQueryService;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.CompanyQueryService;
import com.salespilot.api.application.queryservice.MeetingQueryService;
import com.salespilot.api.application.usecase.EditCollaboratorUseCase;
import com.salespilot.api.application.usecase.GetAllCompaniesUseCase;
import com.salespilot.api.application.usecase.GetAllManagersUseCase;
import com.salespilot.api.application.usecase.GetAllMeetingsUseCase;
import com.salespilot.api.application.usecase.GetAllSellersUseCase;
import com.salespilot.api.application.usecase.GetCardMetricsUseCase;
import com.salespilot.api.application.usecase.GetCollaboratorByIdUseCase;
import com.salespilot.api.application.usecase.GetCompanyByIdUseCase;
import com.salespilot.api.application.usecase.GetGroupedCompaniesCountUseCase;
import com.salespilot.api.application.usecase.GetMeetingContextAndMetadataUseCase;
import com.salespilot.api.application.usecase.GetMeetingInsightUseCase;
import com.salespilot.api.application.usecase.GetMeetingPostAnalysisUseCase;
import com.salespilot.api.application.usecase.GetSellerByIdUseCase;
import com.salespilot.api.application.usecase.GetSystemStatusUseCase;
import com.salespilot.api.application.usecase.PostCollaboratorUseCase;
import com.salespilot.api.application.usecase.PostCompanyUseCase;
import com.salespilot.api.application.usecase.UpdateCompanyUseCase;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.domain.repository.MeetingPostAnalysisRepository;
import com.salespilot.api.domain.repository.MeetingPreAnalysisRepository;
import com.salespilot.api.domain.repository.MeetingRealtimeInsightRepository;
import com.salespilot.api.domain.repository.MeetingRepository;
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
    public PostCollaboratorUseCase postCollaboratorUseCase(CollaboratorRepository collaboratorRepository, CompanyQueryService companyQueryService, CollaboratorAssembler assembler) {
        return new PostCollaboratorUseCase(collaboratorRepository, companyQueryService, assembler);
    }

    @Bean
    public EditCollaboratorUseCase editCollaboratorUseCase(CollaboratorRepository repository, CollaboratorQueryService collaboratorQueryService, CompanyQueryService companyQueryService, CollaboratorAssembler assembler) {
        return new EditCollaboratorUseCase(repository, collaboratorQueryService, companyQueryService, assembler);
    }

    @Bean
    public GetCollaboratorByIdUseCase getCollaboratorByIdUseCase(CollaboratorQueryService collaboratorQueryService, CompanyQueryService companyQueryService, CollaboratorAssembler assembler) {
        return new GetCollaboratorByIdUseCase(collaboratorQueryService, companyQueryService, assembler);
    }

    @Bean
    public GetAllManagersUseCase getAllManagersUseCase(CollaboratorRepository repository, CompanyQueryService companyQueryService, CollaboratorAssembler assembler) {
        return new GetAllManagersUseCase(repository, companyQueryService, assembler);
    }

    @Bean
    public GetAllSellersUseCase getAllSellersUseCase(CollaboratorRepository repository, CompanyQueryService companyQueryService, MeetingRepository meetingRepository, MeetingPostAnalysisRepository meetingPostAnalysisRepository, SellerAssembler assembler) {
        return new GetAllSellersUseCase(repository, companyQueryService, meetingRepository, meetingPostAnalysisRepository, assembler);
    }
    

    @Bean
    public GetSellerByIdUseCase getSellerByIdUseCase(CollaboratorQueryService collaboratorQueryService, ClientQueryService clientQueryService, CompanyQueryService companyQueryService, MeetingRepository meetingRepository, SellerWithMeetingsAssembler assembler) {
        return new GetSellerByIdUseCase(collaboratorQueryService, clientQueryService, companyQueryService, meetingRepository, assembler);
    }

    @Bean
    public GetAllMeetingsUseCase getAllMeetingsUseCase(MeetingRepository repository, CollaboratorQueryService collaboratorQueryService, ClientQueryService clientQueryService, MeetingPostAnalysisRepository meetingPostAnalysisRepository, MeetingAssembler assembler) {
        return new GetAllMeetingsUseCase(repository, collaboratorQueryService, clientQueryService, meetingPostAnalysisRepository, assembler);
    }

    @Bean
    public GetMeetingContextAndMetadataUseCase getMeetingContextAndMetadataUseCase(MeetingQueryService meetingQueryService, CollaboratorQueryService collaboratorQueryService, ClientQueryService clientQueryService, MeetingPreAnalysisRepository meetingPreAnalysisRepository, MeetingContextMetadataAssembler assembler) {
        return new GetMeetingContextAndMetadataUseCase(meetingQueryService, collaboratorQueryService, clientQueryService, meetingPreAnalysisRepository, assembler);
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
    public GetGroupedCompaniesCountUseCase getGroupedCompaniesCountUseCase(CompanyRepository companyRepository) {
        return new GetGroupedCompaniesCountUseCase(companyRepository);
    }

    @Bean
    public GetCardMetricsUseCase getCardMetricsUseCase(CompanyRepository companyRepository, MeetingRepository meetingRepository, CollaboratorRepository collaboratorRepository) {
        return new GetCardMetricsUseCase(companyRepository, meetingRepository, collaboratorRepository);
    }
}
