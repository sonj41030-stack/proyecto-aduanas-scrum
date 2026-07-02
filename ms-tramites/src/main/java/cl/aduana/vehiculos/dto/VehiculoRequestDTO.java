package cl.aduana.vehiculos.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehiculoRequestDTO {

    @NotBlank(message = "La patente es obligatoria")
    @Pattern(regexp = "^[A-Z0-9]{5,8}$", message = "Formato de patente inválido")
    private String patente;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 1950, message = "El año no es válido")
    private Integer anio;

    @NotBlank(message = "El color es obligatorio")
    private String color;

    @NotBlank(message = "El tipo de vehículo es obligatorio")
    private String tipoVehiculo;

    @NotBlank(message = "El RUT del propietario es obligatorio")
    private String propietarioRut;

    @NotBlank(message = "El nombre del propietario es obligatorio")
    private String propietarioNombre;
}
