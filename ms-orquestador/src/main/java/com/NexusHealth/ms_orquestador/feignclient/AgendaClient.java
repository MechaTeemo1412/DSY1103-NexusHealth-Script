package com.NexusHealth.ms_orquestador.feignclient;

import com.NexusHealth.ms_orquestador.dto.CitaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ms-agenda", url = "http://ms-agenda:8082/api/v1/agenda")
public interface AgendaClient {
    @GetMapping("/proximas-24h")
    List<CitaDTO> obtenerCitasProximas24h();
}
