package com.NexusHealth.ms_orquestador.service;

import com.NexusHealth.ms_orquestador.dto.CitaDTO;
import com.NexusHealth.ms_orquestador.dto.LogAuditoriaDTO;
import com.NexusHealth.ms_orquestador.dto.NotificacionDTO;
import com.NexusHealth.ms_orquestador.feignclient.AgendaClient;
import com.NexusHealth.ms_orquestador.feignclient.AuditoriaClient;
import com.NexusHealth.ms_orquestador.feignclient.NotificacionesClient;
import com.NexusHealth.ms_orquestador.model.Alerta;
import com.NexusHealth.ms_orquestador.model.EstadoAlerta;
import com.NexusHealth.ms_orquestador.repository.AlertaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 * Servicio central del componente Orquestador (ms-orquestador) dentro de NexusHealth.
 * <p>
 * Esta clase asume la responsabilidad crítica de gobernar el flujo de negocio interconectado
 * mediante la orquestación de llamadas síncronas hacia componentes distribuidos independientes
 * (Agenda, Notificaciones y Auditoría). Actúa bajo el enfoque de patrones de integración empresarial
 * para resolver la sincronización lógica del procesamiento diario de citas médicas.
 * </p>
 *
 * @author Equipo Desarrollo NexusHealth
 * @version 1.0
 * @since 2026-05-18
 */
@Service
@Slf4j
public class AlertaService {
    /**
     * Cliente HTTP declarativo (OpenFeign) para interactuar síncronamente con el ms-agenda.
     */
    @Autowired private AgendaClient agendaClient;
    /**
     * Cliente HTTP declarativo (OpenFeign) para derivar las órdenes de envío al ms-notificaciones.
     */
    @Autowired private NotificacionesClient notificacionesClient;
    /**
     * Cliente HTTP declarativo (OpenFeign) para centralizar y emitir logs operativos al ms-auditoria.
     */
    @Autowired private AuditoriaClient auditoriaClient;
    /**
     * Repositorio JPA encargado de persistir el historial de ejecuciones en Oracle Cloud (perfil LOW).
     */
    @Autowired private AlertaRepository alertaRepository;
    /**
     * Ejecuta de forma automática el flujo batch diario de procesamiento e integración de citas médicas.
     * <p>
     * Este método se gatilla mediante un hilo autónomo gestionado por el pool de Spring Scheduler 
     * a las 08:00 AM cada día. Realiza las siguientes etapas orquestadas en cadena:
     * </p>
     * <ol>
     * <li>Inicializa el registro maestro de la Alerta en estado 'PROCESANDO' dentro de Oracle Cloud.</li>
     * <li>Consume los bloques de la agenda programados para las próximas 24 horas mediante ms-agenda.</li>
     * <li>Itera la colección de citas obtenidas, enmascara datos clínicos sensibles y despacha
     * las solicitudes de SMS/WhatsApp a ms-notificaciones bajo la Ley chilena 20.584.</li>
     * <li>Actualiza el estado final del proceso y remite un DTO consolidado al ms-auditoria.</li>
     * </ol>
     * <p>
     * En caso de interrupción en la red o caídas de componentes, el bloque catch intercepta el pánico,
     * degrada el estado del proceso a 'FALLIDA' de forma resiliente y reporta la anomalía a auditoría.
     * </p>
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void ejecutarRevisionDiaria() {
        procesarAlertasDeAgenda();
    }

    public Alerta procesarAlertasDeAgenda() {
        log.info("Iniciando proceso orquestador: Búsqueda de citas próximas");
        Alerta alerta = new Alerta();
        alerta.setFechaEjecucion(LocalDateTime.now());

        try {
            // 1. Obtener citas médicas en ventana de 24 horas vía Feign
            List<CitaDTO> citas = agendaClient.obtenerCitasProximas24h();

            if (citas.isEmpty()) {
                log.info("No se encontraron citas para notificar hoy.");
                alerta.setEstado(EstadoAlerta.SIN_CITAS);
                alerta.setCitasProcesadas(0);
            } else {
                // 2. Mapear y delegar notificaciones enmascaradas protegiendo la ficha clínica
                for (CitaDTO cita : citas) {
                    // Cumplimiento Ley 20.584: Mensaje genérico sin diagnóstico
                    String fechaFormateada = cita.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                    String mensaje = String.format("Estimado/a, le recordamos su cita con el Dr. %s para el %s. Responda 1 para confirmar o 2 para anular.",
                            cita.getMedicoNombre(), fechaFormateada);

                    notificacionesClient.enviarMensaje(new NotificacionDTO(cita.getPacienteRut(), mensaje));
                    log.info("Orden de notificación enviada para RUT: {}", cita.getPacienteRut());
                }
                alerta.setEstado(EstadoAlerta.PROCESADA);
                alerta.setCitasProcesadas(citas.size());
            }

            // 3. Informar éxito de la transacción distribuida al concentrador de auditoría
            auditoriaClient.registrarEvento(new LogAuditoriaDTO(
                    "ms-orquestador", "PROCESO_DIARIO", "EXITO", "Se procesaron " + alerta.getCitasProcesadas() + " notificaciones.", LocalDateTime.now()
            ));

        } catch (Exception e) {
            log.error("Fallo crítico en la orquestación: {}", e.getMessage());
            alerta.setEstado(EstadoAlerta.FALLIDA);
            alerta.setCitasProcesadas(0);

            // Informar fallo técnico a ms-auditoria para análisis
            auditoriaClient.registrarEvento(new LogAuditoriaDTO(
                    "ms-orquestador", "PROCESO_DIARIO", "FALLA", "Error de integración: " + e.getMessage(), LocalDateTime.now()
            ));
        }
        return alertaRepository.save(alerta);
    }
}
