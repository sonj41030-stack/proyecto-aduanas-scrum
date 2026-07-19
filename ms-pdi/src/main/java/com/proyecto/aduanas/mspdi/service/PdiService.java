package com.proyecto.aduanas.mspdi.service;

import com.proyecto.aduanas.mspdi.dto.ConsultaPdiRequestDTO;
import com.proyecto.aduanas.mspdi.dto.ConsultaPdiResponseDTO;

import java.util.List;

public interface PdiService {
    ConsultaPdiResponseDTO consultarEstado(ConsultaPdiRequestDTO requestDTO);
    List<ConsultaPdiResponseDTO> listarHistorial();
    List<ConsultaPdiResponseDTO> listarHistorialPorDocumento(String documento);
}
