package com.NexusHealth.ms_notificaciones.controller;

import com.NexusHealth.ms_notificaciones.dto.NotificacionDTO;
import com.NexusHealth.ms_notificaciones.model.Notificacion;
import com.NexusHealth.ms_notificaciones.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notificaciones")
public class NotificacionController {
    @Autowired
    private NotificacionService service;

    @PostMapping("/enviar")
    public ResponseEntity<Notificacion> enviarMensaje(@Valid @RequestBody NotificacionDTO dto) {
        return new ResponseEntity<>(service.procesarEnvio(dto), HttpStatus.CREATED);
    }
}
