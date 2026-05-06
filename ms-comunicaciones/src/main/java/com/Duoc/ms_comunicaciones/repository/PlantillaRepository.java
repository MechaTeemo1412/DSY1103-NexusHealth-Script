package com.Duoc.ms_comunicaciones.repository;

import com.Duoc.ms_comunicaciones.model.CanalEnvio;
import com.Duoc.ms_comunicaciones.model.Plantilla;
import com.Duoc.ms_comunicaciones.model.TipoNotificacion;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantillaRepository {
    List<Plantilla> obtenerPlantillas();
    Optional<Plantilla> buscarPlantillaPorId(Long id);

    List<Plantilla> buscarPlantillasPorCanal(CanalEnvio canalEnvio);
    Optional<Plantilla> buscarPlantillaPorTipoNotificacion(TipoNotificacion tipoNotificacion);
}
