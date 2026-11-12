package musicaapi.observabilidad;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

import musicaapi.catalogo.RepositorioCanciones;

@Component
public class CancionesHealthIndicator implements HealthIndicator {

    private final RepositorioCanciones cancionRepository;

    public CancionesHealthIndicator(RepositorioCanciones cancionRepository) {
        this.cancionRepository = cancionRepository;
    }

    @Override
    public Health health() {
        try {
            long total = cancionRepository.count();

            return Health.up()
                    .withDetail("servicio", "Catálogo de canciones disponible")
                    .withDetail("totalCanciones", total)
                    .build();

        } catch (Exception ex) {
            return Health.down()
                    .withDetail("servicio", "Catálogo de canciones no disponible")
                    .withDetail("error", ex.getMessage())
                    .build();
        }
    }
}