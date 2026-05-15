package com.NexusHealth.ms_notificaciones.feignclient;

import com.NexusHealth.ms_notificaciones.dto.LogAuditoriaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="ms-auditoria",url="http://localhost:8085/api/v1/auditoria")
public interface AuditoriaClient {
    @PostMapping("/registro")
    void registrarEvento(@RequestBody LogAuditoriaDTO notificacion);
}
