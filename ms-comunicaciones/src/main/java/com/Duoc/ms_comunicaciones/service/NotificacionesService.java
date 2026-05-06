package com.Duoc.ms_comunicaciones.service;


import com.Duoc.ms_comunicaciones.model.Plantilla;
import com.Duoc.ms_comunicaciones.repository.PlantillaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NotificacionesService {

    @Autowired
    private PlantillaRepository plantillaRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Plantilla> obtenerPlantillas(){
        return plantillaRepository.obtenerPlantillas();
    }
    public Optional<Plantilla> obtenerPlantillaPorId(Long id) {
        Optional<Plantilla> plantilla = plantillaRepository.buscarPlantillaPorId(id);
        if (plantilla.isPresent()) {
            enviarLogAuditoria("CONSULTA_PLANTILLA","Se consultó la plantilla de notificaciones ID:"+id);
        }
        return plantilla;
    }

    private void enviarLogAuditoria(String accion, String detalle) {
        try {
            Map<String,String> body = new HashMap<>();
            body.put("usuario", "sistema_notificaciones");
            body.put("accion", accion);
            body.put("detalle", detalle);

            String urlAuditoria = "http://localhost:8085/api/v1/auditoria";
            restTemplate.postForEntity(urlAuditoria, body, String.class);
        } catch (Exception e){
            System.err.println("Fallo en la comunicación con ms-auditoria: " + e.getMessage());
        }
    }

}
