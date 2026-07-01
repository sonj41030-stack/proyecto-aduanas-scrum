package com.proyecto.aduanas.msreportes.service;

import com.proyecto.aduanas.msreportes.dto.ReporteRequestDTO;
import com.proyecto.aduanas.msreportes.dto.ReporteResponseDTO;
import com.proyecto.aduanas.msreportes.entity.EstadoReporte;
import com.proyecto.aduanas.msreportes.entity.Prioridad;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReporteService {
    
    ReporteResponseDTO crearReporte(ReporteRequestDTO requestDTO);
    
    ReporteResponseDTO obtenerReportePorId(Long id);
    
    Optional<ReporteResponseDTO> obtenerReportePorNumero(String numeroReporte);
    
    List<ReporteResponseDTO> obtenerTodosLosReportes();
    
    List<ReporteResponseDTO> obtenerReportesPorPlaca(String placa);
    
    List<ReporteResponseDTO> obtenerReportesPorEstado(EstadoReporte estado);
    
    List<ReporteResponseDTO> obtenerReportesPorPrioridad(Prioridad prioridad);
    
    List<ReporteResponseDTO> obtenerReportesPorResponsable(String responsable);
    
    List<ReporteResponseDTO> obtenerReportesPorRango(LocalDateTime inicio, LocalDateTime fin);
    
    ReporteResponseDTO actualizarReporte(Long id, ReporteRequestDTO requestDTO);
    
    ReporteResponseDTO cambiarEstado(Long id, EstadoReporte nuevoEstado);
    
    ReporteResponseDTO marcarComoCompletado(Long id);
    
    void eliminarReporte(Long id);
    
    Long contarReportesPorEstado(EstadoReporte estado);
}
