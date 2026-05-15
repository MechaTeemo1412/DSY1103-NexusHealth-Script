package com.NexusHealth.ms_agenda.dto;

import com.NexusHealth.ms_agenda.model.EstadoCita;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstadoCitaDTO {
    @NotNull(message = "El nuevo estado es obligatorio")
    private EstadoCita nuevoEstado;
}
