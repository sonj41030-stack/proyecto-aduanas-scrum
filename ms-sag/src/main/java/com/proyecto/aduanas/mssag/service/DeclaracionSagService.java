package com.proyecto.aduanas.mssag.service;

import com.proyecto.aduanas.mssag.dto.DeclaracionSagRequestDTO;
import com.proyecto.aduanas.mssag.dto.DeclaracionSagResponseDTO;

import java.util.List;

public interface DeclaracionSagService {
    DeclaracionSagResponseDTO registrarDeclaracion(DeclaracionSagRequestDTO requestDTO);
    DeclaracionSagResponseDTO obtenerPorId(Long id);
    List<DeclaracionSagResponseDTO> listarTodas();
    DeclaracionSagResponseDTO aprobarDeclaracion(Long id);
    void eliminarDeclaracion(Long id);
}
