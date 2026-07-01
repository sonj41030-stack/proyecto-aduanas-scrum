package com.proyecto.aduanas.msreportes.controller;

import com.proyecto.aduanas.msreportes.dto.ReporteRequestDTO;
import com.proyecto.aduanas.msreportes.dto.ReporteResponseDTO;
import com.proyecto.aduanas.msreportes.entity.EstadoReporte;
import com.proyecto.aduanas.msreportes.entity.Prioridad;
import com.proyecto.aduanas.msreportes.service.ReporteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
@Slf4j
public class ReporteController {
    
    private final ReporteService reporteService;
    
    @PostMapping
    public ResponseEntity<ReporteResponseDTO> crearReporte(@Valid @RequestBody ReporteRequestDTO requestDTO) {
        log.info("POST /reportes - Creando nuevo reporte");
        ReporteResponseDTO reporte = reporteService.crearReporte(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(reporte);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> obtenerReportePorId(@PathVariable Long id) {
        log.info("GET /reportes/{} - Obteniendo reporte por ID", id);
        ReporteResponseDTO reporte = reporteService.obtenerReportePorId(id);
        return ResponseEntity.ok(reporte);
    }
    
    @GetMapping("/numero/{numeroReporte}")
    public ResponseEntity<ReporteResponseDTO> obtenerReportePorNumero(@PathVariable String numeroReporte) {
        log.info("GET /reportes/numero/{} - Obteniendo reporte por número", numeroReporte);
        Optional<ReporteResponseDTO> reporte = reporteService.obtenerReportePorNumero(numeroReporte);
        return reporte.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<ReporteResponseDTO>> obtenerTodosLosReportes() {
        log.info("GET /reportes - Obteniendo todos los reportes");
        List<ReporteResponseDTO> reportes = reporteService.obtenerTodosLosReportes();
        return ResponseEntity.ok(reportes);
    }
    
    @GetMapping("/placa/{placa}")
    public ResponseEntity<List<ReporteResponseDTO>> obtenerReportesPorPlaca(@PathVariable String placa) {
        log.info("GET /reportes/placa/{} - Obteniendo reportes por placa", placa);
        List<ReporteResponseDTO> reportes = reporteService.obtenerReportesPorPlaca(placa);
        return ResponseEntity.ok(reportes);
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReporteResponseDTO>> obtenerReportesPorEstado(@PathVariable EstadoReporte estado) {
        log.info("GET /reportes/estado/{} - Obteniendo reportes por estado", estado);
        List<ReporteResponseDTO> reportes = reporteService.obtenerReportesPorEstado(estado);
        return ResponseEntity.ok(reportes);
    }
    
    @GetMapping("/prioridad/{prioridad}")
    public ResponseEntity<List<ReporteResponseDTO>> obtenerReportesPorPrioridad(@PathVariable Prioridad prioridad) {
        log.info("GET /reportes/prioridad/{} - Obteniendo reportes por prioridad", prioridad);
        List<ReporteResponseDTO> reportes = reporteService.obtenerReportesPorPrioridad(prioridad);
        return ResponseEntity.ok(reportes);
    }
    
    @GetMapping("/responsable/{responsable}")
    public ResponseEntity<List<ReporteResponseDTO>> obtenerReportesPorResponsable(@PathVariable String responsable) {
        log.info("GET /reportes/responsable/{} - Obteniendo reportes por responsable", responsable);
        List<ReporteResponseDTO> reportes = reporteService.obtenerReportesPorResponsable(responsable);
        return ResponseEntity.ok(reportes);
    }
    
    @GetMapping("/rango")
    public ResponseEntity<List<ReporteResponseDTO>> obtenerReportesPorRango(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fin) {
        log.info("GET /reportes/rango - Obteniendo reportes en rango de fechas");
        List<ReporteResponseDTO> reportes = reporteService.obtenerReportesPorRango(inicio, fin);
        return ResponseEntity.ok(reportes);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> actualizarReporte(
            @PathVariable Long id,
            @Valid @RequestBody ReporteRequestDTO requestDTO) {
        log.info("PUT /reportes/{} - Actualizando reporte", id);
        ReporteResponseDTO reporte = reporteService.actualizarReporte(id, requestDTO);
        return ResponseEntity.ok(reporte);
    }
    
    @PatchMapping("/{id}/estado/{estado}")
    public ResponseEntity<ReporteResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @PathVariable EstadoReporte estado) {
        log.info("PATCH /reportes/{}/estado/{} - Cambiando estado del reporte", id, estado);
        ReporteResponseDTO reporte = reporteService.cambiarEstado(id, estado);
        return ResponseEntity.ok(reporte);
    }
    
    @PatchMapping("/{id}/completar")
    public ResponseEntity<ReporteResponseDTO> marcarComoCompletado(@PathVariable Long id) {
        log.info("PATCH /reportes/{}/completar - Marcando reporte como completado", id);
        ReporteResponseDTO reporte = reporteService.marcarComoCompletado(id);
        return ResponseEntity.ok(reporte);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        log.info("DELETE /reportes/{} - Eliminando reporte", id);
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/estadisticas/contar/{estado}")
    public ResponseEntity<Long> contarReportesPorEstado(@PathVariable EstadoReporte estado) {
        log.info("GET /reportes/estadisticas/contar/{} - Contando reportes por estado", estado);
        Long cantidad = reporteService.contarReportesPorEstado(estado);
        return ResponseEntity.ok(cantidad);
    }
}
