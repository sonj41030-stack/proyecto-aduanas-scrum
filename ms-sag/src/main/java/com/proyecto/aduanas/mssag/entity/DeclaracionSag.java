package com.proyecto.aduanas.mssag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "declaraciones_sag")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeclaracionSag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pasajeroNombre;

    @Column(nullable = false)
    private String paisProcedencia;

    @Column(nullable = false)
    private Boolean transportaAlimentos;

    @Column(nullable = false)
    private Boolean transportaMascotas;

    @Column(length = 500)
    private String detalle;

    @Column(nullable = false)
    private Boolean certificadoSanitario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private EstadoDeclaracion estado;

    @Column(nullable = false)
    private LocalDateTime fechaDeclaracion;

    @PrePersist
    protected void onCreate() {
        this.fechaDeclaracion = LocalDateTime.now();
    }
}
