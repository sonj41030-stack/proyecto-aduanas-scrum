package cl.aduana.vehiculos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "viajero")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Viajero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "salida_temporal_id", nullable = false)
    private SalidaTemporal salidaTemporal;

    @Column(name = "nombre_completo", nullable = false, length = 120)
    private String nombreCompleto;

    @Column(nullable = false, length = 15)
    private String rut;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_viajero", nullable = false, length = 20)
    private TipoViajero tipoViajero;

    // Se calcula y persiste al guardar, para poder filtrar sin recalcular en cada consulta
    @Column(name = "es_menor_edad", nullable = false)
    private Boolean esMenorEdad;

    // Se calcula comparando el RUT del viajero contra Vehiculo.propietarioRut al registrar.
    // No hay relación formal con una entidad "propietario": es solo una bandera derivada.
    @Column(name = "es_propietario", nullable = false)
    private Boolean esPropietario;

    // --- Datos de autorización notarial: obligatorios solo si esMenorEdad = true ---
    @Column(name = "nombre_autorizante", length = 120)
    private String nombreAutorizante;

    @Column(name = "rut_autorizante", length = 15)
    private String rutAutorizante;

    @Column(name = "parentesco_autorizante", length = 60)
    private String parentescoAutorizante;

    @Column(name = "numero_escritura_notarial", length = 60)
    private String numeroEscrituraNotarial;

    @Column(name = "notaria_nombre", length = 120)
    private String notariaNombre;

    @Column(name = "ciudad_notaria", length = 80)
    private String ciudadNotaria;

    @Column(name = "fecha_autorizacion")
    private LocalDate fechaAutorizacion;

    @Column(name = "url_documento_autorizacion", length = 300)
    private String urlDocumentoAutorizacion;

    @Column
    private Boolean validada;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (Boolean.TRUE.equals(this.esMenorEdad) && this.validada == null) {
            this.validada = false;
        }
    }
}
