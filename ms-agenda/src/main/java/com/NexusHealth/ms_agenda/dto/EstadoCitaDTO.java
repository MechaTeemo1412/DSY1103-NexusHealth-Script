package com.NexusHealth.ms_agenda.dto;

import com.NexusHealth.ms_agenda.model.EstadoCita;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstadoCitaDTO {
    @Schema(description = "Nuevo estado que tomará la cita médica", example = "CONFIRMADA")
    @NotNull(message = "El nuevo estado es obligatorio")
    private EstadoCita nuevoEstado;
}
