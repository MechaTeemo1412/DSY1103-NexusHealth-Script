package com.NexusHealth.ms_agenda.controller;

import com.NexusHealth.ms_agenda.dto.EstadoCitaDTO;
import com.NexusHealth.ms_agenda.model.Cita;
import com.NexusHealth.ms_agenda.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/agenda")
public class CitaController {
    @Autowired
    private CitaService service;

    // Endpoint que será consumido por ms-notificaciones para saber a quién escribirle
    @GetMapping("/proximas-24h")
    public ResponseEntity<List<Cita>> obtenerCitasParaNotificar() {
        return ResponseEntity.ok(service.obtenerCitasProximas24Horas());
    }

    // Endpoint para actualizar el estado (usamos PATCH porque es una actualización parcial)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cita> modificarEstadoCita(
            @PathVariable Long id,
            @Valid @RequestBody EstadoCitaDTO dto) {
        return ResponseEntity.ok(service.actualizarEstado(id, dto));
    }
}
