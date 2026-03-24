package com.example.animalgame.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("API AnimalGame")
                        .description("Documentação da API do sistema Jogo do Bicho")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Nadson Silva")
                                .email("nadson@email.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Projeto acadêmico IFAM"));
    }
}