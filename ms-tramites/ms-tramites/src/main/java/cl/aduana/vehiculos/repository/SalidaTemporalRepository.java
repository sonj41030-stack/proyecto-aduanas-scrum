package cl.aduana.vehiculos.repository;

import cl.aduana.vehiculos.model.EstadoSalida;
import cl.aduana.vehiculos.model.SalidaTemporal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalidaTemporalRepository extends JpaRepository<SalidaTemporal, Long> {
    List<SalidaTemporal> findByVehiculoId(Long vehiculoId);
    List<SalidaTemporal> findByVehiculoIdAndEstado(Long vehiculoId, EstadoSalida estado);
}
