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
/**
 * Motor central de mensajería para el ecosistema NexusHealth.
 * <p>
 * Este servicio (Service Layer) es responsable de gestionar el despacho de comunicaciones 
 * externas hacia los pacientes (ej. recordatorios de citas vía WhatsApp o SMS). 
 * Su diseño asegura que la información transmitida cumpla con las normativas de 
 * privacidad, aislando la lógica de envío de la lógica médica central. Además, 
 * implementa un patrón de trazabilidad estricto, reportando cada intento de envío 
 * al sistema central de auditoría.
 * </p>
 * * @author Equipo Desarrollo NexusHealth
 * @version 1.0
 * @since 2026-05-18
 */
@Service
@Slf4j
public class NotificacionService {
    /**
     * Repositorio JPA para la persistencia del historial de mensajería.
     * Permite mantener un registro local en Oracle Cloud de todos los mensajes gestionados.
     */
    @Autowired
    private NotificacionRepository notificacionRepository;
    /**
     * Cliente HTTP declarativo (OpenFeign) utilizado para reportar síncronamente 
     * el resultado de cada transacción de mensajería al microservicio de auditoría.
     */
    @Autowired
    private AuditoriaClient auditoriaClient;
    /**
     * Procesa, simula el envío y registra una notificación dirigida a un paciente.
     * <p>
     * El flujo de este método consta de tres etapas críticas:
     * <ol>
     * <li><b>Preparación:</b> Mapea los datos del DTO entrante a la entidad de persistencia local.</li>
     * <li><b>Despacho (Mock API):</b> Simula la latencia y la tasa de éxito (90%) de una integración 
     * real con la API de WhatsApp Business. Captura excepciones para evitar la caída del servicio.</li>
     * <li><b>Trazabilidad Distribuida:</b> Persiste el resultado en la base de datos local e 
     * informa inmediatamente al ms-auditoria mediante una petición Feign.</li>
     * </ol>
     * </p>
     * * @param dto Objeto de transferencia de datos que contiene el destinatario y el mensaje (sin datos clínicos sensibles).
     * @return Notificacion La entidad persistida con su estado final de envío (ENVIADO, REINTENTANDO o FALLIDO).
     */
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
        Notificacion guardada = notificacionRepository.save(logNotif);

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

