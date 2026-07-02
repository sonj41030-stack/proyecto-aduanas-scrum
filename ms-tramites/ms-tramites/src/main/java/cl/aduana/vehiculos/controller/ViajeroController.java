package cl.aduana.vehiculos.controller;

import cl.aduana.vehiculos.dto.ViajeroRequestDTO;
import cl.aduana.vehiculos.dto.ViajeroResponseDTO;
import cl.aduana.vehiculos.service.ViajeroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/viajeros")
@Tag(name = "Viajeros", description = "Registro del paso de todos los viajeros del vehículo; " +
        "si el viajero es menor de edad, exige y valida la autorización notarial")
public class ViajeroController {

    private final ViajeroService viajeroService;

    public ViajeroController(ViajeroService viajeroService) {
        this.viajeroService = viajeroService;
    }

    @PostMapping
    @Operation(summary = "Registrar el paso de un viajero (adulto o menor de edad)")
    public ResponseEntity<ViajeroResponseDTO> registrarViajero(
            @Valid @RequestBody ViajeroRequestDTO request) {
        ViajeroResponseDTO creado = viajeroService.registrarViajero(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un viajero por id")
    public ResponseEntity<ViajeroResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(viajeroService.obtenerPorId(id));
    }

    @PatchMapping("/{id}/validar")
    @Operation(summary = "Validar la autorización notarial de un viajero menor de edad (funcionario de aduana)")
    public ResponseEntity<ViajeroResponseDTO> validarAutorizacion(@PathVariable Long id) {
        return ResponseEntity.ok(viajeroService.validarAutorizacionMenor(id));
    }

    @GetMapping("/salida/{salidaTemporalId}")
    @Operation(summary = "Listar todos los viajeros de una salida temporal")
    public ResponseEntity<List<ViajeroResponseDTO>> listarPorSalida(@PathVariable Long salidaTemporalId) {
        return ResponseEntity.ok(viajeroService.listarPorSalida(salidaTemporalId));
    }

    @GetMapping("/salida/{salidaTemporalId}/menores")
    @Operation(summary = "Listar solo los viajeros menores de edad de una salida temporal")
    public ResponseEntity<List<ViajeroResponseDTO>> listarMenoresPorSalida(@PathVariable Long salidaTemporalId) {
        return ResponseEntity.ok(viajeroService.listarMenoresPorSalida(salidaTemporalId));
    }
}
