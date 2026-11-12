package musicaapi.Deezer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class DeezerService {

    private static final Logger logger = LoggerFactory.getLogger(DeezerService.class);
    private final RestClient restClient;

    public DeezerService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.deezer.com")
                .build();
    }

    public List<CancionExterna> buscar(String texto) {
        try {
            DeezerBusquedaResponse respuesta = restClient.get()
                    .uri("/search?q={texto}&limit=5", texto)
                    .retrieve()
                    .body(DeezerBusquedaResponse.class);

            if (respuesta == null || respuesta.data() == null) {
                return List.of();
            }

            return respuesta.data().stream()
                    .map(p -> new CancionExterna(
                            p.title(),
                            p.artist().name(),
                            p.album().title()))
                    .toList();

        } catch (RestClientException ex) {
                  logger.error("Falló la consulta a Deezer: {}", ex.getMessage());
          throw new ServicioExternoException("No se pudo consultar Deezer");
}
    }
}