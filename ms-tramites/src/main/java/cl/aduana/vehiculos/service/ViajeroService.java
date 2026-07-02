package cl.aduana.vehiculos.service;

import cl.aduana.vehiculos.dto.ViajeroRequestDTO;
import cl.aduana.vehiculos.dto.ViajeroResponseDTO;

import java.util.List;

public interface ViajeroService {
    ViajeroResponseDTO registrarViajero(ViajeroRequestDTO request);
    ViajeroResponseDTO obtenerPorId(Long id);
    ViajeroResponseDTO validarAutorizacionMenor(Long id);
    List<ViajeroResponseDTO> listarPorSalida(Long salidaTemporalId);
    List<ViajeroResponseDTO> listarMenoresPorSalida(Long salidaTemporalId);
}
