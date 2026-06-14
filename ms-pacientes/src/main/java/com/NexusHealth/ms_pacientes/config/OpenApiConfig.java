package com.NexusHealth.ms_pacientes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI configuarOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Pacientes Api")
                .description("Microservicio para extracción,Validación de los datos y su formato")
                .version("1.0.0"));
    }
}
