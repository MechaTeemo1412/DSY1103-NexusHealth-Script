package com.NexusHealth.ms_auditoria.repository;

import com.NexusHealth.ms_auditoria.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
}
