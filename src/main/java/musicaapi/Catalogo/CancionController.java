package musicaapi.catalogo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;

@RestController
public class CancionController {

    private static final Logger logger = LoggerFactory.getLogger(CancionController.class);
    private final RepositorioCanciones cancionRepository;
    private final Counter cancionesCreadas;

    public CancionController(RepositorioCanciones cancionRepository,
                         MeterRegistry meterRegistry) {
        this.cancionRepository = cancionRepository;
        this.cancionesCreadas = Counter.builder("musica.canciones.creadas")
            .description("Cantidad de canciones creadas")
            .register(meterRegistry);
    }  

    @GetMapping("/cancion")
    public List<Cancion> listarCanciones(@RequestParam(required = false) String genero) {
        if (genero == null) {
            return cancionRepository.findAll();
        }
        return cancionRepository.findByGeneroIgnoreCase(genero);
    }

    @GetMapping("/cancion/buscar")
    public List<Cancion> buscarPorArtista(@RequestParam String artista) {
        return cancionRepository.findByArtistaIgnoreCase(artista);
    }

    @GetMapping("/cancion/{id}")
    public ResponseEntity<Cancion> obtenerCancionPorId(@PathVariable Long id) {
        return cancionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/cancion")
    public ResponseEntity<Cancion> agregarCancion(@Valid @RequestBody CancionesRequest request) {
        Cancion nueva = new Cancion(                
                request.titulo(),
                request.artista(),
                request.genero(),
                request.anio()
        );
        Cancion guardada = cancionRepository.save(nueva);
                cancionesCreadas.increment();
                logger.info("Canción creada: {} - {}", guardada.getTitulo(), guardada.getArtista());
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);          
          }

    @PutMapping("/cancion/{id}")
    public ResponseEntity<Cancion> actualizarCancion(
        @PathVariable Long id,
        @Valid @RequestBody CancionesRequest request) {

    return cancionRepository.findById(id)
            .map(cancion -> {
                cancion.setTitulo(request.titulo());
                cancion.setArtista(request.artista());
                cancion.setGenero(request.genero());
                cancion.setAnio(request.anio());

                Cancion actualizada = cancionRepository.save(cancion);
                return ResponseEntity.ok(actualizada);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/cancion/{id}")
        public ResponseEntity<Void> eliminarCancion(@PathVariable Long id) {
        if (!cancionRepository.existsById(id)) {
            logger.warn("Se intentó eliminar una canción que no existe, id: {}", id);
          return ResponseEntity.notFound().build();
    }

        cancionRepository.deleteById(id);
            logger.info("Canción eliminada, id: {}", id);
         return ResponseEntity.noContent().build();
 }
}