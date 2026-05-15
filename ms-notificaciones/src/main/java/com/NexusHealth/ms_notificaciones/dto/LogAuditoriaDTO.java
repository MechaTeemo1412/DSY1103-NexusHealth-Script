package com.NexusHealth.ms_notificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogAuditoriaDTO {
    private String microservicioOrigen;
    private String accion;
    private String estado;
    private String detalle;
    private LocalDateTime fechaHora;
}
