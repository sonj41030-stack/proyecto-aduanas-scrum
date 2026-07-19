package com.proyecto.aduanas.mssag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeclaracionSagRequestDTO {

    @NotBlank(message = "El nombre del pasajero es requerido")
    private String pasajeroNombre;

    @NotBlank(message = "El país de procedencia es requerido")
    private String paisProcedencia;

    @NotNull(message = "Debe indicar si transporta alimentos")
    private Boolean transportaAlimentos;

    @NotNull(message = "Debe indicar si transporta mascotas")
    private Boolean transportaMascotas;

    private String detalle;

    @NotNull(message = "Debe indicar si cuenta con certificado sanitario")
    private Boolean certificadoSanitario;
}
