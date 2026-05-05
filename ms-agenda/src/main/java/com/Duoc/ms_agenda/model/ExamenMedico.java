package com.Duoc.ms_agenda.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="EXAMENES_MEDICOS")
public class ExamenMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID_EXAMEN")
    private Long id;

    @Column(name="TIPO_EXAMEN", nullable = false)
    private String tipoExamen;

    @Column(name="RESULTADO")
    private String resultado;

    // Relación exigida por la rúbrica: Muchos exámenes pertenecen a una cita
    @ManyToOne
    @JoinColumn(name = "ID_CITA", nullable = false)
    private Cita cita;
}
