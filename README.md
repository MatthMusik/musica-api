# Música API

API REST desarrollada con Spring Boot que gestiona un catálogo de canciones y guarda la información en una base de datos mediante JPA e Hibernate.

## Contexto

El contexto elegido es la **música**. La API permite crear, consultar, actualizar y eliminar canciones (operaciones CRUD) y filtrarlas por género. Los datos se almacenan en una base de datos H2 en memoria, por lo que se reinician cada vez que se detiene la aplicación.

## Integrantes

- Daniel Mateo Duque

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA (`JpaRepository`)
- Hibernate
- Base de datos H2 (en memoria)
- Jakarta Validation
- Maven

## Cómo ejecutarlo

Desde la carpeta raíz del proyecto, ejecuta en la terminal:

```
.\mvnw spring-boot:run
```

También se puede ejecutar desde VS Code con el botón **Run** sobre el método `main` de la clase `MusicaApiApplication`.

La aplicación queda disponible en `http://localhost:8080`.

Para consultar la base de datos directamente, abre `http://localhost:8080/h2-console` con estos datos:

- **JDBC URL:** `jdbc:h2:mem:musicadb`
- **User Name:** `sa`
- **Password:** (vacía)

## Estructura del proyecto

- `Cancion`: entidad JPA que representa la tabla `cancion`.
- `RepositorioCanciones`: repositorio basado en `JpaRepository`, con una consulta personalizada por género.
- `CancionesRequest`: DTO con las reglas de validación de los datos recibidos.
- `CancionController`: controlador con los endpoints de la API.

## Endpoints

- `POST /cancion`: crea una canción nueva (responde 201 Created).
- `GET /cancion`: lista todas las canciones.
- `GET /cancion?genero=rock`: filtra las canciones por género (consulta personalizada).
- `GET /cancion/{id}`: busca una canción por su id (responde 404 si no existe).
- `PUT /cancion/{id}`: actualiza una canción existente (responde 200 OK, o 404 si no existe).
- `DELETE /cancion/{id}`: elimina una canción (responde 204 No Content, o 404 si no existe).

Reglas de validación para `POST` y `PUT`: `titulo`, `artista` y `genero` son obligatorios, el género debe tener entre 3 y 20 caracteres y el año debe ser un número positivo. Si no se cumplen, la API responde 400 Bad Request.

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
  "id": 1,
  "titulo": "Bohemian Rhapsody",
  "artista": "Queen",
  "genero": "Rock",
  "anio": 1975
}
```