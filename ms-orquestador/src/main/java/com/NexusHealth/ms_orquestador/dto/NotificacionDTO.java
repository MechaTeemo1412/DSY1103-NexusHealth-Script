package com.NexusHealth.ms_orquestador.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "NotificacionDTO",
        description = "Objeto de transferencia para el envío de notificaciones al paciente. Diseñado para cumplir con la Ley 20.584 (Sin datos clínicos)"
)
public class NotificacionDTO {
    @NotNull(message = "El destinatario es obligatorio")
    @Schema(
            description = "Identificador de destino temporal del paciente (RUT sin puntos y con guion)",
            example = "11111111-1"
    )
    private String destinatario; //usaremos el RUT como identificador de destino temporal
    @NotNull(message = "El mensaje es obligatorio")
    @Schema(
            description = "Cuerpo del mensaje a notificar. Solo contiene información de coordinación, omitiendo cualquier diagnóstico médico.",
            example = "Estimado/a, le recordamos su cita médica para mañana a las 10:30 hrs. Responda 1 para confirmar o 2 para cancelar."
    )
    private String mensaje;
}
