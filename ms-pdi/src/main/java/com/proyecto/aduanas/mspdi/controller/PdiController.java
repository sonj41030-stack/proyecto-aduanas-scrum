package com.proyecto.aduanas.mspdi.controller;

import com.proyecto.aduanas.mspdi.dto.ConsultaPdiRequestDTO;
import com.proyecto.aduanas.mspdi.dto.ConsultaPdiResponseDTO;
import com.proyecto.aduanas.mspdi.service.PdiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pdi")
@RequiredArgsConstructor
@Slf4j
public class PdiController {

    private final PdiService pdiService;

    @PostMapping("/consultas")
    public ResponseEntity<ConsultaPdiResponseDTO> consultar(@Valid @RequestBody ConsultaPdiRequestDTO requestDTO) {
        log.info("POST /pdi/consultas - Consultando estado PDI para documento {}", requestDTO.getDocumento());
        return ResponseEntity.status(HttpStatus.CREATED).body(pdiService.consultarEstado(requestDTO));
    }

    @GetMapping("/consultas")
    public ResponseEntity<List<ConsultaPdiResponseDTO>> historial() {
        return ResponseEntity.ok(pdiService.listarHistorial());
    }

    @GetMapping("/consultas/documento/{documento}")
    public ResponseEntity<List<ConsultaPdiResponseDTO>> historialPorDocumento(@PathVariable String documento) {
        return ResponseEntity.ok(pdiService.listarHistorialPorDocumento(documento));
    }
}
