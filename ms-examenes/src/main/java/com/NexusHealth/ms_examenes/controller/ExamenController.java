package com.NexusHealth.ms_examenes.controller;

import com.NexusHealth.ms_examenes.dto.EstadoExamenDTO;
import com.NexusHealth.ms_examenes.model.Examen;
import com.NexusHealth.ms_examenes.service.ExamenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/examenes")
public class ExamenController {
    @Autowired
    private ExamenService examenService;
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Examen> modificarEstadoExamen(
            @PathVariable Long id,
            @Valid @RequestBody EstadoExamenDTO dto) {
        return ResponseEntity.ok(examenService.actualizarEstado(id, dto));
    }
}
