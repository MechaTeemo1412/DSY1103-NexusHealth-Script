package com.NexusHealth.ms_auditoria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogAuditoriaDTO {
    @NotBlank(message = "El origen del microservicio es obligatorio")
    private String microservicioOrigen;

    @NotBlank(message = "La acción realizada es obligatoria")
    private String accion;

    @NotBlank(message = "El estado de la acción es obligatorio")
    private String estado;

    private String detalle;

    @NotNull(message = "La fecha y hora del evento no pueden ser nulas")
    private LocalDateTime fechaHora;
}
