package com.proyecto.aduanas.msreportes.dto;

import com.proyecto.aduanas.msreportes.entity.Prioridad;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteRequestDTO {
    
    @NotBlank(message = "La placa del vehículo es requerida")
    private String placaVehiculo;
    
    @NotBlank(message = "El tipo de vehículo es requerido")
    private String tipoVehiculo;
    
    @NotBlank(message = "La descripción del egreso es requerida")
    private String descripcionEgreso;
    
    @NotNull(message = "El monto del egreso es requerido")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal montoEgreso;
    
    @NotNull(message = "La prioridad es requerida")
    private Prioridad prioridad;
    
    @NotBlank(message = "El responsable es requerido")
    private String responsable;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
}
