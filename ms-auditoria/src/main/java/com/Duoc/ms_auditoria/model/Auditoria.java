package com.Duoc.ms_auditoria.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Auditoria")
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="USUARIO_ORIGEN")
    private String usuario;

    @Column(name="ACCION_REALIZADA")
    private String accion;

    @Column(name="DETALLE")
    private String detalle;

    @Column(name="FECHA_HORA")
    private LocalDateTime fechaHora;
}
