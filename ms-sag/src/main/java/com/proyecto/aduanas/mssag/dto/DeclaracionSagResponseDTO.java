package com.proyecto.aduanas.mssag.dto;

import com.proyecto.aduanas.mssag.entity.EstadoDeclaracion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeclaracionSagResponseDTO {
    private Long id;
    private String pasajeroNombre;
    private String paisProcedencia;
    private Boolean transportaAlimentos;
    private Boolean transportaMascotas;
    private String detalle;
    private Boolean certificadoSanitario;
    private EstadoDeclaracion estado;
    private LocalDateTime fechaDeclaracion;
}
