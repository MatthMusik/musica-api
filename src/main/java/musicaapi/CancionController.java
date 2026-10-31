package musicaapi;

import java.util.List;

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

import jakarta.validation.Valid;

@RestController
public class CancionController {

    private final RepositorioCanciones cancionRepository;

    public CancionController(RepositorioCanciones cancionRepository) {
        this.cancionRepository = cancionRepository;
    }
  

    @GetMapping("/cancion")
    public List<Cancion> listarCanciones(@RequestParam(required = false) String genero) {
        if (genero == null) {
            return cancionRepository.findAll();
        }
        return cancionRepository.findByGeneroIgnoreCase(genero);
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
            return ResponseEntity.noContent().build();
        }

        cancionRepository.deleteById(id);
        return ResponseEntity.notFound().build();
 } 
}