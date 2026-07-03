package com.proyecto.aduanas.msreportes.exception;

public class ReporteNotFoundException extends RuntimeException {

    public ReporteNotFoundException(String message) {
        super(message);
    }

    public static ReporteNotFoundException porId(Long id) {
        return new ReporteNotFoundException("Reporte no encontrado con ID: " + id);
    }
}