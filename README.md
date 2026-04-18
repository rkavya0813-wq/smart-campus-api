# Smart Campus API

Author: Kavya Dissanayake  
Student No: 20222048  
Module: Client-Server Architectures (5COSC022W)

## Overview
Smart Campus API is a plain JAX-RS (Jersey) REST service that manages rooms and sensors in a smart campus. It supports sensor readings, includes validation and error handling for common failure cases, and provides request/response logging plus a global exception handler for unexpected errors. Data is stored in-memory using `HashMap` and `ArrayList` (no database).

## Tech Stack
- Java 17
- JAX-RS (Jersey)
- Grizzly HTTP Server
- Maven
- Jackson (JSON)

## How to Build
```bash
mvn clean compile
```

## How to Run
```bash
mvn exec:java
```

Server runs on:
http://localhost:8080/

Base API URL:
http://localhost:8080/api/v1

## API Endpoints

### Root Endpoint
GET /api/v1

### Rooms
GET /api/v1/rooms
POST /api/v1/rooms
GET /api/v1/rooms/{roomId}
DELETE /api/v1/rooms/{roomId}

### Sensors
GET /api/v1/sensors
GET /api/v1/sensors?type=CO2
GET /api/v1/sensors/{sensorId}
POST /api/v1/sensors

### Sensor Readings
GET /api/v1/sensors/{sensorId}/readings
POST /api/v1/sensors/{sensorId}/readings

### Error Test Endpoint
GET /api/v1/error-test

## Sample curl Commands

1. Create Room
```bash
curl -i -X POST http://localhost:8080/api/v1/rooms \
	-H "Content-Type: application/json" \
	-d '{"id":"R101","name":"Lecture Hall 1","sensorIds":[]}'
```

2. Create Sensor
```bash
curl -i -X POST http://localhost:8080/api/v1/sensors \
	-H "Content-Type: application/json" \
	-d '{"id":"S1","type":"CO2","status":"ACTIVE","currentValue":400,"roomId":"R101"}'
```

3. Get All Sensors
```bash
curl -i http://localhost:8080/api/v1/sensors
```

4. Add Sensor Reading
```bash
curl -i -X POST http://localhost:8080/api/v1/sensors/S1/readings \
	-H "Content-Type: application/json" \
	-d '{"value":420,"timestamp":1710000000000}'
```

5. Test Validation Error (invalid room)
```bash
curl -i -X POST http://localhost:8080/api/v1/sensors \
	-H "Content-Type: application/json" \
	-d '{"id":"S2","type":"TEMP","status":"ACTIVE","currentValue":22,"roomId":"NO_SUCH_ROOM"}'
```

6. Test Global 500 error (/error-test)
```bash
curl -i http://localhost:8080/api/v1/error-test
```

## Error Handling
The API uses custom exceptions and JAX-RS `ExceptionMapper` implementations to return consistent JSON errors.

HTTP status codes used:
- 404 Not Found
- 403 Forbidden (sensor in maintenance)
- 409 Conflict (room not empty / duplicate)
- 422 Unprocessable Entity (invalid relationships)
- 500 Internal Server Error (global handler)

JSON error format:
```json
{
	"error": "message",
	"status": 500
}
```

## Logging
`LoggingFilter` logs:
- HTTP method
- Request URI
- Response status code
