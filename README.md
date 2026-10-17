# Música API

API REST desarrollada con Spring Boot como ejercicio introductorio al desarrollo de servicios web. Una API es la interfaz que expone el servidor para que un cliente (como el navegador o Bruno) pueda pedirle o enviarle datos por HTTP.

La API gestiona un catálogo de canciones. Permite consultar todas las canciones, buscar una por su id, filtrarlas por género y agregar canciones nuevas. Los datos se guardan en memoria, por lo que se reinician cada vez que se detiene la aplicación.


- Daniel Mateo Duque

## Requisitos

- Java 21 o superior
- Maven (o el `mvnw` incluido en el proyecto)

## Cómo ejecutarlo

Desde la carpeta raíz del proyecto, ejecuta en la terminal:

.\mvnw spring-boot:run


También se puede ejecutar desde VS Code con el botón **Run** sobre el método `main` de la clase `MusicaApiApplication`.

La aplicación queda disponible en `http://localhost:8080`.

## Endpoints

- `GET /canciones`: lista todas las canciones.
- `GET /canciones/{id}`: busca una canción por su id (responde 404 si no existe).
- `GET /canciones?genero=rock`: filtra las canciones por género.
- `POST /canciones`: crea una canción nueva a partir de un JSON (responde 201 Created).
- `DELETE /canciones/{id}`: elimina una canción por su id (responde 204 si se eliminó y 404 si no existe).

Ejemplo de JSON para el `POST`:

```json
{
  "titulo": "Africa",
  "artista": "Toto",
  "genero": "Rock",
  "anio": 1982
}
```