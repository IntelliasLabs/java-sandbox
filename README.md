# Sandbox 

This application provides skeleton for starting new projects with a lot of boilerplate code.
In this project used Clean\Onion Architecture. As an example used ItemEntity as a base Domain model, 
backed by Repository, Service and Rest Controller, validation, logging. 
Tests written for each layer - Repository, Service, Controller and Integration tests for the whole application.

## Technology stack
* Maven
* Spring Boot 3
* Postgres Database
* Docker | Docker Compose
* Flyway
* MapStruct
* Lombok
* Swagger
* JUnit | Mockito | MockMvc | Testcontainers


## REST API

|           Endpoint	           | Method   | Req. body | Status |    Resp. body    | Description    		   	                                |
|:-----------------------------:|:--------:|:---------:|:------:|:----------------:|:-----------------------------------------------------|
|        `/api/v1/items`        | `GET`    |           |  200   |    ItemDTO[]     | Get all the items                                    |
|        `/api/v1/items`        | `POST`   |  ItemDTO  |  201   |     ItemDTO      | Add a new item                                       |
|     `/api/v1/items/{id}`      | `GET`    |           |  200   |     ItemDTO      | Get the item with the given ID.                      |
|                               |          |           |  404   |     ErrorDTO     | No item with the given ID exists.                    |
| `/api/v1/items/{id}/{locale}` | `GET`    |           |  200   | LocalizedItemDTO | Get the localized item with the given ID and Locale. |
|                               |          |           |  404   |     ErrorDTO     | No item with the given ID exists.                    |
|     `/api/v1/items/{id}`      | `PUT`    |  ItemDTO  |  200   |     ItemDTO      | Update the item with the given ID.                   |
|                               |          |           |  404   |     ErrorDTO     | No item with the given ID exists.                    |
|     `/api/v1/items/{id}`      | `DELETE` |           |  204   |                  | Delete the item with the given ID.                   |

[Swagger UI](http://localhost:8080/swagger-ui/index.html) is available.

## Useful Commands

| Maven Command	             | Description                                   |
|:---------------------------|:----------------------------------------------|
| `mvn spring-boot:run`      | Run the application.                          |
| `mvn clean install`        | Build the application.                        |
| `mvn clean test`           | Run tests.                                    |


## Running a PostgreSQL Database

Run PostgreSQL as a Docker container

```bash
docker run -d \
    --name sandbox-postgres \
    -e POSTGRES_USER=user \
    -e POSTGRES_PASSWORD=password \
    -e POSTGRES_DB=sandbox_db \
    -p 5432:5432 \
    postgres:14.4
```

or run via Docker Compose
```bash  
    cd docker
    docker-compose up
```

### Container Commands

| Docker Command	              | Description       |
|:-------------------------------:|:-----------------:|
| `docker stop sandbox-postgres`   | Stop container.   |
| `docker start sandbox-postgres`  | Start container.  |
| `docker remove sandbox-postgres` | Remove container. |

### Database Commands

Start an interactive PSQL console:

```bash
docker exec -it sandbox-postgres psql -U user -d sandbox_db
```

|     PSQL Command	     | Description                    |
|:---------------------:|:------------------------------:|
|        `\list`        | List all databases.            |
| `\connect sandbox_db` | Connect to specific database.  |
|         `\dt`         | List all tables.               |
|        `\quit`        | Quit interactive psql console. |

### Internationalization (i18n)

#### Data localization 
In a case you need translated or adjusted response according to the certain location, there is an example with translated currency 
code by requested locale on the `/api/v1/items/{id}/{locale}` endpoint. It translates Entity field `currencyCode` with the most 
matching locale among those present in ResourceBundle files in the `src/main/resources/resourcebundle` directory.

#### Exception localization
For translation of REST request arguments validation messages, there is a ValidationExceptionHandler class that handles  
MethodArgumentNotValidException type exceptions and translates its messages by the language header.
Validation happens before the REST controllers method invocation.

## Spring Actuator

The following Spring Actuator endpoints are used for monitoring application state and retrieving the corresponding data:

 - `/actuator` Use this endpoint to get a list of all endpoints enabled in Spring Actuator.
 - `/actuator/health` Use this endpoint to get the basic health information about the application.
 - `/actuator/metrics` Use this endpoint to get a list of available application metrics.
 - `/actuator/metrics/{MetricName}` Use this endpoint to receive data from the specific metric.
 - `/actuator/info` Use this endpoint to display the application-specific information.
 - `/actuator/httpexchanges` Use this endpoint to get traces of all the HTTP requests have been processed by the application.
 - `/actuator/flyway` Use this endpoint to get details about the Flyway database migrations.
 - `/actuator/beans` Use this endpoint to get all available beans created by the application.

## Authentication

The application utilizes the `Basic HTTP` authentication. It requires the `Authorization: Basic` header followed by the
`login:password` pair to be included in all requests made to the application. 
The login and password are specified in the [`credentials.properties`](src/main/resources/credentials.properties) file. 

To disable the authentication for the entire application, use the `app:security:enabled` property of the 
[`application.yml`](src/main/resources/application.yml).

Note, that the `login:password` pair used in the authorization header should be encoded in `base64` format:

```http request
### Get all items
GET http://localhost:8081/actuator
Authorization: Basic dXNlcjpwYXNzd29yZA==
```