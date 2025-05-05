# Race Management Microservice

This microservice is responsible for managing the lifecycle of races and their participants (horses).

## Architecture

This project uses a hexagonal architecture (ports and adapters) divided into 3 main layers:

1. **Domain**: Contains business entities (Race, Participant) and ports (interfaces) to interact with the outside world.
2. **Application**: Contains services that implement the application's use cases.
3. **Infrastructure**: Contains adapters that implement the domain ports (persistence, messaging, REST API).

### Benefits of Hexagonal Architecture

- The domain is independent of technical details
- Facilitates unit testing (thanks to interfaces)
- Promotes separation of concerns

## Business Rules

- A race takes place on a given day and has a name and a unique number for that day
- A race must have a minimum of 3 participants
- Each participant has a name and a number
- Participants in a race are numbered starting from 1, without duplicates or gaps

## API Endpoints

### Create a Race
`POST /api/courses`

**Request:**
```json
{
  "nom": "Grand Prix de Paris",
  "numero": 1,
  "date": "2025-05-05",
  "partants": [
    {
      "nom": "Bucephalus",
      "numero": 1
    },
    {
      "nom": "Pegasus",
      "numero": 2
    },
    {
      "nom": "Shadowfax",
      "numero": 3
    }
  ]
}
```

**Response:** (201 Created)
```json
{
  "id": 1,
  "nom": "Grand Prix de Paris",
  "numero": 1,
  "date": "2025-05-05",
  "partants": [
    {
      "id": 1,
      "nom": "Bucephalus",
      "numero": 1
    },
    {
      "id": 2,
      "nom": "Pegasus",
      "numero": 2
    },
    {
      "id": 3,
      "nom": "Shadowfax",
      "numero": 3
    }
  ]
}
```

### Get Race by ID
`GET /api/courses/{id}`

## Event Streaming

When a race is created, an event is published to a Kafka topic:

- Topic: `course-created`
- Key: Race ID
- Value: JSON representation of the race data

## Development Setup

### Prerequisites
- Java 21
- Maven
- Docker and Docker Compose

### Running the Application

1. Start the required infrastructure using Docker Compose:
   ```
   docker-compose up -d
   ```

2. Build and run the application:
   ```
   mvn clean package
   mvn spring-boot:run
   ```

3. Access the API documentation:
   ```
   http://localhost:8080/swagger-ui.html
   ```

4. Monitor Kafka topics with AKHQ:
   ```
   http://localhost:8181
   ```

## Running Tests 

The application includes multiple test layers to ensure code quality and functionality:

### Run All Tests
```bash
mvn test
```

### Run Specific Test Types

#### Unit Tests
Tests the business logic in isolation, primarily focused on the domain layer:
```bash
mvn test -Dtest=*Test
```

#### Integration Tests
Tests the integration between components, including database operations:
```bash
mvn test -Dtest=*IntegrationTest
```

#### API Tests
Tests the REST endpoints:
```bash
mvn test -Dtest=CourseControllerTest
```

#### Kafka Messaging Tests
Tests the Kafka message publishing functionality:
```bash
mvn test -Dtest=CourseEventPublisherAdapterTest
```

### Test Environment

Tests use:
- H2 in-memory database for persistence tests
- Embedded Kafka for messaging tests

No need to run external dependencies for testing as they are automatically configured.

### Test Coverage

To generate test coverage reports:
```bash
mvn jacoco:report
```

Then open `target/site/jacoco/index.html` in your browser to view the coverage report.

## Technologies Used

- Spring Boot 3.2
- Spring Data JPA
- PostgreSQL
- Apache Kafka
- OpenAPI (Swagger)
- JUnit 5
- Docker