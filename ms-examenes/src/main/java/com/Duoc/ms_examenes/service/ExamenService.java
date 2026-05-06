package com.Duoc.ms_examenes.service;


import com.Duoc.ms_examenes.model.Examen;
import com.Duoc.ms_examenes.repository.ExamenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExamenService {

    @Autowired
    private ExamenRepository examenRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Examen> obtenerTodos() {
        return examenRepository.findAll();
    }

    public Optional<Examen> obtenerExamenPorId(long id) {
        Optional<Examen> examen = examenRepository.findById(id);
        if (examen.isPresent()) {
            enviarLogAuditoria("CONSULTA_EXAMEN", "Se consultó exitosamente el examen"+id);
        }
        return examen;
    }
    private void enviarLogAuditoria(String accion, String detalle){
        try {
            Map<String,String> body = new HashMap<>();
            body.put("usuario", "sistema_examenes");
            body.put("accion", accion);
            body.put("detalle", detalle);

            // Manteniendo el puerto 8085 para auditoría que definiste originalmente
            String urlAuditoria = "http://localhost:8085/api/v1/auditoria";
            restTemplate.postForEntity(urlAuditoria, body, String.class);
            System.out.println("Log enviado a ms-auditoria con éxito.");
        } catch (Exception e){
            System.err.println("Fallo en la comunicación con ms-auditoria: " + e.getMessage());
        }
    }
}
