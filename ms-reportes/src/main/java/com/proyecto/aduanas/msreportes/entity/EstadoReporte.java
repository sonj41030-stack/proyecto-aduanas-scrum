package com.proyecto.aduanas.msreportes.entity;

public enum EstadoReporte {
    PENDIENTE("Pendiente"),
    EN_REVISION("En Revisión"),
    APROBADO("Aprobado"),
    RECHAZADO("Rechazado"),
    CERRADO("Cerrado");
    
    private final String descripcion;
    
    EstadoReporte(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
