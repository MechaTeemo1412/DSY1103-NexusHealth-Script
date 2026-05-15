package com.NexusHealth.ms_orquestador.feignclient;

import com.NexusHealth.ms_orquestador.dto.NotificacionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-notificaciones", url = "http://localhost:8084/api/v1/notificaciones")
public interface NotificacionesClient {
    @PostMapping("/enviar")
    void enviarMensaje(@RequestBody NotificacionDTO dto);
}
