package com.NexusHealth.ms_orquestador.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaDTO {
    private Long id;
    private String pacienteRut;
    private String medicoNombre;
    private LocalDateTime fechaHora;
}
