package musicaapi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CancionController {

    private final List<Cancion> canciones = new ArrayList<>(List.of(
            new Cancion(1, "Bohemian Rhapsody", "Queen", "Rock", 1975),
            new Cancion(2, "Imagine", "John Lennon", "Pop", 1971),
            new Cancion(3, "Smells Like Teen Spirit", "Nirvana", "Grunge", 1991),
            new Cancion(4, "Billie Jean", "Michael Jackson", "Pop", 1982),
            new Cancion(5, "Hotel California", "Eagles", "Rock", 1976)
    ));

    private int siguienteId = canciones.size() + 1;

    @GetMapping("/canciones")
    public List<Cancion> listarCanciones(@RequestParam(required = false) String genero) {
        if (genero == null) {
            return canciones;
        }
        List<Cancion> cancionesFiltradas = new ArrayList<>();
        for (Cancion c : canciones) {
            if (c.genero().equalsIgnoreCase(genero)) {
                cancionesFiltradas.add(c);
            }
        }
        return cancionesFiltradas;
    }

    @GetMapping("/canciones/{id}")
    public ResponseEntity<Cancion> obtenerCancionPorId(@PathVariable int id) {
        for (Cancion c : canciones) {
            if (c.id() == id) {
                return ResponseEntity.ok(c);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/canciones")
    public ResponseEntity<Cancion> agregarCancion(@RequestBody CancionesRequest request) {
        Cancion nuevaCancion = new Cancion(
                siguienteId++,
                request.titulo(),
                request.artista(),
                request.genero(),
                request.anio()
        );
        canciones.add(nuevaCancion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCancion);
    }

    @DeleteMapping("/canciones/{id}")
    public ResponseEntity<Void> eliminarCancion(@PathVariable int id) {
        boolean eliminada = canciones.removeIf(c -> c.id() == id);

        if (eliminada) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}