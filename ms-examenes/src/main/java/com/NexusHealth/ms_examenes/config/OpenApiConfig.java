package com.NexusHealth.ms_examenes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI configOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Exámenes API")
                .description("Microservicio encargado de monitorear y mapear el cambio del estado de un exámen")
                .version("1.0.0"));
    }
}
