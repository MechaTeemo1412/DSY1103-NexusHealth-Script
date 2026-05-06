package com.Duoc.ms_agenda.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name="TB_AGENDA_CITAS")
public class Cita {
    @Id
    @Column(name="ID_CITA")
    private Long id;

    @Column(name="ID_PACIENTE")
    private Long idPaciente;

    @Column(name="FECHA_HORA_CITA")
    private LocalDateTime fechaHora;

    // Aquí aplicamos tu Enum de forma segura
    @Enumerated(EnumType.STRING)
    @Column(name="ESTADO_CITA", length = 50)
    private EstadoCita estadoCita;
}
