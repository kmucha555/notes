# Super Notes App

## Technologies

* Java 17
* Micronaut
* MongoDB

## Dependencies

* MongoDB

## How to start?

### Locally
* Ensure that you have MongoDB running on localhost:27017
* Set env variable: `MICRONAUT_ENVIRONMENTS=local`
* Run `./gradlew run`

### Using docker
* `docker compose build`
* `docker compose up`

## API Documentation
After service startup the documentation is reachable under:

* http://localhost:8080/swagger/super-notes-1.0.0.yml
* http://localhost:8080/swagger-ui/index.html

You can use Swagger documentation to play with an API. 