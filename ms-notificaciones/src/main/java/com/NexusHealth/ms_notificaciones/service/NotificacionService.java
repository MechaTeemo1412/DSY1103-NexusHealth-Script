package com.NexusHealth.ms_notificaciones.service;

import com.NexusHealth.ms_notificaciones.dto.LogAuditoriaDTO;
import com.NexusHealth.ms_notificaciones.dto.NotificacionDTO;
import com.NexusHealth.ms_notificaciones.feignclient.AuditoriaClient;
import com.NexusHealth.ms_notificaciones.model.EstadoEnvio;
import com.NexusHealth.ms_notificaciones.model.Notificacion;
import com.NexusHealth.ms_notificaciones.repository.NotificacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class NotificacionService {
    @Autowired
    private NotificacionRepository repository;

    @Autowired
    private AuditoriaClient auditoriaClient;

    public Notificacion procesarEnvio(NotificacionDTO dto) {
        log.info("Iniciando despacho de notificación para: {}", dto.getDestinatario());

        // 1. Preparamos el registro
        Notificacion logNotif = new Notificacion();
        logNotif.setDestinatario(dto.getDestinatario());
        logNotif.setMensaje(dto.getMensaje());
        logNotif.setFechaEnvio(LocalDateTime.now());

        try {
            // Simulador de éxito de la API de WhatsApp
            boolean exitoEnvio = Math.random() > 0.1;

            if(exitoEnvio) {
                logNotif.setEstado(EstadoEnvio.ENVIADO);
                log.info("Mensaje enviado exitosamente a WhatsApp");
            } else {
                logNotif.setEstado(EstadoEnvio.REINTENTANDO);
                log.warn("Fallo en API de WhatsApp. Programando reintento.");
            }
        } catch (Exception e) {
            logNotif.setEstado(EstadoEnvio.FALLIDO);
            log.error("Error crítico en el motor de mensajería: {}", e.getMessage());
        }

        // 2. Persistencia real
        Notificacion guardada = repository.save(logNotif);

        // 3. Comunicación con auditoría mediante Feign
        auditoriaClient.registrarEvento(new LogAuditoriaDTO(
                "ms-notificaciones",
                "ENVIO_WHATSAPP",
                guardada.getEstado().toString(),
                "Mensaje a: " + guardada.getDestinatario(),
                LocalDateTime.now()
        ));

        return guardada;
    }
}

