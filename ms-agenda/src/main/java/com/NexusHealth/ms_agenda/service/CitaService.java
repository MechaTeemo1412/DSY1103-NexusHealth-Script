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

@Service
@Slf4j
public class CitaService {
    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private AuditoriaClient auditoriaClient; // Conexión a ms-auditoria

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

    // Actualiza el estado de la cita (Ej: cuando el paciente responde "1" en WhatsApp)
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
