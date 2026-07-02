package cl.aduana.vehiculos.controller;

import cl.aduana.vehiculos.dto.SalidaTemporalRequestDTO;
import cl.aduana.vehiculos.dto.SalidaTemporalResponseDTO;
import cl.aduana.vehiculos.service.SalidaTemporalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salidas-temporales")
@Tag(name = "Salidas temporales", description = "Registro de salida temporal de vehículos en el paso aduanero")
public class SalidaTemporalController {

    private final SalidaTemporalService salidaTemporalService;

    public SalidaTemporalController(SalidaTemporalService salidaTemporalService) {
        this.salidaTemporalService = salidaTemporalService;
    }

    @PostMapping
    @Operation(summary = "Registrar salida temporal de un vehículo")
    public ResponseEntity<SalidaTemporalResponseDTO> registrarSalida(
            @Valid @RequestBody SalidaTemporalRequestDTO request) {
        SalidaTemporalResponseDTO creada = salidaTemporalService.registrarSalidaTemporal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una salida temporal por id")
    public ResponseEntity<SalidaTemporalResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(salidaTemporalService.obtenerPorId(id));
    }

    @PatchMapping("/{id}/retorno")
    @Operation(summary = "Registrar el retorno del vehículo al país")
    public ResponseEntity<SalidaTemporalResponseDTO> registrarRetorno(@PathVariable Long id) {
        return ResponseEntity.ok(salidaTemporalService.registrarRetorno(id));
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    @Operation(summary = "Listar salidas temporales de un vehículo")
    public ResponseEntity<List<SalidaTemporalResponseDTO>> listarPorVehiculo(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(salidaTemporalService.listarPorVehiculo(vehiculoId));
    }
}
