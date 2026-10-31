package musicaapi;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioCanciones extends JpaRepository<Cancion, Long> {

    List<Cancion> findByGeneroIgnoreCase(String genero);

    List<Cancion> findByTituloContainingIgnoreCase(String titulo);
    
}
