package com.Duoc.ms_examenes.repository;

import com.Duoc.ms_examenes.model.EstadoExamen;
import com.Duoc.ms_examenes.model.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamenRepository extends JpaRepository<Examen,Long> {
    List<Examen>findAll();
    Optional<Examen> findById(long id);

    List<Examen> findByIdPaciente(long idPaciente);
    List<Examen> findByEstadoExamen(EstadoExamen estadoExamen);
}
