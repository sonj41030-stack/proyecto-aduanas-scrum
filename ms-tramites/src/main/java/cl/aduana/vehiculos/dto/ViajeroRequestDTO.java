package cl.aduana.vehiculos.dto;

import cl.aduana.vehiculos.model.TipoViajero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ViajeroRequestDTO {

    @NotNull(message = "El id de la salida temporal es obligatorio")
    private Long salidaTemporalId;

    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombreCompleto;

    @NotBlank(message = "El RUT es obligatorio")
    private String rut;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @NotNull(message = "El tipo de viajero (CONDUCTOR o PASAJERO) es obligatorio")
    private TipoViajero tipoViajero;

    // ---- Los siguientes campos son obligatorios SOLO si el viajero es menor de edad.
    //      La validación condicional se realiza en el service, no aquí, porque
    //      depende de un dato calculado (la edad) y no puede expresarse con
    //      anotaciones simples sin acoplar el DTO a reglas de negocio. ----
    private String nombreAutorizante;
    private String rutAutorizante;
    private String parentescoAutorizante;
    private String numeroEscrituraNotarial;
    private String notariaNombre;
    private String ciudadNotaria;
    private LocalDate fechaAutorizacion;
    private String urlDocumentoAutorizacion;
}
