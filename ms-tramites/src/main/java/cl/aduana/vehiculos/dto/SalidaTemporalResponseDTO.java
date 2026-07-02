package cl.aduana.vehiculos.dto;

import cl.aduana.vehiculos.model.EstadoSalida;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SalidaTemporalResponseDTO {
    private Long id;
    private Long vehiculoId;
    private String patenteVehiculo;
    private LocalDateTime fechaSalida;
    private LocalDateTime fechaRetornoEstimada;
    private LocalDateTime fechaRetornoReal;
    private String motivoViaje;
    private String paisDestino;
    private String conductorRut;
    private String conductorNombre;
    private EstadoSalida estado;
    private String observaciones;
}
