# Phishing Filter

This project runs an integrated environment including PostgreSQL, Kafka (in KRaft mode), Cassandra, and the Phishing Filter Spring Boot application using Docker Compose.

## Technology Choices

- **PostgreSQL** is used for transactional behavior, providing reliable, ACID-compliant relational data storage.
- **Cassandra** handles eventual consistency with high throughput for key-value storage of phishing senders and URLs, including TTL (time-to-live). It supports horizontal scaling for growing data needs.
- **Kafka** provides scalability and makes handling message peaks easier, serving as a highly performant event streaming platform.
- **Spring Boot/Kotlin app** has been created in a modular monolith structure, allowing for horizontal scaling and the easy separation of modules into microservices if needed.

## API Usage

### Ingestion

Send SMS data to the API:

POST /api/sms
```json
{
  "sender": "234100200300",
  "recipient": "48700800999",
  "message": "Dzień dobry. W związku z audytem nadzór finansowy w naszym banku proszą o potwierdzanie danych pod adresem: https://www.m-baank.pl.ng/personal-data﻿"
}
```
Example Response:
```json
{
    "messageId": "bd739660-e5f2-4f9d-a946-f820df3734c2",
    "messageStatus": "PROCESSING"
}
```

### Get Status

Check the status of a submitted message by its ID:

GET /api/sms/:messageId

Example Response:
```json
{
    "messageId": "bd739660-e5f2-4f9d-a946-f820df3734c2",
    "messageStatus": "SKIPPED"
}
```

### Status Descriptions

- **PROCESSING**: The message is succesfully ingested and it is being processed.
- **SAFE**: The message is classified as safe.
- **PHISHING**: The message is detected as phishing because it contains potentially malicious url evaluated with Google WebRisk API.
- **SKIPPED**: The message was skipped from processing (e.g. recipient not subscribed).
- **PENDING**: External service issue (such cases are not handled in current versions)
- **UNKNOWN**: The status could not be determined (e.g. message not found)

## Prerequisites

- Docker and Docker Compose installed on your machine.
- Basic familiarity with Docker commands.

## Starting the Environment

1. Clone or download this repository.
2. Open a terminal in the directory containing the `docker-compose.yml` file.
3. Run the command:
docker compose up -d

4. Wait a few minutes for all services to start and become healthy.

## Services

- **PostgreSQL** (`postgres`): Relational database on port 5432.
- **Kafka** (`kafka`): Event streaming platform (KRaft mode) on ports 9092 and 9093.
- **Cassandra** (`cassandra`): NoSQL database on port 9042.
- **Phishing Filter App** (`phishing-filter`): Spring Boot application on port 8080.

Data volumes are used for persistence:
- `pgdata` for Postgres
- `kafka_data` for Kafka
- `cassandra_data` for Cassandra

## Accessing the Application

Once the containers are healthy, access the Phishing Filter API at:  
`http://localhost:8080`

## Stopping the Environment

To stop and remove all containers, execute:
docker compose down

## Troubleshooting

- Check container logs for errors:
docker compose logs <service-name>

- The application waits for dependent services to be healthy before starting.
- Startup times may vary; Kafka and Cassandra may take a few moments to initialize.
