package com.proyecto.aduanas.mssag.service.impl;

import com.proyecto.aduanas.mssag.dto.DeclaracionSagRequestDTO;
import com.proyecto.aduanas.mssag.dto.DeclaracionSagResponseDTO;
import com.proyecto.aduanas.mssag.entity.DeclaracionSag;
import com.proyecto.aduanas.mssag.entity.EstadoDeclaracion;
import com.proyecto.aduanas.mssag.exception.DeclaracionNotFoundException;
import com.proyecto.aduanas.mssag.repository.DeclaracionSagRepository;
import com.proyecto.aduanas.mssag.service.DeclaracionSagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeclaracionSagServiceImpl implements DeclaracionSagService {

    private final DeclaracionSagRepository repository;

    @Override
    public DeclaracionSagResponseDTO registrarDeclaracion(DeclaracionSagRequestDTO requestDTO) {
        boolean requiereCertificado = Boolean.TRUE.equals(requestDTO.getTransportaAlimentos())
                || Boolean.TRUE.equals(requestDTO.getTransportaMascotas());
        boolean tieneCertificado = Boolean.TRUE.equals(requestDTO.getCertificadoSanitario());

        // Regla de negocio: si declara alimentos o mascotas sin certificado sanitario,
        // la declaración queda OBSERVADA y debe revisarla un funcionario SAG.
        EstadoDeclaracion estado = (requiereCertificado && !tieneCertificado)
                ? EstadoDeclaracion.OBSERVADA
                : EstadoDeclaracion.REGISTRADA;

        DeclaracionSag entidad = DeclaracionSag.builder()
                .pasajeroNombre(requestDTO.getPasajeroNombre())
                .paisProcedencia(requestDTO.getPaisProcedencia())
                .transportaAlimentos(requestDTO.getTransportaAlimentos())
                .transportaMascotas(requestDTO.getTransportaMascotas())
                .detalle(requestDTO.getDetalle())
                .certificadoSanitario(requestDTO.getCertificadoSanitario())
                .estado(estado)
                .build();

        return toDto(repository.save(entidad));
    }

    @Override
    public DeclaracionSagResponseDTO obtenerPorId(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new DeclaracionNotFoundException("No existe declaración SAG con id " + id));
    }

    @Override
    public List<DeclaracionSagResponseDTO> listarTodas() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public DeclaracionSagResponseDTO aprobarDeclaracion(Long id) {
        DeclaracionSag entidad = repository.findById(id)
                .orElseThrow(() -> new DeclaracionNotFoundException("No existe declaración SAG con id " + id));
        entidad.setEstado(EstadoDeclaracion.APROBADA);
        return toDto(repository.save(entidad));
    }

    @Override
    public void eliminarDeclaracion(Long id) {
        if (!repository.existsById(id)) {
            throw new DeclaracionNotFoundException("No existe declaración SAG con id " + id);
        }
        repository.deleteById(id);
    }

    private DeclaracionSagResponseDTO toDto(DeclaracionSag entidad) {
        return DeclaracionSagResponseDTO.builder()
                .id(entidad.getId())
                .pasajeroNombre(entidad.getPasajeroNombre())
                .paisProcedencia(entidad.getPaisProcedencia())
                .transportaAlimentos(entidad.getTransportaAlimentos())
                .transportaMascotas(entidad.getTransportaMascotas())
                .detalle(entidad.getDetalle())
                .certificadoSanitario(entidad.getCertificadoSanitario())
                .estado(entidad.getEstado())
                .fechaDeclaracion(entidad.getFechaDeclaracion())
                .build();
    }
}
