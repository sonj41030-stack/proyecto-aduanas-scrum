package cl.aduana.vehiculos.service.impl;

import cl.aduana.vehiculos.dto.ViajeroRequestDTO;
import cl.aduana.vehiculos.dto.ViajeroResponseDTO;
import cl.aduana.vehiculos.exception.BusinessRuleException;
import cl.aduana.vehiculos.exception.ResourceNotFoundException;
import cl.aduana.vehiculos.model.SalidaTemporal;
import cl.aduana.vehiculos.model.Vehiculo;
import cl.aduana.vehiculos.model.Viajero;
import cl.aduana.vehiculos.repository.SalidaTemporalRepository;
import cl.aduana.vehiculos.repository.ViajeroRepository;
import cl.aduana.vehiculos.service.ViajeroService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class ViajeroServiceImpl implements ViajeroService {

    private static final int EDAD_MAYORIA = 18;

    private final ViajeroRepository viajeroRepository;
    private final SalidaTemporalRepository salidaTemporalRepository;

    public ViajeroServiceImpl(ViajeroRepository viajeroRepository,
                               SalidaTemporalRepository salidaTemporalRepository) {
        this.viajeroRepository = viajeroRepository;
        this.salidaTemporalRepository = salidaTemporalRepository;
    }

    @Override
    public ViajeroResponseDTO registrarViajero(ViajeroRequestDTO request) {
        SalidaTemporal salida = salidaTemporalRepository.findById(request.getSalidaTemporalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Salida temporal no encontrada con id " + request.getSalidaTemporalId()));

        int edad = Period.between(request.getFechaNacimiento(), LocalDate.now()).getYears();
        boolean esMenorEdad = edad < EDAD_MAYORIA;

        Vehiculo vehiculo = salida.getVehiculo();
        boolean esPropietario = vehiculo.getPropietarioRut().equalsIgnoreCase(request.getRut());

        if (esPropietario && esMenorEdad) {
            throw new BusinessRuleException(
                    "Inconsistencia de datos: el RUT coincide con el propietario del vehículo, "
                            + "pero la fecha de nacimiento indica que es menor de edad");
        }

        if (esPropietario
                && !vehiculo.getPropietarioNombre().equalsIgnoreCase(request.getNombreCompleto())) {
            throw new BusinessRuleException(
                    "El RUT coincide con el propietario del vehículo (" + vehiculo.getPropietarioRut()
                            + ") pero el nombre ingresado no coincide con el registrado ("
                            + vehiculo.getPropietarioNombre() + "). Verifique los datos");
        }

        Viajero.ViajeroBuilder builder = Viajero.builder()
                .salidaTemporal(salida)
                .nombreCompleto(request.getNombreCompleto())
                .rut(request.getRut())
                .fechaNacimiento(request.getFechaNacimiento())
                .tipoViajero(request.getTipoViajero())
                .esMenorEdad(esMenorEdad)
                .esPropietario(esPropietario);

        if (esMenorEdad) {
            validarDatosNotariales(request);
            builder.nombreAutorizante(request.getNombreAutorizante())
                    .rutAutorizante(request.getRutAutorizante())
                    .parentescoAutorizante(request.getParentescoAutorizante())
                    .numeroEscrituraNotarial(request.getNumeroEscrituraNotarial())
                    .notariaNombre(request.getNotariaNombre())
                    .ciudadNotaria(request.getCiudadNotaria())
                    .fechaAutorizacion(request.getFechaAutorizacion())
                    .urlDocumentoAutorizacion(request.getUrlDocumentoAutorizacion())
                    .validada(false);
        }
        // Si es adulto, los campos notariales quedan en null aunque el cliente los haya enviado:
        // no aplican y no deben persistirse.

        Viajero guardado = viajeroRepository.save(builder.build());
        return toResponseDTO(guardado);
    }

    @Override
    public ViajeroResponseDTO obtenerPorId(Long id) {
        Viajero viajero = viajeroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Viajero no encontrado con id " + id));
        return toResponseDTO(viajero);
    }

    @Override
    public ViajeroResponseDTO validarAutorizacionMenor(Long id) {
        Viajero viajero = viajeroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Viajero no encontrado con id " + id));

        if (!Boolean.TRUE.equals(viajero.getEsMenorEdad())) {
            throw new BusinessRuleException(
                    "El viajero es mayor de edad; no requiere validación de autorización notarial");
        }

        viajero.setValidada(true);
        Viajero actualizado = viajeroRepository.save(viajero);
        return toResponseDTO(actualizado);
    }

    @Override
    public List<ViajeroResponseDTO> listarPorSalida(Long salidaTemporalId) {
        return viajeroRepository.findBySalidaTemporalId(salidaTemporalId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public List<ViajeroResponseDTO> listarMenoresPorSalida(Long salidaTemporalId) {
        return viajeroRepository.findBySalidaTemporalIdAndEsMenorEdad(salidaTemporalId, true).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private void validarDatosNotariales(ViajeroRequestDTO request) {
        if (!StringUtils.hasText(request.getNombreAutorizante())
                || !StringUtils.hasText(request.getRutAutorizante())
                || !StringUtils.hasText(request.getParentescoAutorizante())
                || !StringUtils.hasText(request.getNumeroEscrituraNotarial())
                || !StringUtils.hasText(request.getNotariaNombre())
                || !StringUtils.hasText(request.getCiudadNotaria())
                || request.getFechaAutorizacion() == null) {
            throw new BusinessRuleException(
                    "El viajero es menor de edad: se requieren todos los datos de la autorización notarial "
                            + "(autorizante, notaría, ciudad, número de escritura y fecha de autorización)");
        }
        if (request.getFechaAutorizacion().isAfter(LocalDate.now())) {
            throw new BusinessRuleException("La fecha de autorización notarial no puede ser futura");
        }
    }

    private ViajeroResponseDTO toResponseDTO(Viajero viajero) {
        return ViajeroResponseDTO.builder()
                .id(viajero.getId())
                .salidaTemporalId(viajero.getSalidaTemporal().getId())
                .nombreCompleto(viajero.getNombreCompleto())
                .rut(viajero.getRut())
                .fechaNacimiento(viajero.getFechaNacimiento())
                .tipoViajero(viajero.getTipoViajero())
                .esMenorEdad(viajero.getEsMenorEdad())
                .esPropietario(viajero.getEsPropietario())
                .nombreAutorizante(viajero.getNombreAutorizante())
                .rutAutorizante(viajero.getRutAutorizante())
                .parentescoAutorizante(viajero.getParentescoAutorizante())
                .numeroEscrituraNotarial(viajero.getNumeroEscrituraNotarial())
                .notariaNombre(viajero.getNotariaNombre())
                .ciudadNotaria(viajero.getCiudadNotaria())
                .fechaAutorizacion(viajero.getFechaAutorizacion())
                .urlDocumentoAutorizacion(viajero.getUrlDocumentoAutorizacion())
                .validada(viajero.getValidada())
                .createdAt(viajero.getCreatedAt())
                .build();
    }
}
