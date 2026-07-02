package cl.aduana.vehiculos.service.impl;

import cl.aduana.vehiculos.dto.SalidaTemporalRequestDTO;
import cl.aduana.vehiculos.dto.SalidaTemporalResponseDTO;
import cl.aduana.vehiculos.exception.BusinessRuleException;
import cl.aduana.vehiculos.exception.ResourceNotFoundException;
import cl.aduana.vehiculos.model.EstadoSalida;
import cl.aduana.vehiculos.model.SalidaTemporal;
import cl.aduana.vehiculos.model.Vehiculo;
import cl.aduana.vehiculos.repository.SalidaTemporalRepository;
import cl.aduana.vehiculos.repository.VehiculoRepository;
import cl.aduana.vehiculos.service.SalidaTemporalService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalidaTemporalServiceImpl implements SalidaTemporalService {

    private final SalidaTemporalRepository salidaTemporalRepository;
    private final VehiculoRepository vehiculoRepository;

    public SalidaTemporalServiceImpl(SalidaTemporalRepository salidaTemporalRepository,
                                      VehiculoRepository vehiculoRepository) {
        this.salidaTemporalRepository = salidaTemporalRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    @Override
    public SalidaTemporalResponseDTO registrarSalidaTemporal(SalidaTemporalRequestDTO request) {
        Vehiculo vehiculo = vehiculoRepository.findById(request.getVehiculoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehículo no encontrado con id " + request.getVehiculoId()));

        boolean tieneSalidaActiva = !salidaTemporalRepository
                .findByVehiculoIdAndEstado(vehiculo.getId(), EstadoSalida.EN_TRANSITO)
                .isEmpty();

        if (tieneSalidaActiva) {
            throw new BusinessRuleException(
                    "El vehículo con patente " + vehiculo.getPatente() + " ya tiene una salida temporal activa");
        }

        SalidaTemporal salida = SalidaTemporal.builder()
                .vehiculo(vehiculo)
                .fechaSalida(LocalDateTime.now())
                .fechaRetornoEstimada(request.getFechaRetornoEstimada())
                .motivoViaje(request.getMotivoViaje())
                .paisDestino(request.getPaisDestino())
                .conductorRut(request.getConductorRut())
                .conductorNombre(request.getConductorNombre())
                .observaciones(request.getObservaciones())
                .estado(EstadoSalida.EN_TRANSITO)
                .build();

        SalidaTemporal guardada = salidaTemporalRepository.save(salida);
        return toResponseDTO(guardada);
    }

    @Override
    public SalidaTemporalResponseDTO obtenerPorId(Long id) {
        SalidaTemporal salida = salidaTemporalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salida temporal no encontrada con id " + id));
        return toResponseDTO(salida);
    }

    @Override
    public SalidaTemporalResponseDTO registrarRetorno(Long id) {
        SalidaTemporal salida = salidaTemporalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salida temporal no encontrada con id " + id));

        if (salida.getEstado() != EstadoSalida.EN_TRANSITO) {
            throw new BusinessRuleException("La salida temporal no se encuentra en tránsito");
        }

        salida.setFechaRetornoReal(LocalDateTime.now());
        salida.setEstado(EstadoSalida.RETORNADO);

        SalidaTemporal actualizada = salidaTemporalRepository.save(salida);
        return toResponseDTO(actualizada);
    }

    @Override
    public List<SalidaTemporalResponseDTO> listarPorVehiculo(Long vehiculoId) {
        return salidaTemporalRepository.findByVehiculoId(vehiculoId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private SalidaTemporalResponseDTO toResponseDTO(SalidaTemporal salida) {
        return SalidaTemporalResponseDTO.builder()
                .id(salida.getId())
                .vehiculoId(salida.getVehiculo().getId())
                .patenteVehiculo(salida.getVehiculo().getPatente())
                .fechaSalida(salida.getFechaSalida())
                .fechaRetornoEstimada(salida.getFechaRetornoEstimada())
                .fechaRetornoReal(salida.getFechaRetornoReal())
                .motivoViaje(salida.getMotivoViaje())
                .paisDestino(salida.getPaisDestino())
                .conductorRut(salida.getConductorRut())
                .conductorNombre(salida.getConductorNombre())
                .estado(salida.getEstado())
                .observaciones(salida.getObservaciones())
                .build();
    }
}
