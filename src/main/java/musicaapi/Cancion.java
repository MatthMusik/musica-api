package musicaapi;

public record Cancion(
        int id,
        String titulo,
        String artista,
        String genero,
        int anio
) {
}