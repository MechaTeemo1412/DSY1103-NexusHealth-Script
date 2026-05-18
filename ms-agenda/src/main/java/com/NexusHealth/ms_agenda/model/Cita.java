package com.NexusHealth.ms_agenda.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="CITAS")
@Data
public class Cita {
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    private Long id;

    @Column(name = "paciente_rut", nullable = false) // Vinculación lógica con el ms-pacientes
    private String pacienteRut;

    @Column(name = "medico_nombre", nullable = false)
    private String medicoNombre;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora; // Fecha y hora exacta de la cita

    @Enumerated(EnumType.STRING) // IMPORTANTE: Guarda el texto del Enum ("PROGRAMADA") en vez de un número (0, 1) en Oracle
    @Column(nullable = false)
    private EstadoCita estado;
}
