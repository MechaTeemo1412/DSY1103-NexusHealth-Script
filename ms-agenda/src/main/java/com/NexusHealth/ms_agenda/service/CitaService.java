package com.NexusHealth.ms_agenda.service;


import com.NexusHealth.ms_agenda.feignclient.AuditoriaClient;
import com.NexusHealth.ms_agenda.dto.EstadoCitaDTO;
import com.NexusHealth.ms_agenda.dto.NotificacionDTO;
import com.NexusHealth.ms_agenda.model.Cita;
import com.NexusHealth.ms_agenda.model.EstadoCita;
import com.NexusHealth.ms_agenda.repository.CitaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Servicio central del microservicio de Agenda (ms-agenda) dentro de NexusHealth.
 * <p>
 * Representa la capa de lógica de negocio (Service Layer) responsable de la gestión,
 * filtrado y transición de estados de los bloques horarios y citas médicas. 
 * Implementa reglas de tiempo críticas (ventanas de 24 horas) y mantiene una 
 * coreografía estricta con el microservicio de auditoría para garantizar la 
 * trazabilidad distribuida de cualquier lectura o mutación en la base de datos.
 * </p>
 *
 * @author Equipo Desarrollo NexusHealth
 * @version 1.0
 * @since 2026-05-18
 */
@Service
@Slf4j
public class CitaService {
    /**
     * Repositorio JPA para la persistencia y consulta de las citas médicas en Oracle Cloud.
     */
    @Autowired
    private CitaRepository citaRepository;
    /**
     * Cliente HTTP declarativo (OpenFeign) utilizado para reportar eventos de lectura 
     * y actualización al concentrador central de auditoría.
     */
    @Autowired
    private AuditoriaClient auditoriaClient;
    /**
     * Recupera todas las citas médicas programadas dentro de una ventana temporal estricta de 24 horas.
     * <p>
     * <b>Regla de Negocio:</b> Este método es consumido principalmente por el Orquestador 
     * durante su proceso batch diario. Solo extrae las citas que se encuentran activas 
     * bajo el estado {@link EstadoCita#PROGRAMADA} y cuya fecha de atención esté comprendida 
     * exactamente entre el instante actual y las próximas 24 horas.
     * Al finalizar la extracción, despacha un evento síncrono de trazabilidad al ms-auditoria.
     * </p>
     *
     * @return List&lt;Cita&gt; Colección de citas médicas que cumplen con los criterios de tiempo y estado.
     */
    public List<Cita> obtenerCitasProximas24Horas() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime manana = ahora.plusHours(24); // Ventana exacta de 24 horas

        log.info("Buscando citas PROGRAMADAS entre {} y {}", ahora, manana);

        List<Cita> citas = citaRepository.findByFechaHoraBetweenAndEstado(ahora, manana, EstadoCita.PROGRAMADA);

        auditoriaClient.registrarEvento(new NotificacionDTO(
                "ms-agenda", "CONSULTA_CITAS_24H", "EXITO", "Citas encontradas: " + citas.size(), LocalDateTime.now()
        ));
        return citas;
    }

    /**
     * Procesa la mutación del estado clínico de una cita médica específica.
     * <p>
     * Este método es el encargado de consolidar las respuestas asíncronas de los pacientes 
     * (por ejemplo, cuando un paciente responde "1" o "2" vía WhatsApp). Actualiza el 
     * estado de la cita respetando la inmutabilidad de la fecha original asignada.
     * </p>
     *
     * @param idCita Identificador único (Primary Key) de la cita médica a modificar.
     * @param dto Objeto de transferencia de datos que encapsula el nuevo estado objetivo.
     * @return Cita La entidad de la cita médica actualizada y persistida en Oracle Cloud.
     * @throws RuntimeException Si el {@code idCita} proporcionado no existe en los registros de la base de datos.
     */
    public Cita actualizarEstado(Long idCita, EstadoCitaDTO dto) {
        log.info("Actualizando estado de la cita ID: {} al estado: {}", idCita, dto.getNuevoEstado());

        // 1. Busca la cita
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + idCita));

        // 2. Regla de negocio: Actualiza el estado, la fecha queda intacta
        cita.setEstado(dto.getNuevoEstado());

        // 3. Persiste el cambio
        Cita citaActualizada = citaRepository.save(cita);

        // 4. Reporta el cambio de estado a auditoría
        auditoriaClient.registrarEvento(new NotificacionDTO(
                "ms-agenda", "ACTUALIZACION_ESTADO", "EXITO",
                "Cita " + idCita + " pasó a estado: " + dto.getNuevoEstado(), LocalDateTime.now()
        ));

        return citaActualizada;
    }
}
