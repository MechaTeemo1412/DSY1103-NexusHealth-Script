package com.NexusHealth.ms_notificaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NotificacionDTO {

    @NotBlank(message = "El destinatario es obligatorio")
    private String destinatario;

    @NotBlank
    @Size(message = "El cuerpo del mensaje no puede estar vacío")
    private String mensaje;
}
