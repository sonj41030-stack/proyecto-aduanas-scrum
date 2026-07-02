package cl.aduana.vehiculos.service;

import cl.aduana.vehiculos.dto.SalidaTemporalRequestDTO;
import cl.aduana.vehiculos.dto.SalidaTemporalResponseDTO;

import java.util.List;

public interface SalidaTemporalService {
    SalidaTemporalResponseDTO registrarSalidaTemporal(SalidaTemporalRequestDTO request);
    SalidaTemporalResponseDTO obtenerPorId(Long id);
    SalidaTemporalResponseDTO registrarRetorno(Long id);
    List<SalidaTemporalResponseDTO> listarPorVehiculo(Long vehiculoId);
}
