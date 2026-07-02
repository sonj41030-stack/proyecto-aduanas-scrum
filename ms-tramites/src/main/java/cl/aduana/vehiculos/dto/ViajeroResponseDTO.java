package cl.aduana.vehiculos.dto;

import cl.aduana.vehiculos.model.TipoViajero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ViajeroResponseDTO {
    private Long id;
    private Long salidaTemporalId;
    private String nombreCompleto;
    private String rut;
    private LocalDate fechaNacimiento;
    private TipoViajero tipoViajero;
    private Boolean esMenorEdad;
    private Boolean esPropietario;

    // Solo tendrán valor si esMenorEdad = true
    private String nombreAutorizante;
    private String rutAutorizante;
    private String parentescoAutorizante;
    private String numeroEscrituraNotarial;
    private String notariaNombre;
    private String ciudadNotaria;
    private LocalDate fechaAutorizacion;
    private String urlDocumentoAutorizacion;
    private Boolean validada;

    private LocalDateTime createdAt;
}
