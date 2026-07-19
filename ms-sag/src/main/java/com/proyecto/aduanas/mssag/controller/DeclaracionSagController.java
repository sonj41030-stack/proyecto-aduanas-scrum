package com.proyecto.aduanas.mssag.controller;

import com.proyecto.aduanas.mssag.dto.DeclaracionSagRequestDTO;
import com.proyecto.aduanas.mssag.dto.DeclaracionSagResponseDTO;
import com.proyecto.aduanas.mssag.service.DeclaracionSagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sag")
@RequiredArgsConstructor
@Slf4j
public class DeclaracionSagController {

    private final DeclaracionSagService declaracionSagService;

    @PostMapping
    public ResponseEntity<DeclaracionSagResponseDTO> registrar(@Valid @RequestBody DeclaracionSagRequestDTO requestDTO) {
        log.info("POST /sag - Registrando declaración SAG");
        return ResponseEntity.status(HttpStatus.CREATED).body(declaracionSagService.registrarDeclaracion(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<DeclaracionSagResponseDTO>> listarTodas() {
        return ResponseEntity.ok(declaracionSagService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeclaracionSagResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(declaracionSagService.obtenerPorId(id));
    }

    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<DeclaracionSagResponseDTO> aprobar(@PathVariable Long id) {
        return ResponseEntity.ok(declaracionSagService.aprobarDeclaracion(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        declaracionSagService.eliminarDeclaracion(id);
        return ResponseEntity.noContent().build();
    }
}
