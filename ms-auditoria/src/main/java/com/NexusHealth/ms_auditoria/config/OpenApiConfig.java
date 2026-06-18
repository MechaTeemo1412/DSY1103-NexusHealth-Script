package com.NexusHealth.ms_auditoria.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI configOpenAPi(){
        return new OpenAPI().info(new Info()
        .title("Auditoría API")
                .description("Receptor central de trazabilidad para registrar Logs y generar reportes")
                .version("1.0.0"));
    }
}
