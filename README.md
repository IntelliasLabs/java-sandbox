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

|       Endpoint	       | Method   | Req. body | Status | Resp. body | Description    		   	              |
|:---------------------:|:--------:|:---------:|:------:|:----------:|:-----------------------------------|
|    `/api/v1/items`    | `GET`    |           |  200   | ItemDTO[]  | Get all the items                  |
|    `/api/v1/items`    | `POST`   |  ItemDTO  |  201   |  ItemDTO   | Add a new item                     |
| `/api/v1/items/{id}`  | `GET`    |           |  200   |  ItemDTO   | Get the item with the given ID.    |
|                       |          |           |  404   |  ErrorDTO  | No item with the given ID exists.  |
| `/api/v1/items/{id}`  | `PUT`    |  ItemDTO  |  200   |  ItemDTO   | Update the item with the given ID. |
|                       |          |           |  404   |  ErrorDTO  | No item with the given ID exists.  |
| `/api/v1/items/{id}`  | `DELETE` |           |  204   |            | Delete the item with the given ID. |


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
