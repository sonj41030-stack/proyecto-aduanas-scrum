package com.proyecto.aduanas.mspdi.service.impl;

import com.proyecto.aduanas.mspdi.dto.ConsultaPdiRequestDTO;
import com.proyecto.aduanas.mspdi.dto.ConsultaPdiResponseDTO;
import com.proyecto.aduanas.mspdi.entity.ConsultaPdi;
import com.proyecto.aduanas.mspdi.entity.EstadoPdi;
import com.proyecto.aduanas.mspdi.entity.PersonaPdi;
import com.proyecto.aduanas.mspdi.repository.ConsultaPdiRepository;
import com.proyecto.aduanas.mspdi.repository.PersonaPdiRepository;
import com.proyecto.aduanas.mspdi.service.PdiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PdiServiceImpl implements PdiService {

    private final PersonaPdiRepository personaPdiRepository;
    private final ConsultaPdiRepository consultaPdiRepository;

    @Override
    public ConsultaPdiResponseDTO consultarEstado(ConsultaPdiRequestDTO requestDTO) {
        // Busca a la persona en el registro base (simula la consulta a PDI).
        // Si no existe ningún antecedente, se asume habilitada por defecto.
        Optional<PersonaPdi> registro = personaPdiRepository.findByDocumento(requestDTO.getDocumento());

        EstadoPdi estado = registro.map(PersonaPdi::getEstado).orElse(EstadoPdi.HABILITADO);
        String observaciones = registro.map(PersonaPdi::getMotivo).orElse(null);

        ConsultaPdi consulta = ConsultaPdi.builder()
                .nombreConsultado(requestDTO.getNombreConsultado())
                .documento(requestDTO.getDocumento())
                .estado(estado)
                .observaciones(observaciones)
                .build();
        consulta = consultaPdiRepository.save(consulta);

        return toDto(consulta);
    }

    @Override
    public List<ConsultaPdiResponseDTO> listarHistorial() {
        return consultaPdiRepository.findAllByOrderByFechaConsultaDesc()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ConsultaPdiResponseDTO> listarHistorialPorDocumento(String documento) {
        return consultaPdiRepository.findByDocumentoOrderByFechaConsultaDesc(documento)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    private ConsultaPdiResponseDTO toDto(ConsultaPdi consulta) {
        return ConsultaPdiResponseDTO.builder()
                .id(consulta.getId())
                .nombreConsultado(consulta.getNombreConsultado())
                .documento(consulta.getDocumento())
                .estado(consulta.getEstado())
                .mensaje(consulta.getEstado().getDescripcion())
                .observaciones(consulta.getObservaciones())
                .fechaConsulta(consulta.getFechaConsulta())
                .build();
    }
}
