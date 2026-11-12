package musicaapi.Deezer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DeezerBusquedaResponse(List<Pista> data) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Pista(String title, Artista artist, Album album) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Artista(String name) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Album(String title) {
    }
}
