package com.salespilot.api.presentation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SalesPilot API")
                        .description("API de gerenciamento de empresas e colaboradores da plataforma SalesPilot.")
                        .version("0.0.1")
                        .contact(new Contact()
                                .name("SalesPilot Team")
                                .email("salespilot@ages.pucrs.br")))
                .tags(List.of(
                        new Tag().name("System").description("Verificação de status da API"),
                        new Tag().name("Companies").description("Gerenciamento de empresas"),
                        new Tag().name("Collaborators").description("Gerenciamento de colaboradores")
                ));
    }
}
