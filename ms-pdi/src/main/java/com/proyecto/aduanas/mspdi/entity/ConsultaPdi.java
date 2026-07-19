package com.proyecto.aduanas.mspdi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Historial de cada consulta de estado PDI realizada desde el frontend,
 * con el resultado que arrojó en ese momento.
 */
@Entity
@Table(name = "consultas_pdi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaPdi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreConsultado;

    @Column(nullable = false)
    private String documento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private EstadoPdi estado;

    @Column(length = 300)
    private String observaciones;

    @Column(nullable = false)
    private LocalDateTime fechaConsulta;

    @PrePersist
    protected void onCreate() {
        this.fechaConsulta = LocalDateTime.now();
    }
}
