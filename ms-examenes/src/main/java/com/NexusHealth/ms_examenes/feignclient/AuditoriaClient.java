package com.NexusHealth.ms_examenes.feignclient;

import com.NexusHealth.ms_examenes.dto.NotificacionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-auditoria", url = "url = \"${auditoria.url}")
public interface AuditoriaClient {
    @PostMapping("/registro")
    void registrarEvento(@RequestBody NotificacionDTO notificacion);
}
