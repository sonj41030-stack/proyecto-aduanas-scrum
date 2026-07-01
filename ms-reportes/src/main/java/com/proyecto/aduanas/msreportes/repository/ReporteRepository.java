package com.proyecto.aduanas.msreportes.repository;

import com.proyecto.aduanas.msreportes.entity.EstadoReporte;
import com.proyecto.aduanas.msreportes.entity.Prioridad;
import com.proyecto.aduanas.msreportes.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    
    Optional<Reporte> findByNumeroReporte(String numeroReporte);
    
    List<Reporte> findByPlacaVehiculo(String placaVehiculo);
    
    List<Reporte> findByEstado(EstadoReporte estado);
    
    List<Reporte> findByPrioridad(Prioridad prioridad);
    
    List<Reporte> findByResponsable(String responsable);
    
    List<Reporte> findByCompletado(Boolean completado);
    
    @Query("SELECT r FROM Reporte r WHERE r.estado = :estado AND r.prioridad = :prioridad")
    List<Reporte> findByEstadoAndPrioridad(
        @Param("estado") EstadoReporte estado,
        @Param("prioridad") Prioridad prioridad
    );
    
    @Query("SELECT r FROM Reporte r WHERE r.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<Reporte> findByFechaCreacionBetween(
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin
    );
    
    @Query("SELECT COUNT(r) FROM Reporte r WHERE r.estado = :estado")
    Long countByEstado(@Param("estado") EstadoReporte estado);
}
