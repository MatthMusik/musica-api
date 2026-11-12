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

        boolean catalogoDisponible = true;

        if (catalogoDisponible) {
            return Health.up()
                    .withDetail("servicio", "Catálogo de canciones disponible")
                    .withDetail("totalCanciones", 150) // Dato simulado
                    .withDetail("descripcion", "Componente académico simulado correctamente")
                    .build();
        }

        return Health.down()
                .withDetail("servicio", "Catálogo de canciones no disponible")
                .withDetail("error", "Simulación de fallo en el catálogo")
                .build();
    }
}