package musicaapi;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity 
public class Cancion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String artista;
    private String genero;
    private int anio;

    public Cancion() {
    }

    public Cancion(String titulo, String artista, String genero, int anio) {
        
        this.titulo = titulo;
        this.artista = artista;
        this.genero = genero;
        this.anio = anio;
    }

    public Long getId() {return id;}
    public String getTitulo() {return titulo;}
    public String getArtista() {return artista;}
    public String getGenero() {return genero;}
    public int getAnio() {return anio;}

    public void setTitulo(String titulo) {this.titulo = titulo;}
    public void setArtista(String artista) {this.artista = artista;}
    public void setGenero(String genero) {this.genero = genero;}
    public void setAnio(int anio) {this.anio = anio;}
    
}
