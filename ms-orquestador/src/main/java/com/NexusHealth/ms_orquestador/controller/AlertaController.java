package com.NexusHealth.ms_orquestador.controller;

import com.NexusHealth.ms_orquestador.model.Alerta;
import com.NexusHealth.ms_orquestador.service.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alertas")
public class AlertaController {
    @Autowired
    private AlertaService alertaService;
    @PostMapping("/ejecutar-ahora")
    public ResponseEntity<Alerta> dispararProcesoManualmente() {
        return ResponseEntity.ok(alertaService.procesarAlertasDeAgenda());
    }
}
