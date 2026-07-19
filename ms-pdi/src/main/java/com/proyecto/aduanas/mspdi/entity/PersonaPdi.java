package com.proyecto.aduanas.mspdi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Registro base de personas con un estado PDI ya conocido (simula el
 * registro nacional que consultaría la Policía de Investigaciones).
 * Si un documento consultado no está en esta tabla, se asume HABILITADO.
 */
@Entity
@Table(name = "personas_pdi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaPdi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String documento;

    @Column(nullable = false)
    private String nombreCompleto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private EstadoPdi estado;

    @Column(length = 300)
    private String motivo;
}
