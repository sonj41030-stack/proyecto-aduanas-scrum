package com.proyecto.aduanas.mspdi.config;

import com.proyecto.aduanas.mspdi.entity.EstadoPdi;
import com.proyecto.aduanas.mspdi.entity.PersonaPdi;
import com.proyecto.aduanas.mspdi.repository.PersonaPdiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Carga un par de registros de ejemplo para poder probar los tres estados
 * posibles (habilitado / observaciones / bloqueado) sin depender de datos
 * reales de la PDI.
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PersonaPdiRepository personaPdiRepository;

    @Override
    public void run(String... args) {
        if (personaPdiRepository.count() > 0) {
            return;
        }

        personaPdiRepository.save(PersonaPdi.builder()
                .documento("11111111-1")
                .nombreCompleto("Persona de prueba — observaciones")
                .estado(EstadoPdi.OBSERVACIONES)
                .motivo("Pendiente de regularizar pago de multa de tránsito")
                .build());

        personaPdiRepository.save(PersonaPdi.builder()
                .documento("22222222-2")
                .nombreCompleto("Persona de prueba — bloqueada")
                .estado(EstadoPdi.BLOQUEADO)
                .motivo("Orden judicial de arraigo nacional vigente")
                .build());
    }
}
