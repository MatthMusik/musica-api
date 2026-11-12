package musicaapi.catalogo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/album")
public class AlbumController {

    private final RepositorioAlbumes albumRepository;
    private final RepositorioCanciones cancionRepository;

    public AlbumController(RepositorioAlbumes albumRepository,
                           RepositorioCanciones cancionRepository) {
        this.albumRepository = albumRepository;
        this.cancionRepository = cancionRepository;
    }

    @PostMapping
    public ResponseEntity<Album> crearAlbum(@RequestBody Album album) {
        Album guardado = albumRepository.save(album);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PostMapping("/{albumId}/cancion")
    public ResponseEntity<Cancion> agregarCancionAlAlbum(
            @PathVariable Long albumId,
            @Valid @RequestBody CancionesRequest request) {

        return albumRepository.findById(albumId)
                .map(album -> {
                    Cancion cancion = new Cancion(
                            request.titulo(),
                            request.artista(),
                            request.genero(),
                            request.anio()
                    );
                    cancion.setAlbum(album);
                    Cancion guardada = cancionRepository.save(cancion);
                    return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}