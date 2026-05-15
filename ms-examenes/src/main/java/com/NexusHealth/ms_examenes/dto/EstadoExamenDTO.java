package com.NexusHealth.ms_examenes.dto;

import com.NexusHealth.ms_examenes.model.EstadoExamen;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstadoExamenDTO {
    @NotNull(message = "El nuevo estado del examen es obligatorio")
    private EstadoExamen nuevoEstado;
}
