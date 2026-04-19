# Smart Campus API

Author: Kavya Dissanayake  
Student No: 20222048  
Module: Client-Server Architectures (5COSC022W)

## Overview
This coursework project implements a simple REST API for a Smart Campus. It exposes rooms, sensors, and sensor readings using plain JAX-RS (Jersey) with in-memory storage, validation, and consistent JSON error responses.

## Tech Stack
- Java 17
- JAX-RS (Jersey)
- Grizzly HTTP Server
- Maven
- Jackson (JSON)

## Build Instructions
1. Open a terminal in the project root.
2. Run:
```bash
mvn clean compile
```

## Run Instructions
1. From the project root, run:
```bash
mvn exec:java
```
2. The server will start at http://localhost:8080/

## Base URL and API Base Path
- Base URL: http://localhost:8080/
- API Base Path: http://localhost:8080/api/v1

## API Design Summary
Resources are organized by RESTful paths and use JSON request/response bodies. Room and sensor data is managed in memory, and sensor readings are modeled as a sub-resource of a sensor.

## Sample curl Commands

1. Discovery (API metadata)
```bash
curl -i http://localhost:8080/api/v1
```

2. Create Room
```bash
curl -i -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"R101","name":"Lecture Hall 1","sensorIds":[]}'
```

3. Get All Rooms
```bash
curl -i http://localhost:8080/api/v1/rooms
```

4. Create Sensor
```bash
curl -i -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"S1","type":"CO2","status":"ACTIVE","currentValue":400,"roomId":"R101"}'
```

5. Get Sensor by ID
```bash
curl -i http://localhost:8080/api/v1/sensors/S1
```

6. Add Sensor Reading
```bash
curl -i -X POST http://localhost:8080/api/v1/sensors/S1/readings \
  -H "Content-Type: application/json" \
  -d '{"value":420}'
```

7. Filter Sensors by Type
```bash
curl -i "http://localhost:8080/api/v1/sensors?type=CO2"
```

8. Delete Room
```bash
curl -i -X DELETE http://localhost:8080/api/v1/rooms/R101
```
