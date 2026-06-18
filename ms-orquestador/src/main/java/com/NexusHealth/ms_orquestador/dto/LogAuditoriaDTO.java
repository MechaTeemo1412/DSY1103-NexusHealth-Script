package com.NexusHealth.ms_orquestador.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(
        name = "LogAuditoriaDTO",
        description = "Objeto inmutable de transferencia de datos utilizado para el registro de la trazabilidad y eventos del ecosistema NexusHealth."
)
public class LogAuditoriaDTO {
    @NotNull(message = "El origen del microservicio no puede ser nulo")
    @Schema(
            description = "Nombre del microservicio que emitió el evento",
            example = "ms-orquestador",
            allowableValues = {"ms-orquestador", "ms-agenda", "ms-pacientes", "ms-notificaciones", "ms-examenes"}
    )
    private String microservicioOrigen;
    @NotNull(message = "La acción transaccional es requerida")
    @Schema(
            description = "Clasificación de la tarea u operación realizada",
            example = "MODIFICACIÓN DE ESTADO"
    )
    private String accion;
    @NotNull(message = "El estado del log es obligatorio")
    @Schema(
            description = "Nivel de severidad o resultado del evento transaccional",
            example = "INFO",
            allowableValues = {"INFO", "WARN", "FALLA", "EXITO"}
    )
    private String estado;
    @NotNull(message = "El detalle técnico es obligatorio")
    @Schema(
            description = "Mensaje descriptivo del evento o detalle de la excepción capturada (pánico informático)",
            example = "Se leyeron 15 citas de la ventana de 24 horas exitosamente."
    )
    private String detalle;
    @NotNull(message = "La marca de tiempo es obligatoria")
    @Schema(
            description = "Marca de tiempo exacta en la que ocurrió el evento (Formato ISO 8601)",
            example = "2026-05-19T10:30:00"
    )
    private LocalDateTime fechaHora;
}
