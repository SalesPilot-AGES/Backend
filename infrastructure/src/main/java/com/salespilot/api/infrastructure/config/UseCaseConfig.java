package com.salespilot.api.infrastructure.config;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.assembler.MeetingAssembler;
import com.salespilot.api.application.queryservice.ClientQueryService;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.CompanyQueryService;
import com.salespilot.api.application.usecase.GetAllCompaniesUseCase;
import com.salespilot.api.application.usecase.GetCompanyByIdUseCase;
import com.salespilot.api.application.usecase.GetSystemStatusUseCase;
import com.salespilot.api.application.usecase.PostCompanyUseCase;
import com.salespilot.api.application.usecase.UpdateCompanyUseCase;
import com.salespilot.api.application.usecase.PostCollaboratorUseCase;
import com.salespilot.api.application.usecase.EditCollaboratorUseCase;
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
    public GetCompanyByIdUseCase getCompanyByIdUseCase(CompanyQueryService companyQueryService) {
        return new GetCompanyByIdUseCase(companyQueryService);
    }

    @Bean
    public UpdateCompanyUseCase updateCompanyUseCase(CompanyRepository repository) {
        return new UpdateCompanyUseCase(repository);
    }

    @Bean
    public CollaboratorAssembler collaboratorAssembler() {
        return new CollaboratorAssembler();
    }

    @Bean
    public PostCollaboratorUseCase postCollaboratorUseCase(CollaboratorRepository collaboratorRepository, CompanyQueryService companyQueryService, CollaboratorAssembler assembler) {
        return new PostCollaboratorUseCase(collaboratorRepository, companyQueryService, assembler);
    }

    @Bean
    public EditCollaboratorUseCase editCollaboratorUseCase(CollaboratorRepository repository, CompanyQueryService companyQueryService, CollaboratorQueryService collaboratorQueryService, CollaboratorAssembler assembler) {
        return new EditCollaboratorUseCase(repository, companyQueryService, collaboratorQueryService, assembler);
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
    public GetAllMeetingsUseCase getAllMeetingsUseCase(MeetingRepository repository, CollaboratorQueryService collaboratorQueryService, ClientQueryService clientQueryService, MeetingAssembler assembler) {
        return new GetAllMeetingsUseCase(repository, collaboratorQueryService, clientQueryService, assembler);
    }

    @Bean
    public MeetingAssembler meetingAssembler() { return new MeetingAssembler(); }
}
