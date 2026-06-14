package com.NexusHealth.ms_orquestador.controller;

import com.NexusHealth.ms_orquestador.model.Alerta;
import com.NexusHealth.ms_orquestador.service.AlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alertas")
@Tag(name = "Orquestador",description = "Controlador Central de tareas automatizadas y manuales")
public class AlertaController {
    @Autowired
    private AlertaService alertaService;
    @PostMapping("/ejecutar-ahora")
    @Operation(
            summary = "Disparar alertas (manualmente)",
            description = "Endspoint para ejecutar bajo demanda de flujo completo: lee agenda, valida con pacientes, envía por notificaciones y reporta a auditoría. Posee tolerancia a fallos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Flujo finalizado",
                    content = @Content(mediaType = "applicaion/json",schema = @Schema(implementation = Alerta.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error o timeout de algún microservicio",
                    content = @Content
            )
    })
    public ResponseEntity<Alerta> dispararProcesoManualmente() {
        return ResponseEntity.ok(alertaService.procesarAlertasDeAgenda());
    }
}
