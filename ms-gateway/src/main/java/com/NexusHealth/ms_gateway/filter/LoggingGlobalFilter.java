package com.NexusHealth.ms_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(LoggingGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().name();

        log.info("[Gateway Pre-Filter] Petición entrante: Método={} Ruta={}", method, path);

        long startTime = System.currentTimeMillis();

        // Continuar con la cadena de filtros y luego ejecutar el Post-Filter
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            log.info("[Gateway Post-Filter] Respuesta enviada para Ruta={} | Tiempo de procesamiento: {}ms", path, duration);
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Alta prioridad para que sea el primero en interceptar
    }
}
