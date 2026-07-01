package com.proyecto.aduanas.msreportes.entity;

public enum Prioridad {
    BAJA("Baja"),
    MEDIA("Media"),
    ALTA("Alta"),
    CRITICA("Crítica");
    
    private final String descripcion;
    
    Prioridad(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
