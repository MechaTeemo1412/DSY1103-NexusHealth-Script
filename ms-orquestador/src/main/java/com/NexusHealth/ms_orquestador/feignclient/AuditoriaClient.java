package com.NexusHealth.ms_orquestador.feignclient;

import com.NexusHealth.ms_orquestador.dto.LogAuditoriaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-auditoria", url = "${auditoria.url}")
public interface AuditoriaClient {
    @PostMapping("/registro")
    void registrarEvento(@RequestBody LogAuditoriaDTO log);
}
