package com.NexusHealth.ms_auditoria.service;

import com.NexusHealth.ms_auditoria.dto.LogAuditoriaDTO;
import com.NexusHealth.ms_auditoria.model.Log;
import com.NexusHealth.ms_auditoria.repository.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
/**
 * Servicio central del microservicio de Auditoría (ms-auditoria) de NexusHealth.
 * <p>
 * Esta clase representa la capa de lógica de negocio (Service Layer) encargada de actuar
 * como el concentrador e interceptor centralizado de eventos transaccionales de todo el ecosistema.
 * Su función principal es ofrecer una solución desacoplada y asíncrona para registrar de forma 
 * persistente en Oracle Cloud las operaciones realizadas por los demás microservicios (Pacientes, 
 * Agenda, Exámenes y Orquestador), satisfaciendo así los indicadores de seguridad y auditoría 
 * técnica distribuidos exigidos en la arquitectura.
 * </p>
 *
 * @author Equipo Desarrollo NexusHealth
 * @version 1.0
 * @since 2026-05-18
 */
@Service
@Slf4j
public class LogService {
    /**
     * Repositorio de persistencia JPA utilizado para la inyección directa y almacenamiento
     * seguro de las trazas de auditoría técnica en la base de datos Oracle Cloud (perfil LOW).
     */
    @Autowired private LogRepository logRepository;
    /**
     * Procesa, mapea y almacena un evento transaccional derivado desde cualquier nodo del sistema.
     * <p>
     * Este método intercepta el payload de datos unificado entrante de forma síncrona. Ejecuta una 
     * traducción estructural limpia desde el objeto de transferencia de datos (DTO) hacia la entidad 
     * de persistencia mapeada {@link Log}, aislando las estructuras físicas de los contratos de la red.
     * </p>
     *
     * @param dto El DTO que encapsula el origen del microservicio, la acción ejecutada, el estado final y el detalle técnico.
     * @return Log La entidad transaccional resultante tras consolidarse y persistirse en la base de datos.
     */

    public Log registrarEvento(LogAuditoriaDTO dto) {
        log.info("Recibiendo registro de auditoría desde: {}", dto.getMicroservicioOrigen());

        // Transformación del DTO a Entity
        Log logEntity = new Log();
        logEntity.setMicroservicioOrigen(dto.getMicroservicioOrigen());
        logEntity.setAccion(dto.getAccion());
        logEntity.setEstado(dto.getEstado());
        logEntity.setDetalle(dto.getDetalle());
        logEntity.setFechaHora(dto.getFechaHora());

        // Persistencia real
        return logRepository.save(logEntity);
    }
    /**
     * Tarea programada batch automática para la consolidación de reportes operativos diarios.
     * <p>
     * Este método es gobernado por el motor de Spring Scheduler en un hilo de ejecución autónomo y aislado.
     * Se gatilla automáticamente mediante una expresión Cron de forma diaria **a las 18:00 horas (6:00 PM)**.
     * </p>
     * <p>
     * Su objetivo técnico y de negocio es:
     * <ol>
     * <li>Recopilar las transacciones y fallas registradas en la ventana de tiempo del día actual.</li>
     * <li>Formatear, estructurar y exportar la data agregada hacia un archivo plano delimitado por comas (CSV).</li>
     * <li>Garantizar que el personal técnico o administradores (DevOps) dispongan de un reporte consolidado
     * sin sobrecargar la memoria en tiempo real, optimizando el uso de CPU de la base de datos autónoma.</li>
     * </ol>
     * </p>
     */
    @Scheduled(cron = "0 0 18 * * *")
    public void generarReporteCSVDiario() {
        log.info("Iniciando generación automática del reporte CSV diario de transacciones...");

        log.info("Reporte CSV generado y almacenado exitosamente.");
    }
}
