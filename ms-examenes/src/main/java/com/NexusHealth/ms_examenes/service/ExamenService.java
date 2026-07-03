package com.NexusHealth.ms_examenes.service;


import com.NexusHealth.ms_examenes.dto.EstadoExamenDTO;
import com.NexusHealth.ms_examenes.dto.NotificacionDTO;
import com.NexusHealth.ms_examenes.feignclient.AuditoriaClient;
import com.NexusHealth.ms_examenes.model.EstadoExamen;
import com.NexusHealth.ms_examenes.model.Examen;
import com.NexusHealth.ms_examenes.repository.ExamenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
/**
 * Servicio principal del microservicio de Exámenes (ms-examenes).
 * <p>
 * Representa la capa de lógica de negocio (Service Layer) encargada de gestionar 
 * el ciclo de vida y las transiciones de estado de las órdenes de exámenes médicos. 
 * Implementa reglas de negocio condicionales e interactúa de forma síncrona con el 
 * microservicio de auditoría a través de clientes Feign para garantizar la 
 * trazabilidad de cada actualización clínica.
 * </p>
 *
 * @author Equipo Desarrollo NexusHealth
 * @version 1.0
 * @since 2026-05-18
 */
@Service
@Slf4j
public class ExamenService {
    /**
     * Repositorio JPA para la persistencia y recuperación de datos de exámenes 
     * en la base de datos Oracle Cloud.
     */
    @Autowired
    private ExamenRepository examenRepository;
    /**
     * Cliente HTTP declarativo (OpenFeign) utilizado para reportar eventos 
     * transaccionales y cambios de estado críticos al servicio central de auditoría.
     */
    @Autowired
    private AuditoriaClient auditoriaClient;
    /**
     * Actualiza el estado clínico de un examen médico y despacha los eventos de auditoría correspondientes.
     * <p>
     * Este método ejecuta una validación de regla de negocio: si el nuevo estado del examen 
     * es {@code LISTO} (transición desde {@code PENDIENTE}), se clasifica como un evento crítico 
     * que requiere notificación al paciente, generando un log de auditoría especializado. 
     * Cualquier otra transición genera un log de actualización estándar.
     * </p>
     *
     * @param idExamen Identificador único (Primary Key) del examen en la base de datos.
     * @param dto Objeto de transferencia de datos (DTO) que encapsula el nuevo estado a aplicar.
     * @return Examen La entidad actualizada tras ser persistida exitosamente.
     * @throws RuntimeException Si no se encuentra ningún registro coincidente con el {@code idExamen} provisto.
     */

    public Examen actualizarEstado(Long idExamen, EstadoExamenDTO dto) {
        log.info("Iniciando actualización de estado para el examen ID: {}", idExamen);

        // 1. Buscamos el examen original
        Examen examen = examenRepository.findById(idExamen)
                .orElseThrow(() -> new RuntimeException("No existe registro para el examen ID: " + idExamen));

        // 2. Lógica de negocio: Verificamos si hay un cambio real a "LISTO"
        boolean cambioAListo = (examen.getEstado() == EstadoExamen.PENDIENTE && dto.getNuevoEstado() == EstadoExamen.LISTO);

        // 3. Actualizamos el estado
        examen.setEstado(dto.getNuevoEstado());
        Examen examenActualizado = examenRepository.save(examen);

        // 4. Si el examen cambió a LISTO, disparamos una notificación crítica
        if (cambioAListo) {
            log.info("ALERTA: El examen ID {} pasó a estado LISTO. Se requiere notificar al paciente.", idExamen);
            auditoriaClient.registrarEvento(new NotificacionDTO(
                    "ms-examenes", "EXAMEN_LISTO", "EXITO",
                    "Examen " + idExamen + " tipo: " + examen.getTipoExamen() + " está disponible para paciente RUT: " + examen.getPacienteRut(),
                    LocalDateTime.now()
            ));
        } else {
            // Log transaccional normal para otros cambios
            auditoriaClient.registrarEvento(new NotificacionDTO(
                    "ms-examenes", "ACTUALIZACION_ESTADO", "EXITO",
                    "Examen " + idExamen + " actualizado a: " + dto.getNuevoEstado(),
                    LocalDateTime.now()
            ));
        }

        return examenActualizado;
    }
}
