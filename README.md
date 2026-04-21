# Smart Campus API

Author: Rashmi Kavya Dissanayake  
Student No: 20222048  
Module: Client-Server Architectures (5COSC022W)

## Overview
This coursework project implements a simple REST API for a Smart Campus. It exposes rooms, sensors, and sensor readings using plain JAX-RS (Jersey) with in-memory storage, validation, and consistent JSON error responses. All data is stored in-memory using Java collections (HashMap and ArrayList), with no database used.

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
  -d '{"id":"R101","name":"Lecture Hall 1","capacity":120,"sensorIds":[]}'
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

5. Create Sensor with Invalid roomId (422)
```bash
curl -i -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"S2","type":"CO2","status":"ACTIVE","currentValue":410,"roomId":"INVALID"}'
```

6. Get Sensor by ID
```bash
curl -i http://localhost:8080/api/v1/sensors/S1
```

7. Add Sensor Reading
```bash
curl -i -X POST http://localhost:8080/api/v1/sensors/S1/readings \
  -H "Content-Type: application/json" \
  -d '{"value":420}'
```

8. Get Sensor Reading History
```bash
curl -i http://localhost:8080/api/v1/sensors/S1/readings
```

9. Filter Sensors by Type
```bash
curl -i "http://localhost:8080/api/v1/sensors?type=CO2"
```

10. Delete Room
```bash
curl -i -X DELETE http://localhost:8080/api/v1/rooms/R101
```

## Conceptual Report Answers

INFORMATICS INSTITUTE OF TECHNOLOGY
In collaboration with
UNIVERSITY OF WESTMINSTER

5COSC022C.2
CLIENT-SERVER ARCHITECTURES

Name: Rashmi Kavya Dissanayake
UOW ID: w19856926
IIT NO: 20222048

## Question 1
In JAX-RS, the resource classes are usually instantiated on a request-by-request basis; that is, a new instance is created with each incoming HTTP request. This helps ensure that request-specific data is not accidentally shared between different clients.
In my implementation resource classes do not contain shared data. Rather, the data of all applications are stored in a central in-memory store in the form of a HashMap and ArrayList. This enables the data to be maintained between requests.
But these data structures are created to be shared globally across requests; therefore, they can be accessed or changed by more than one request. This poses a risk of race in a real-world concurrent system. Whereas my implementation would perform well in a controlled setting, a production system would need thread-safe data structures, like ConcurrentHashMap, or adequate synchronization to prevent data inconsistencies. 
In my implementation, this common data is handled by a central DataStore class that utilizes HashMaps to keep track of rooms, sensors, and readings, accessible by all resource classes.

## Question 2
Hypermedia (HATEOAS) is regarded as an enhanced principle of REST since it enables the client to dynamically determine what actions are available based on links contained in responses from the API.
Instead of relying entirely on static documentation, the API provides navigational links (e.g., links to rooms or sensors), making the system more self-descriptive. This enables the clients to communicate with the API using links instead of hardcoding endpoint addresses (/api/v1/rooms).
This reduces how tightly the client depends on fixed endpoint URLs. In case of a modification of the API structure, clients are able to operate properly provided they adhere to the links, which are offered in the responses. It enhances maintainability and flexibility as well.
The discovery endpoint in my implementation exhibits a simplistic variant of hypermedia because, in this case, it offers links to core resources, thus aiding the client in navigating the API. However, a fully HATEOAS-compliant API would include links in most responses, not just the root endpoint.

## Question 3
Only sending room IDs will help to decrease the volume of the response and this will enhance efficiency of the network and minimise the usage of bandwidth. Nevertheless, it demands that the client make more calls to the API to get complete information on each room.
However, returning full room objects results in all the required information in a single response, making it easy to process on the client side and keeping the number of requests small.
Full room objects are returned in this implementation since it is more usable and it is easier to work with the API. This will make the response size a bit larger but is a fair trade-off with improved client experience and less complexity.
In this coursework, the GET /rooms endpoint delivers complete room objects, improving client-side usability

## Question 4
Yes, the DELETE operation in this implementation is idempotent, as there is no change in the final state of the system due to repeating the same request. For example, the first DELETE request to /rooms/R101 removes the room from the data store. If the same request is sent again, the API returns a 404 Not Found response because the room no longer exists.
Even though the response varies, the condition of the system does not change after initial deletion. Thus, the operation meets the idempotency definition, since the effect of several requests of the same type is the same on the system.
This behavior is evident in the DELETE /rooms/{roomId} endpoint within this implementation.

## Question 5
The @Consumes(MediaType.APPLICATION_JSON) annotation can be used to make sure that the API only receives requests that contain a JSON payload.
When a client transmits data in another format, like text/plain or application/xml, the JAX-RS cannot convert the request body to the Java object. This is due to the fact that there is no appropriate message body reader that can handle this type of content.
Consequently, it will fail to reach the method logic, and the server will send back a 415 Unsupported Media Type response.
This mechanism creates uniformity of the data format, and only valid and expected input gets handled by the API.
This is enforced in the POST endpoints of this API, which accept only JSON requests.

## Question 6
Filtering with @QueryParam is usually desirable as it permits flexible and optional filtering of collections of resources with no alteration of the endpoint structure.
The query parameters are scalable since they are search and filterable. As an illustration, it is possible to add several filters (e.g. /sensors?type=CO2&status=ACTIVE) easily.
Conversely, with path parameters like /sensors/type/CO2, the endpoints are inflexible and cannot be easily scaled in cases where more than one filter is required.
In this approach, filtering is managed through a query parameter in the endpoint /api/v1/sensors?type=CO2, allowing flexible retrieval of sensors based on their type.
Path parameters are better suited to locate a particular resource, whereas query parameters are more appropriate to filter collections. Consequently, query parameters lead to a better, more maintainable and extensible API design.

## Question 7
The sub-resource locator pattern enhances API design by assigning the responsibility to independent resource classes rather than allocating it all in one large controller.
In my implementation, the SensorResource takes care of overall sensor functions, whereas the SensorReadingResource is in charge of overseeing sensor readings via the path /sensors/{sensorId}/readings.
This separation enhances the clarity, upkeep, and structure of the code. It facilitates future API extensions, such as incorporating additional features linked to sensor readings without impacting the core sensor logic.
This approach also supports scalability, as new features related to sensor readings can be added without affecting existing sensor logic.

## Question 8
The HTTP 422 (Unprocessable Entity) is more semantically correct when the format of the request is correct, but the data within the request is invalid.
In this scenario, the endpoint exists and the JSON format is accurate, but the data given includes an incorrect reference. For instance, when a client requests the creation of a sensor with roomId = "R999" and that room is nonexistent, the issue lies with the data, not the endpoint.
A 404 Not Found response is usually the response to a resource being not found in the request URI. But here the URI is valid, and the issue is a missing referred resource in the payload.
Hence, HTTP 422 has a better and more meaningful representation of the error.

## Question 9
Allowing internal Java stack traces to be exposed to API consumers can be very dangerous in security terms.
Stack traces can be sensitive, containing class names, package structure, file paths, method names and line numbers. This information may provide attackers with information on how the system is internally organized.
Attackers can use this information to identify possible vulnerabilities in the system. One of such examples is that they can take advantage of the weaknesses known in some libraries or frameworks.
To avoid this, APIs must send general error messages to the clients and record detailed stack traces on the servers to debug. This makes it safe in addition to enabling developers to troubleshoot.

## Question 10
Applying JAX-RS filters to cross-cutting concerns, like logging, is beneficial since the functionality is concentrated in a single location and is applicable to various application areas.
Rather than inserting logging statements within each resource method, filters will automatically intercept all requests and responses going in and out. This will ensure consistent logging across the entire API.This will minimize code duplication and ensure that resource classes remain business-orientated. It is also an easy way to maintain, where logging behavioral changes can be made at one point.
In general, filtering leads to cleaner, more modular and maintainable code.
