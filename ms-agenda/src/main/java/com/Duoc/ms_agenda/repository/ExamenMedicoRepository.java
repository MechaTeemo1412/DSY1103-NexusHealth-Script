package com.Duoc.ms_agenda.repository;

import com.Duoc.ms_agenda.model.ExamenMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamenMedicoRepository extends JpaRepository<ExamenMedico, Long> {
}
