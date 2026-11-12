package musicaapi.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioAlbumes extends JpaRepository<Album, Long> {
}