package cl.aduana.vehiculos.repository;

import cl.aduana.vehiculos.model.Viajero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViajeroRepository extends JpaRepository<Viajero, Long> {
    List<Viajero> findBySalidaTemporalId(Long salidaTemporalId);
    List<Viajero> findBySalidaTemporalIdAndEsMenorEdad(Long salidaTemporalId, Boolean esMenorEdad);
}
