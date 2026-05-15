package com.NexusHealth.ms_examenes.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="EXAMENES")
public class Examen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_rut", nullable = false)
    private String pacienteRut; // Clave de negocio para vincular con ms-pacientes

    @Column(name = "tipo_examen", nullable = false)
    private String tipoExamen; // Ej: "Perfil Lipídico", "Hemograma"

    @Column(name = "fecha_toma", nullable = false)
    private LocalDateTime fechaToma;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoExamen estado; // Controlado por el Enum
}
