package com.NexusHealth.ms_pacientes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificacionDTO {
    private String microservicioOrigen;
    private String accion;
    private String estado;
    private String detalle;
    private LocalDateTime fechaHora;
}
