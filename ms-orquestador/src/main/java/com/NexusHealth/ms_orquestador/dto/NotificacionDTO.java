package com.NexusHealth.ms_orquestador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificacionDTO {
    private String destinatario; //usaremos el RUT como identificador de destino temporal
    private String mensaje;
}
