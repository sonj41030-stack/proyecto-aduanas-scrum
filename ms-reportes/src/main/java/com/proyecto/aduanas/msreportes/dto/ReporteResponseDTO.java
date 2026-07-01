package com.proyecto.aduanas.msreportes.dto;

import com.proyecto.aduanas.msreportes.entity.EstadoReporte;
import com.proyecto.aduanas.msreportes.entity.Prioridad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteResponseDTO {
    
    private Long id;
    
    private String numeroReporte;
    
    private String placaVehiculo;
    
    private String tipoVehiculo;
    
    private String descripcionEgreso;
    
    private BigDecimal montoEgreso;
    
    private Prioridad prioridad;
    
    private EstadoReporte estado;
    
    private String responsable;
    
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaModificacion;
    
    private LocalDateTime fechaCierre;
    
    private String observaciones;
    
    private Boolean completado;
}
