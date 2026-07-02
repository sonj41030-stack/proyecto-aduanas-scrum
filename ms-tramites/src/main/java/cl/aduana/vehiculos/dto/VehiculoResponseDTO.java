package cl.aduana.vehiculos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class VehiculoResponseDTO {
    private Long id;
    private String patente;
    private String marca;
    private String modelo;
    private Integer anio;
    private String color;
    private String tipoVehiculo;
    private String propietarioRut;
    private String propietarioNombre;
    private LocalDateTime fechaRegistro;
}
