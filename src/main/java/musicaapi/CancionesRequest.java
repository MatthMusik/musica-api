package musicaapi;

public record CancionesRequest(
    String titulo,
    String artista,
    String genero,
    int anio
) {        

   
}
