package cl.aduana.vehiculos.controller;

import cl.aduana.vehiculos.dto.VehiculoRequestDTO;
import cl.aduana.vehiculos.dto.VehiculoResponseDTO;
import cl.aduana.vehiculos.service.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehiculos")
@Tag(name = "Vehículos", description = "Ingreso y consulta de datos de vehículos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @PostMapping
    @Operation(summary = "Ingresar datos de un vehículo")
    public ResponseEntity<VehiculoResponseDTO> registrarVehiculo(
            @Valid @RequestBody VehiculoRequestDTO request) {
        VehiculoResponseDTO creado = vehiculoService.registrarVehiculo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un vehículo por id")
    public ResponseEntity<VehiculoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.obtenerPorId(id));
    }

    @GetMapping("/patente/{patente}")
    @Operation(summary = "Obtener un vehículo por patente")
    public ResponseEntity<VehiculoResponseDTO> obtenerPorPatente(@PathVariable String patente) {
        return ResponseEntity.ok(vehiculoService.obtenerPorPatente(patente));
    }

    @GetMapping
    @Operation(summary = "Listar todos los vehículos registrados")
    public ResponseEntity<List<VehiculoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(vehiculoService.listarTodos());
    }
}
