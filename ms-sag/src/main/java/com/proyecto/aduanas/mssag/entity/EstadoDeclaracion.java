package com.proyecto.aduanas.mssag.entity;

public enum EstadoDeclaracion {
    REGISTRADA("Registrada"),
    OBSERVADA("Observada — requiere certificado sanitario"),
    APROBADA("Aprobada por funcionario SAG");

    private final String descripcion;

    EstadoDeclaracion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
