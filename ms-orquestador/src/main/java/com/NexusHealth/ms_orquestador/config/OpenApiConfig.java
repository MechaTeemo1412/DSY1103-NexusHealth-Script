package com.NexusHealth.ms_orquestador.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI configOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Orquestador API")
                .description("Cordinación del ecosistema de NexusHealth")
                .version("1.0.0"));
    }
}
