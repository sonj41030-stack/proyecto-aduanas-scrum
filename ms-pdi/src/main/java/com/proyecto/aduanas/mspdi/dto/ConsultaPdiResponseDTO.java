package com.proyecto.aduanas.mspdi.dto;

import com.proyecto.aduanas.mspdi.entity.EstadoPdi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaPdiResponseDTO {
    private Long id;
    private String nombreConsultado;
    private String documento;
    private EstadoPdi estado;
    private String mensaje;
    private String observaciones;
    private LocalDateTime fechaConsulta;
}
