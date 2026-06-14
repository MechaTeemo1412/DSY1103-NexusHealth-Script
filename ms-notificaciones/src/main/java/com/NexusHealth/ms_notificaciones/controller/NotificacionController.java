package com.NexusHealth.ms_notificaciones.controller;

import com.NexusHealth.ms_notificaciones.dto.NotificacionDTO;
import com.NexusHealth.ms_notificaciones.model.Notificacion;
import com.NexusHealth.ms_notificaciones.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Notificaciones",description = "Endpoints para el motor de envíos y WebHooks")
public class NotificacionController {
    @Autowired
    private NotificacionService service;

    @PostMapping("/enviar")
    @Operation(
            summary = "Despachar notificaciones al paciente",
            description = "Recibe el DTO con los datos de contacto y despacha el mensaje"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Notificación despachada y guardado en el historial exitosamente",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = Notificacion.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de contacto inválidos o payload mal formado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Falla de red al intentar conectar con el proveedor de mensajería",
                    content = @Content
            )
    })
    public ResponseEntity<Notificacion> enviarMensaje(@Valid @RequestBody NotificacionDTO dto) {
        return new ResponseEntity<>(service.procesarEnvio(dto), HttpStatus.CREATED);
    }
}
