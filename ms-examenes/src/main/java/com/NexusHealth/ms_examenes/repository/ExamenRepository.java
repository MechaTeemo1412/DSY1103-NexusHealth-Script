package com.NexusHealth.ms_examenes.repository;

import com.NexusHealth.ms_examenes.model.EstadoExamen;
import com.NexusHealth.ms_examenes.model.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExamenRepository extends JpaRepository<Examen,Long> {
    List<Examen> findByEstado(EstadoExamen estado);
}
