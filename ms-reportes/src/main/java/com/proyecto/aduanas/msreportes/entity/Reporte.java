package com.proyecto.aduanas.msreportes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reporte {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String numeroReporte;
    
    @Column(nullable = false)
    private String placaVehiculo;
    
    @Column(nullable = false)
    private String tipoVehiculo;
    
    @Column(nullable = false)
    private String descripcionEgreso;
    
    @Column(nullable = false)
    private BigDecimal montoEgreso;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridad prioridad;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReporte estado;
    
    @Column(nullable = false)
    private String responsable;
    
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(nullable = true)
    private LocalDateTime fechaModificacion;
    
    @Column(nullable = true)
    private LocalDateTime fechaCierre;
    
    @Column(length = 500)
    private String observaciones;
    
    @Column(nullable = false)
    private Boolean completado;
    
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoReporte.PENDIENTE;
        this.completado = false;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }
}
