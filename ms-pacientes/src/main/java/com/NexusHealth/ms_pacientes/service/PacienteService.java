package com.NexusHealth.ms_pacientes.service;

import com.NexusHealth.ms_pacientes.feignclient.AuditoriaClient;
import com.NexusHealth.ms_pacientes.dto.NotificacionDTO;
import com.NexusHealth.ms_pacientes.exception.ResourceNotFoundException;
import com.NexusHealth.ms_pacientes.model.Paciente;
import com.NexusHealth.ms_pacientes.repository.PacienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PacienteService {
    @Autowired // Inyección del repositorio de persistencia
    private PacienteRepository pacienteRepository;

    @Autowired // Inyección del cliente HTTP para comunicación con otro microservicio
    private AuditoriaClient auditoriaClient;

    public List<Paciente> obtenerTodos() {
        log.info("Iniciando extracción de registros de pacientes"); // Log estratégico
        return pacienteRepository.findAll();
    }

    public Paciente obtenerPorRut(String rut) {
        log.info("Buscando paciente en sistema por RUT: {}", rut);

        Optional<Paciente> paciente = pacienteRepository.findByRut(rut);

        if (paciente.isPresent()) {
            // Comunicación exitosa: Envía log transaccional a ms-auditoria
            auditoriaClient.registrarEvento(new NotificacionDTO(
                    "ms-pacientes", "CONSULTA_POR_RUT", "EXITO", "Se consultó el RUT: " + rut, LocalDateTime.now()
            ));
            return paciente.get();
        } else {
            // Comunicación fallida: Envía log a auditoría y luego lanza excepción
            auditoriaClient.registrarEvento(new NotificacionDTO(
                    "ms-pacientes", "CONSULTA_POR_RUT", "FALLA_CONTROLADA", "No se encontró el RUT: " + rut, LocalDateTime.now()
            ));
            // Esta excepción es capturada por GlobalExceptionHandler
            throw new ResourceNotFoundException("Paciente no encontrado con el RUT: " + rut);
        }
    }
}
