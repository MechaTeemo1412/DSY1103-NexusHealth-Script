package com.NexusHealth.ms_agenda.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI configurarOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Agenda API")
                        .description("Microservicio encargado de la gestión de disponibilidad y citas médicas en NexusHealth")
                        .version("1.0.0")
                );
    }
}