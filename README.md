# Música API

API REST desarrollada con Spring Boot que gestiona un catálogo de canciones y álbumes, guarda la información en MySQL con JPA e Hibernate, consume una API externa de música (Deezer) y expone herramientas de observabilidad (métricas, logs, trazas y estado de salud).

## Contexto

El contexto elegido es la **música**. La API permite crear, consultar, actualizar y eliminar canciones (CRUD), buscarlas por género y por artista, agruparlas en álbumes, y buscar canciones reales en Deezer.

## Integrantes

- Daniel Mateo Duque

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA (`JpaRepository`) e Hibernate
- MySQL
- Jakarta Validation
- `RestClient` (consumo de API externa)
- Spring Boot Actuator, Micrometer y Prometheus
- Micrometer Tracing (Brave)
- SLF4J (logging)
- Maven

## Configuración de MySQL

1. Tener un servidor MySQL activo en `localhost:3306`.
2. La aplicación usa la base de datos `musica_db`. Se crea automáticamente al arrancar si no existe. Si prefieres crearla a mano:

```sql
CREATE DATABASE musica_db;
```

3. La conexión se configura en `src/main/resources/application.properties`. Por defecto usa el usuario `root`.
4. **La contraseña no está en el repositorio.** Se lee de la variable de entorno `MYSQL_PASSWORD`. Si tu usuario `root` tiene contraseña, defínela antes de ejecutar (en PowerShell):

```powershell
$env:MYSQL_PASSWORD='tu_contraseña'
```

Si tu `root` no tiene contraseña, no hace falta definir nada. Hibernate crea las tablas `album` y `cancion` automáticamente.

## Cómo ejecutarlo

Desde la carpeta raíz del proyecto, en la misma terminal donde definiste la variable, ejecuta:

```
.\mvnw spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

## Estructura del proyecto

```
musicaapi
├── catalogo        Entidades, repositorios, DTO y controladores de canciones y álbumes
├── deezer          Consumo de la API externa de Deezer
├── observabilidad  Health indicator, logs de ejemplo y trazas
└── MusicaApiApplication.java
```

## Entidades y relación

- **Album**: `id` y `nombre`.
- **Cancion**: `id`, `titulo`, `artista`, `genero`, `anio` y `album`.

La relación es **muchos a uno** (`@ManyToOne`): muchas canciones pertenecen a un álbum, y una canción puede no tener álbum. En la base de datos, la tabla `cancion` tiene la columna `album_id`, que es una llave foránea hacia la tabla `album`.

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
- `POST /album/{albumId}/cancion`: crea una canción asociada a un álbum (201 Created, o 404 si el álbum no existe).

## API externa utilizada

La aplicación consume la **API pública de Deezer** (`https://api.deezer.com`), que no requiere clave ni registro. Se usa `RestClient` para consultar su buscador (`/search?q=...`).

- `GET /deezer/buscar?texto=queen`: busca canciones en Deezer y devuelve título, artista y álbum.

**Manejo de errores:** si Deezer no responde o falla la conexión, la aplicación lo registra en el log con nivel ERROR y responde `502 Bad Gateway` con un mensaje claro, en vez de un error genérico.

## Observabilidad

La aplicación usa Spring Boot Actuator y Micrometer. Los endpoints están disponibles en `http://localhost:8080`:

- `GET /actuator/health`: estado de la aplicación, con un indicador propio llamado `canciones` que comprueba que la base de datos responde y muestra el total de canciones.
- `GET /actuator/metrics`: lista de métricas disponibles.
- `GET /actuator/metrics/musica.canciones.creadas`: **métrica personalizada** que cuenta las canciones creadas con `POST /cancion`.
- `GET /actuator/prometheus`: métricas en formato Prometheus.
- `GET /procesar?tipo=normal|advertencia|error`: genera logs de nivel INFO, WARN y ERROR, que se ven en la terminal.
- `GET /traza`: devuelve el `traceId` y el `spanId` de la solicitud. Estos identificadores también aparecen en las líneas de log.

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
  "anio": 1975,
  "album": null,
  "id": 1
}
```