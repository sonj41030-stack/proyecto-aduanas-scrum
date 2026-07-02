package cl.aduana.vehiculos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "salida_temporal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalidaTemporal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "fecha_salida", nullable = false)
    private LocalDateTime fechaSalida;

    @Column(name = "fecha_retorno_estimada", nullable = false)
    private LocalDateTime fechaRetornoEstimada;

    @Column(name = "fecha_retorno_real")
    private LocalDateTime fechaRetornoReal;

    @Column(name = "motivo_viaje", nullable = false, length = 200)
    private String motivoViaje;

    @Column(name = "pais_destino", nullable = false, length = 60)
    private String paisDestino;

    @Column(name = "conductor_rut", nullable = false, length = 15)
    private String conductorRut;

    @Column(name = "conductor_nombre", nullable = false, length = 120)
    private String conductorNombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoSalida estado;

    @Column(length = 300)
    private String observaciones;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoSalida.EN_TRANSITO;
        }
    }
}
