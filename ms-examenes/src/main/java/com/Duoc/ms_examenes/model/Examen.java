package com.Duoc.ms_examenes.model;


import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Entity
@Immutable
@Getter
@Table(name="EXAMENES")
public class Examen {
    @Id
    @Column(name="iD_EXAMEN")
    private Long id;

    @Column(name="ID_PACIENTE")
    private Long idPaciente;

    @Enumerated(EnumType.STRING)
    @Column(name="TIPO_PROCEDIMIENTO")
    private TipoProcedimiento tipoProcedimiento;

    @Enumerated(EnumType.STRING)
    @Column(name="ESTADO_EXAMEN")
    private EstadoExamen estadoExamen;

    @Column(name="FECHA_DISPONIBILIDAD")
    private LocalDateTime fechaDisponibilidad;
}
