# Música API

API REST desarrollada con Spring Boot que gestiona un catálogo de canciones y álbumes, guarda la información con JPA e Hibernate, consume una API externa de música (Deezer) y expone herramientas de observabilidad (métricas, logs, trazas y estado de salud).

## Contexto

El contexto elegido es la **música**. La API permite crear, consultar, actualizar y eliminar canciones (CRUD), buscarlas por género y por artista, agruparlas en álbumes, y buscar canciones reales en Deezer. Los datos se guardan en una base H2 en memoria, por lo que se reinician al detener la aplicación.

## Integrantes

- Daniel Mateo Duque

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA (`JpaRepository`) e Hibernate
- Base de datos H2 (en memoria) con consola web
- Jakarta Validation
- `RestClient` (consumo de API externa)
- Spring Boot Actuator, Micrometer y Prometheus
- Micrometer Tracing (Brave)
- SLF4J (logging)
- Maven

## Cómo ejecutarlo

Desde la carpeta raíz del proyecto, ejecuta en la terminal:

```
.\mvnw spring-boot:run
```

También se puede ejecutar desde VS Code con el botón **Run** sobre el método `main` de `MusicaApiApplication`. La aplicación queda disponible en `http://localhost:8080`.

Consola de H2 (`http://localhost:8080/h2-console`):

- **JDBC URL:** `jdbc:h2:mem:musicadb`
- **User Name:** `sa`
- **Password:** (vacía)

## Estructura del proyecto

```
musicaapi
├── catalogo        Entidades, repositorios, DTO y controladores de canciones y álbumes
├── deezer          Consumo de la API externa de Deezer
├── observabilidad  Health indicator, logs de ejemplo y trazas
└── MusicaApiApplication.java
```

## Endpoints

### Canciones

- `POST /cancion`: crea una canción (201 Created).
- `GET /cancion`: lista todas las canciones.
- `GET /cancion?genero=rock`: filtra por género.
- `GET /cancion/{id}`: busca una canción por id (404 si no existe).
- `GET /cancion/buscar?artista=Queen`: busca por artista (el parámetro es obligatorio).
- `PUT /cancion/{id}`: actualiza una canción (200 OK, o 404 si no existe).
- `DELETE /cancion/{id}`: elimina una canción (204 No Content, o 404 si no existe).

Reglas de validación para `POST` y `PUT`: `titulo`, `artista` y `genero` son obligatorios, el género debe tener entre 3 y 20 caracteres y el año debe ser un número positivo. Si no se cumplen, la API responde 400 Bad Request.

### Álbumes

- `POST /album`: crea un álbum (201 Created).
- `POST /album/{albumId}/cancion`: crea una canción asociada a un álbum (201 Created, o 404 si el álbum no existe). Relación `@ManyToOne`: muchas canciones pertenecen a un álbum.

### API externa (Deezer)

- `GET /deezer/buscar?texto=queen`: busca canciones en Deezer y devuelve título, artista y álbum. Si el servicio externo falla, responde 502 Bad Gateway con un mensaje de error.

### Observabilidad

- `GET /actuator/health`: estado de la aplicación, incluyendo un componente propio `canciones` que comprueba la base de datos.
- `GET /actuator/metrics`: lista de métricas disponibles.
- `GET /actuator/metrics/musica.canciones.creadas`: métrica propia que cuenta las canciones creadas.
- `GET /actuator/prometheus`: métricas en formato Prometheus.
- `GET /procesar?tipo=normal|advertencia|error`: genera logs de nivel INFO, WARN y ERROR.
- `GET /traza`: devuelve el `traceId` y el `spanId` de la solicitud.

## Ejemplo

Solicitud `POST /cancion`:

```json
{
  "titulo": "Bohemian Rhapsody",
  "artista": "Queen",
  "genero": "Rock",
  "anio": 1975
}
```

Respuesta `201 Created`:

```json
{
  "titulo": "Bohemian Rhapsody",
  "artista": "Queen",
  "genero": "Rock",
