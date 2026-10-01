# Employee REST API

A personal Spring Boot REST API project for employee management and user authentication.

## Technology

* Java 21
* Spring Boot
* Spring Data JPA / Hibernate
* Spring Security with JWT
* PostgreSQL
* Flyway
* Maven
* JUnit
* Docker / Docker Compose
* OpenAPI / Swagger UI

## Features

* Employee CRUD operations
* Request validation and error handling
* User registration and login
* JWT authentication and role-based authorization
* Database migrations with Flyway
* Unit and integration tests
* Docker Compose support

## Running the Application

Create a local `.env` file using `.env.example` as a template and provide the required values.

To run with Maven on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Or with Docker Compose:

```powershell
docker compose up --build
```

The API runs at:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

## Testing

```powershell
.\mvnw.cmd clean test
```

The project uses Flyway migrations to create and update the database schema.

> This is a personal learning and portfolio project.
