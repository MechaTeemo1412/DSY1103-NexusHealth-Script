package com.Duoc.ms_agenda.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="CITAS")
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID_CITA")
    private Long id;

    @Column(name="ID_PACIENTE", nullable = false)
    private Long idPaciente;

    @Column(name="FECHA_HORA", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name="ESTADO_CITA", nullable = false)
    private String estado;

    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL)
    private List<ExamenMedico> examenes;

}
