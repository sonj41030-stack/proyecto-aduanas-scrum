package cl.aduana.vehiculos.service.impl;

import cl.aduana.vehiculos.dto.VehiculoRequestDTO;
import cl.aduana.vehiculos.dto.VehiculoResponseDTO;
import cl.aduana.vehiculos.exception.BusinessRuleException;
import cl.aduana.vehiculos.exception.ResourceNotFoundException;
import cl.aduana.vehiculos.model.Vehiculo;
import cl.aduana.vehiculos.repository.VehiculoRepository;
import cl.aduana.vehiculos.service.VehiculoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoServiceImpl(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Override
    public VehiculoResponseDTO registrarVehiculo(VehiculoRequestDTO request) {
        String patenteNormalizada = request.getPatente().toUpperCase();

        if (vehiculoRepository.existsByPatente(patenteNormalizada)) {
            throw new BusinessRuleException(
                    "Ya existe un vehículo registrado con la patente " + patenteNormalizada);
        }

        Vehiculo vehiculo = Vehiculo.builder()
                .patente(patenteNormalizada)
                .marca(request.getMarca())
                .modelo(request.getModelo())
                .anio(request.getAnio())
                .color(request.getColor())
                .tipoVehiculo(request.getTipoVehiculo())
                .propietarioRut(request.getPropietarioRut())
                .propietarioNombre(request.getPropietarioNombre())
                .build();

        Vehiculo guardado = vehiculoRepository.save(vehiculo);
        return toResponseDTO(guardado);
    }

    @Override
    public VehiculoResponseDTO obtenerPorId(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con id " + id));
        return toResponseDTO(vehiculo);
    }

    @Override
    public VehiculoResponseDTO obtenerPorPatente(String patente) {
        Vehiculo vehiculo = vehiculoRepository.findByPatente(patente.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con patente " + patente));
        return toResponseDTO(vehiculo);
    }

    @Override
    public List<VehiculoResponseDTO> listarTodos() {
        return vehiculoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private VehiculoResponseDTO toResponseDTO(Vehiculo vehiculo) {
        return VehiculoResponseDTO.builder()
                .id(vehiculo.getId())
                .patente(vehiculo.getPatente())
                .marca(vehiculo.getMarca())
                .modelo(vehiculo.getModelo())
                .anio(vehiculo.getAnio())
                .color(vehiculo.getColor())
                .tipoVehiculo(vehiculo.getTipoVehiculo())
                .propietarioRut(vehiculo.getPropietarioRut())
                .propietarioNombre(vehiculo.getPropietarioNombre())
                .fechaRegistro(vehiculo.getFechaRegistro())
                .build();
    }
}
