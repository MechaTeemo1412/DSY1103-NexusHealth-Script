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

/**
 * Servicio central del microservicio de Pacientes dentro del ecosistema NexusHealth.
 * <p>
 * Esta clase actúa como la capa de lógica de negocio encargada
 * de gestionar, validar y extraer la información demográfica de los pacientes. Opera
 * bajo el patrón arquitectónico Controller-Service-Repository (CSR), sirviendo como
 * un adaptador síncrono que abstrae la persistencia en Oracle Cloud y
 * distribuye eventos de trazabilidad hacia el módulo de auditoría.
 * </p>
 * * @author Equipo Desarrollo NexusHealth
 * @version 1.0
 * @since 2026-05-18
 */
@Service
@Slf4j
public class PacienteService {
    /**
     * Repositorio de persistencia JPA para ejecutar operaciones CRUD 
     * directas sobre la tabla mapeada de Pacientes en Oracle Cloud.
     */
    @Autowired // Inyección del repositorio de persistencia
    private PacienteRepository pacienteRepository;
    /**
     * Cliente HTTP declarativo (OpenFeign) utilizado para intercomunicar 
     * de forma síncrona este servicio con el microservicio central de auditoría (ms-auditoria).
     */
    @Autowired
    private AuditoriaClient auditoriaClient;
    /**
     * Extrae la totalidad de los registros de pacientes almacenados en la base de datos.
     * <p>
     * Consolida una lista limpia de entidades persistidas. Incluye un log estructurado 
     * con SLF4J al inicio del proceso para diagnóstico de latencias.
     * </p>
     * * @return List&lt;Paciente&gt; Una colección con todas las entidades Paciente encontradas.
     */
    public List<Paciente> obtenerTodos() {
        log.info("Iniciando extracción de registros de pacientes"); // Log estratégico
        return pacienteRepository.findAll();
    }
    /**
     * Busca y valida la existencia de un paciente en el sistema clínico utilizando su RUT.
     * <p>
     * Este método implementa un comportamiento transaccional con trazabilidad distribuida (Indicador IE 2.3.2):
     * <ul>
     * <li>Si el paciente existe: Registra un evento de éxito asíncrono en ms-auditoria y retorna el objeto.</li>
     * <li>Si el paciente no existe: Despacha un log tipificado como FALLA_CONTROLADA a auditoría y gatilla una excepción.</li>
     * </ul>
     * </p>
     * * @param rut Cadena de texto que representa el identificador único del paciente (RUT con guion y dígito verificador).
     * @return Paciente La entidad que contiene los datos validados del paciente consultado.
     * @throws ResourceNotFoundException Excepción personalizada de negocio lanzada cuando el RUT no coincide con ningún registro, la cual es capturada y formateada de forma global por el GlobalExceptionHandler.
     */
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
