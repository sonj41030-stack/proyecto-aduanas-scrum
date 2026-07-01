package com.proyecto.aduanas.msreportes.service.impl;

import com.proyecto.aduanas.msreportes.dto.ReporteRequestDTO;
import com.proyecto.aduanas.msreportes.dto.ReporteResponseDTO;
import com.proyecto.aduanas.msreportes.entity.EstadoReporte;
import com.proyecto.aduanas.msreportes.entity.Prioridad;
import com.proyecto.aduanas.msreportes.entity.Reporte;
import com.proyecto.aduanas.msreportes.repository.ReporteRepository;
import com.proyecto.aduanas.msreportes.service.ReporteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReporteServiceImpl implements ReporteService {
    
    private final ReporteRepository reporteRepository;
    
    @Override
    public ReporteResponseDTO crearReporte(ReporteRequestDTO requestDTO) {
        log.info("Creando nuevo reporte para vehículo: {}", requestDTO.getPlacaVehiculo());
        
        String numeroReporte = "REP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Reporte reporte = Reporte.builder()
            .numeroReporte(numeroReporte)
            .placaVehiculo(requestDTO.getPlacaVehiculo())
            .tipoVehiculo(requestDTO.getTipoVehiculo())
            .descripcionEgreso(requestDTO.getDescripcionEgreso())
            .montoEgreso(requestDTO.getMontoEgreso())
            .prioridad(requestDTO.getPrioridad())
            .responsable(requestDTO.getResponsable())
            .observaciones(requestDTO.getObservaciones())
            .build();
        
        Reporte reporteGuardado = reporteRepository.save(reporte);
        log.info("Reporte creado exitosamente con número: {}", numeroReporte);
        
        return mapToResponseDTO(reporteGuardado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ReporteResponseDTO obtenerReportePorId(Long id) {
        log.info("Obteniendo reporte con ID: {}", id);
        
        Reporte reporte = reporteRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Reporte no encontrado con ID: {}", id);
                return new RuntimeException("Reporte no encontrado con ID: " + id);
            });
        
        return mapToResponseDTO(reporte);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<ReporteResponseDTO> obtenerReportePorNumero(String numeroReporte) {
        log.info("Obteniendo reporte por número: {}", numeroReporte);
        
        return reporteRepository.findByNumeroReporte(numeroReporte)
            .map(this::mapToResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> obtenerTodosLosReportes() {
        log.info("Obteniendo todos los reportes");
        
        return reporteRepository.findAll().stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> obtenerReportesPorPlaca(String placa) {
        log.info("Obteniendo reportes para placa: {}", placa);
        
        return reporteRepository.findByPlacaVehiculo(placa).stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> obtenerReportesPorEstado(EstadoReporte estado) {
        log.info("Obteniendo reportes con estado: {}", estado);
        
        return reporteRepository.findByEstado(estado).stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> obtenerReportesPorPrioridad(Prioridad prioridad) {
        log.info("Obteniendo reportes con prioridad: {}", prioridad);
        
        return reporteRepository.findByPrioridad(prioridad).stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> obtenerReportesPorResponsable(String responsable) {
        log.info("Obteniendo reportes asignados a: {}", responsable);
        
        return reporteRepository.findByResponsable(responsable).stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> obtenerReportesPorRango(LocalDateTime inicio, LocalDateTime fin) {
        log.info("Obteniendo reportes entre {} y {}", inicio, fin);
        
        return reporteRepository.findByFechaCreacionBetween(inicio, fin).stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public ReporteResponseDTO actualizarReporte(Long id, ReporteRequestDTO requestDTO) {
        log.info("Actualizando reporte con ID: {}", id);
        
        Reporte reporte = reporteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reporte no encontrado con ID: " + id));
        
        reporte.setPlacaVehiculo(requestDTO.getPlacaVehiculo());
        reporte.setTipoVehiculo(requestDTO.getTipoVehiculo());
        reporte.setDescripcionEgreso(requestDTO.getDescripcionEgreso());
        reporte.setMontoEgreso(requestDTO.getMontoEgreso());
        reporte.setPrioridad(requestDTO.getPrioridad());
        reporte.setResponsable(requestDTO.getResponsable());
        reporte.setObservaciones(requestDTO.getObservaciones());
        
        Reporte reporteActualizado = reporteRepository.save(reporte);
        log.info("Reporte actualizado exitosamente");
        
        return mapToResponseDTO(reporteActualizado);
    }
    
    @Override
    public ReporteResponseDTO cambiarEstado(Long id, EstadoReporte nuevoEstado) {
        log.info("Cambiando estado del reporte {} a {}", id, nuevoEstado);
        
        Reporte reporte = reporteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reporte no encontrado con ID: " + id));
        
        reporte.setEstado(nuevoEstado);
        
        if (nuevoEstado == EstadoReporte.CERRADO) {
            reporte.setFechaCierre(LocalDateTime.now());
            reporte.setCompletado(true);
        }
        
        Reporte reporteActualizado = reporteRepository.save(reporte);
        log.info("Estado del reporte actualizado a {}", nuevoEstado);
        
        return mapToResponseDTO(reporteActualizado);
    }
    
    @Override
    public ReporteResponseDTO marcarComoCompletado(Long id) {
        log.info("Marcando reporte {} como completado", id);
        
        return cambiarEstado(id, EstadoReporte.CERRADO);
    }
    
    @Override
    public void eliminarReporte(Long id) {
        log.info("Eliminando reporte con ID: {}", id);
        
        if (!reporteRepository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado con ID: " + id);
        }
        
        reporteRepository.deleteById(id);
        log.info("Reporte eliminado exitosamente");
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long contarReportesPorEstado(EstadoReporte estado) {
        return reporteRepository.countByEstado(estado);
    }
    
    private ReporteResponseDTO mapToResponseDTO(Reporte reporte) {
        return ReporteResponseDTO.builder()
            .id(reporte.getId())
            .numeroReporte(reporte.getNumeroReporte())
            .placaVehiculo(reporte.getPlacaVehiculo())
            .tipoVehiculo(reporte.getTipoVehiculo())
            .descripcionEgreso(reporte.getDescripcionEgreso())
            .montoEgreso(reporte.getMontoEgreso())
            .prioridad(reporte.getPrioridad())
            .estado(reporte.getEstado())
            .responsable(reporte.getResponsable())
            .fechaCreacion(reporte.getFechaCreacion())
            .fechaModificacion(reporte.getFechaModificacion())
            .fechaCierre(reporte.getFechaCierre())
            .observaciones(reporte.getObservaciones())
            .completado(reporte.getCompletado())
            .build();
    }
}
