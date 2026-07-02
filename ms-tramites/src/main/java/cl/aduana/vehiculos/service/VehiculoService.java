package cl.aduana.vehiculos.service;

import cl.aduana.vehiculos.dto.VehiculoRequestDTO;
import cl.aduana.vehiculos.dto.VehiculoResponseDTO;

import java.util.List;

public interface VehiculoService {
    VehiculoResponseDTO registrarVehiculo(VehiculoRequestDTO request);
    VehiculoResponseDTO obtenerPorId(Long id);
    VehiculoResponseDTO obtenerPorPatente(String patente);
    List<VehiculoResponseDTO> listarTodos();
}
