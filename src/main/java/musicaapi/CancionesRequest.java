package musicaapi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
    
public record CancionesRequest(

    @NotBlank (message = "El titulo es obligatorio")
    String titulo,
    @NotBlank (message = "El artista es obligatorio")
    String artista,
    @NotBlank (message = "El genero es obligatorio")
    @Size (min = 3, max = 20, message = "El genero debe tener entre 3 y 20 caracteres")
    String genero,    
    @Positive(message = "El año debe ser un número positivo")
    int anio

) {        

   
}
