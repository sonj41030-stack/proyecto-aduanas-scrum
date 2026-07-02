package cl.aduana.vehiculos.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SalidaTemporalRequestDTO {

    @NotNull(message = "El id del vehículo es obligatorio")
    private Long vehiculoId;

    @NotNull(message = "La fecha de retorno estimada es obligatoria")
    @Future(message = "La fecha de retorno estimada debe ser futura")
    private LocalDateTime fechaRetornoEstimada;

    @NotBlank(message = "El motivo del viaje es obligatorio")
    private String motivoViaje;

    @NotBlank(message = "El país de destino es obligatorio")
    private String paisDestino;

    @NotBlank(message = "El RUT del conductor es obligatorio")
    private String conductorRut;

    @NotBlank(message = "El nombre del conductor es obligatorio")
    private String conductorNombre;

    private String observaciones;
}
