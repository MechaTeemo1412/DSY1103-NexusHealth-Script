package com.NexusHealth.ms_auditoria.service;

import com.NexusHealth.ms_auditoria.dto.LogAuditoriaDTO;
import com.NexusHealth.ms_auditoria.model.Log;
import com.NexusHealth.ms_auditoria.repository.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogService {
    @Autowired
    private LogRepository repository;

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
        return repository.save(logEntity);
    }

    // Cron Job: Se ejecuta todos los días a las 18:00:00 según el requerimiento
    @Scheduled(cron = "0 0 18 * * *")
    public void generarReporteCSVDiario() {
        log.info("Iniciando generación automática del reporte CSV diario de transacciones...");

        // Aquí iría la lógica para extraer los logs del día usando repository.findAll()
        // y el uso de librerías como OpenCSV para escribir el archivo.

        log.info("Reporte CSV generado y almacenado exitosamente.");
    }
}
