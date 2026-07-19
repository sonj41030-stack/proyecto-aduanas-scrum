package com.proyecto.aduanas.mspdi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaPdiRequestDTO {

    @NotBlank(message = "El nombre es requerido")
    private String nombreConsultado;

    @NotBlank(message = "El RUN/pasaporte es requerido")
    private String documento;
}
