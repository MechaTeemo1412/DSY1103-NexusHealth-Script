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

@Service
@Slf4j
public class ExamenService {
    @Autowired
    private ExamenRepository examenRepository;

    @Autowired
    private AuditoriaClient auditoriaClient;

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
