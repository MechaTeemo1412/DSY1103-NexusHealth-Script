package com.NexusHealth.ms_orquestador.repository;

import com.NexusHealth.ms_orquestador.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta,Long> {
}
