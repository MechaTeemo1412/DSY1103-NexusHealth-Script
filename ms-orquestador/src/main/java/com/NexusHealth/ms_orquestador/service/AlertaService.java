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

@Service
@Slf4j
public class AlertaService {
    @Autowired
    private AgendaClient agendaClient;
    @Autowired private NotificacionesClient notificacionesClient;
    @Autowired private AuditoriaClient auditoriaClient;
    @Autowired private AlertaRepository alertaRepository;

    // Se ejecuta automáticamente todos los días a las 08:00 AM
    @Scheduled(cron = "0 0 8 * * *")
    public void ejecutarRevisionDiaria() {
        procesarAlertasDeAgenda();
    }

    public Alerta procesarAlertasDeAgenda() {
        log.info("Iniciando proceso orquestador: Búsqueda de citas próximas");
        Alerta alerta = new Alerta();
        alerta.setFechaEjecucion(LocalDateTime.now());

        try {
            // 1. Consultar a ms-agenda
            List<CitaDTO> citas = agendaClient.obtenerCitasProximas24h();

            if (citas.isEmpty()) {
                log.info("No se encontraron citas para notificar hoy.");
                alerta.setEstado(EstadoAlerta.SIN_CITAS);
                alerta.setCitasProcesadas(0);
            } else {
                // 2. Iterar y enviar a ms-notificaciones
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

            // 3. Informar éxito a ms-auditoria
            auditoriaClient.registrarEvento(new LogAuditoriaDTO(
                    "ms-orquestador", "PROCESO_DIARIO", "EXITO", "Se procesaron " + alerta.getCitasProcesadas() + " notificaciones.", LocalDateTime.now()
            ));

        } catch (Exception e) {
            log.error("Fallo crítico en la orquestación: {}", e.getMessage());
            alerta.setEstado(EstadoAlerta.FALLIDA);
            alerta.setCitasProcesadas(0);

            // Informar fallo a ms-auditoria
            auditoriaClient.registrarEvento(new LogAuditoriaDTO(
                    "ms-orquestador", "PROCESO_DIARIO", "FALLA", "Error de integración: " + e.getMessage(), LocalDateTime.now()
            ));
        }

        // 4. Persistir registro histórico local
        return alertaRepository.save(alerta);
    }
}
