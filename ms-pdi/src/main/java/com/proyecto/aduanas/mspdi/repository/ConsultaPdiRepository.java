package com.proyecto.aduanas.mspdi.repository;

import com.proyecto.aduanas.mspdi.entity.ConsultaPdi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaPdiRepository extends JpaRepository<ConsultaPdi, Long> {
    List<ConsultaPdi> findByDocumentoOrderByFechaConsultaDesc(String documento);
    List<ConsultaPdi> findAllByOrderByFechaConsultaDesc();
}
