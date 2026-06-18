package com.salespilot.api.infrastructure;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Minimal Spring Boot application used as the context anchor for infrastructure
 * integration tests. Spring Boot 4 removed @DataJpaTest, so repository tests use
 * @SpringBootTest(webEnvironment = NONE) with this class as the bootstrap entry point.
 * It is scoped to test sources and never deployed.
 */
@SpringBootApplication(
        scanBasePackages = "com.salespilot.api.infrastructure.persistence"
)
public class InfrastructureTestApplication {
}
