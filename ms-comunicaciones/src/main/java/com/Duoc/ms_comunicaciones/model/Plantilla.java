package com.Duoc.ms_comunicaciones.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Getter
@Table(name="PLANTILLAS_NOTIFICACIONES")
public class Plantilla {

    @Id
    @Column(name="ID_PLANTILLA")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="TIPO_NOTIFICACION")
    private TipoNotificacion tipoNotificacion;

    @Enumerated(EnumType.STRING)
    @Column(name="CANAL_ENVIO")
    private CanalEnvio canalEnvio;

    @Column(name="CUERPO_MENSAJE", length = 500)
    private String cuerpoMensaje;

}
