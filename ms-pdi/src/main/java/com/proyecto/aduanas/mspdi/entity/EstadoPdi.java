package com.proyecto.aduanas.mspdi.entity;

public enum EstadoPdi {
    HABILITADO("Habilitado para salir del país"),
    OBSERVACIONES("Existen observaciones pendientes"),
    BLOQUEADO("Salida bloqueada — debe regularizar su situación");

    private final String descripcion;

    EstadoPdi(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
