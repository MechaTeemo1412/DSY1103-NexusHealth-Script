package com.Duoc.ms_orquestador.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CitaDTO {
    private Long id;
    private Long idPaciente;
    private LocalDateTime fechaHora;
    private String estado;
}

