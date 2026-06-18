package com.salespilot.api.presentation.handler;

import com.salespilot.api.application.exception.*;
import com.salespilot.api.domain.enums.CollaboratorRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @RestController
    static class TestController {
        @GetMapping("/test/tax-id")
        void taxId() { throw new TaxIdAlreadyExists(); }

        @GetMapping("/test/collaborator-exists")
        void collaboratorExists() { throw new CollaboratorAlreadyExistsException(UUID.randomUUID(), "x@x.com"); }

        @GetMapping("/test/company-not-found")
        void companyNotFound() { throw new CompanyNotFoundException(UUID.randomUUID()); }

        @GetMapping("/test/collaborator-not-found")
        void collaboratorNotFound() { throw new CollaboratorNotFoundException(UUID.randomUUID()); }

        @GetMapping("/test/client-not-found")
        void clientNotFound() { throw new ClientNotFoundException(UUID.randomUUID()); }

        @GetMapping("/test/meeting-not-found")
        void meetingNotFound() { throw new MeetingNotFoundException(UUID.randomUUID()); }

        @GetMapping("/test/post-analysis-not-found")
        void postAnalysisNotFound() { throw new MeetingPostAnalysisNotFoundException(UUID.randomUUID()); }

        @GetMapping("/test/invalid-role")
        void invalidRole() { throw new InvalidCollaboratorRoleException(CollaboratorRole.SELLER, CollaboratorRole.MANAGER); }

        @GetMapping("/test/invalid-period")
        void invalidPeriod() { throw new InvalidPeriodException(LocalDate.now(), LocalDate.now().plusDays(1)); }

        @GetMapping("/test/invalid-credentials")
        void invalidCredentials() { throw new InvalidCredentialsException(); }

        @GetMapping("/test/refresh-token-invalid")
        void refreshTokenInvalid() { throw new RefreshTokenInvalidException(); }

        @GetMapping("/test/forbidden")
        void forbidden() { throw new ForbiddenException(); }
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturn409WhenTaxIdAlreadyExists() throws Exception {
        mockMvc.perform(get("/test/tax-id")).andExpect(status().isConflict());
    }

    @Test
    void shouldReturn409WhenCollaboratorAlreadyExists() throws Exception {
        mockMvc.perform(get("/test/collaborator-exists")).andExpect(status().isConflict());
    }

    @Test
    void shouldReturn404WhenCompanyNotFound() throws Exception {
        mockMvc.perform(get("/test/company-not-found")).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenCollaboratorNotFound() throws Exception {
        mockMvc.perform(get("/test/collaborator-not-found")).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenClientNotFound() throws Exception {
        mockMvc.perform(get("/test/client-not-found")).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenMeetingNotFound() throws Exception {
        mockMvc.perform(get("/test/meeting-not-found")).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenMeetingPostAnalysisNotFound() throws Exception {
        mockMvc.perform(get("/test/post-analysis-not-found")).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn409WhenInvalidCollaboratorRole() throws Exception {
        mockMvc.perform(get("/test/invalid-role")).andExpect(status().isConflict());
    }

    @Test
    void shouldReturn400WhenInvalidPeriod() throws Exception {
        mockMvc.perform(get("/test/invalid-period")).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn401WhenInvalidCredentials() throws Exception {
        mockMvc.perform(get("/test/invalid-credentials")).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn401WhenRefreshTokenInvalid() throws Exception {
        mockMvc.perform(get("/test/refresh-token-invalid")).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn403WhenForbidden() throws Exception {
        mockMvc.perform(get("/test/forbidden")).andExpect(status().isForbidden());
    }

    @Test
    void shouldReturn422WhenMethodArgNotValid() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        var response = handler.handleMethodArgumentNotValidException();
        assertThat(response.getStatusCode().value()).isEqualTo(422);
    }

    @Test
    void shouldReturn422WhenHandlerMethodValidation() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        var response = handler.handleHandlerMethodValidationException();
        assertThat(response.getStatusCode().value()).isEqualTo(422);
    }
}
