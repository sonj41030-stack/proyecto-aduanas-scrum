package com.proyecto.aduanas.mssag.repository;

import com.proyecto.aduanas.mssag.entity.DeclaracionSag;
import com.proyecto.aduanas.mssag.entity.EstadoDeclaracion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeclaracionSagRepository extends JpaRepository<DeclaracionSag, Long> {
    List<DeclaracionSag> findByEstado(EstadoDeclaracion estado);
    List<DeclaracionSag> findByPasajeroNombreContainingIgnoreCase(String pasajeroNombre);
}
