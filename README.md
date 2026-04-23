# Smart Campus API

A RESTful API for managing rooms and sensors across a university campus, built using JAX-RS (Jersey 2.41) deployed on Apache Tomcat.

## Technology Stack

- **Framework:** JAX-RS (Jersey 2.41)
- **Server:** Apache Tomcat 9
- **Build Tool:** Maven
- **Data Storage:** In-memory HashMap and ArrayList
- **JSON:** Jackson

## Project Structure

com.smartcampus
├── AppConfig.java
├── exception
│   ├── GlobalExceptionMapper.java
│   ├── LinkedResourceNotFoundException.java
│   ├── LinkedResourceNotFoundExceptionMapper.java
│   ├── RoomNotEmptyException.java
│   ├── RoomNotEmptyExceptionMapper.java
│   ├── SensorUnavailableException.java
│   └── SensorUnavailableExceptionMapper.java
├── model
│   ├── ErrorMessage.java
│   ├── Room.java
│   ├── Sensor.java
│   └── SensorReading.java
└── resource
├── DiscoveryResource.java
├── LoggingFilter.java
├── RoomResource.java
├── SensorReadingResource.java
└── SensorResource.java

## How to Build and Run

### Prerequisites
- Java JDK 11 or higher
- Apache Maven
- Apache Tomcat 9
- NetBeans 18 (recommended)

### Steps
1. Clone the repository:https://github.com/manuji2003/smart-campus-api.git

2. Open the project in NetBeans

3. Right-click project → Clean and Build

4. Right-click project → Run (Tomcat starts automatically)

5. Visit: http://localhost:8080/smart-campus-tomcat/api/v1/

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/ | Discovery endpoint |
| GET | /api/v1/rooms | Get all rooms |
| POST | /api/v1/rooms | Create a room |
| GET | /api/v1/rooms/{id} | Get room by ID |
| DELETE | /api/v1/rooms/{id} | Delete a room |
| GET | /api/v1/sensors | Get all sensors |
| POST | /api/v1/sensors | Create a sensor |
| GET | /api/v1/sensors/{id} | Get sensor by ID |
| GET | /api/v1/sensors?type=X | Filter sensors by type |
| GET | /api/v1/sensors/{id}/readings | Get all readings |
| POST | /api/v1/sensors/{id}/readings | Add a reading |

## Sample curl Commands

### 1. Get API Discovery
```bash
curl -X GET http://localhost:8080/smart-campus-tomcat/api/v1/
```

### 2. Create a Room
```bash
curl -X POST http://localhost:8080/smart-campus-tomcat/api/v1/rooms \
-H "Content-Type: application/json" \
-d "{\"id\": \"LIB-301\", \"name\": \"Library Quiet Study\", \"capacity\": 50}"
```

### 3. Create a Sensor
```bash
curl -X POST http://localhost:8080/smart-campus-tomcat/api/v1/sensors \
-H "Content-Type: application/json" \
-d "{\"id\": \"TEMP-001\", \"type\": \"Temperature\", \"status\": \"ACTIVE\", \"currentValue\": 22.5, \"roomId\": \"LIB-301\"}"
```

### 4. Add a Sensor Reading
```bash
curl -X POST http://localhost:8080/smart-campus-tomcat/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d "{\"value\": 25.3}"
```

### 5. Get All Readings for a Sensor
```bash
curl -X GET http://localhost:8080/smart-campus-tomcat/api/v1/sensors/TEMP-001/readings
```

## Error Handling

| Status Code | Scenario |
|-------------|----------|
| 409 Conflict | Deleting a room that has sensors assigned |
| 422 Unprocessable Entity | Creating a sensor with non-existent roomId |
| 403 Forbidden | Adding reading to a MAINTENANCE sensor |
| 500 Internal Server Error | Any unexpected runtime error |

## Report - Question Answers

2. Open the project in NetBeans

3. Right-click project → Clean and Build

4. Right-click project → Run (Tomcat starts automatically)

5. Visit: http://localhost:8080/smart-campus-tomcat/api/v1/

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/ | Discovery endpoint |
| GET | /api/v1/rooms | Get all rooms |
| POST | /api/v1/rooms | Create a room |
| GET | /api/v1/rooms/{id} | Get room by ID |
| DELETE | /api/v1/rooms/{id} | Delete a room |
| GET | /api/v1/sensors | Get all sensors |
| POST | /api/v1/sensors | Create a sensor |
| GET | /api/v1/sensors/{id} | Get sensor by ID |
| GET | /api/v1/sensors?type=X | Filter sensors by type |
| GET | /api/v1/sensors/{id}/readings | Get all readings |
| POST | /api/v1/sensors/{id}/readings | Add a reading |

## Sample curl Commands

### 1. Get API Discovery
```bash
curl -X GET http://localhost:8080/smart-campus-tomcat/api/v1/
```

### 2. Create a Room
```bash
curl -X POST http://localhost:8080/smart-campus-tomcat/api/v1/rooms \
-H "Content-Type: application/json" \
-d "{\"id\": \"LIB-301\", \"name\": \"Library Quiet Study\", \"capacity\": 50}"
```

### 3. Create a Sensor
```bash
curl -X POST http://localhost:8080/smart-campus-tomcat/api/v1/sensors \
-H "Content-Type: application/json" \
-d "{\"id\": \"TEMP-001\", \"type\": \"Temperature\", \"status\": \"ACTIVE\", \"currentValue\": 22.5, \"roomId\": \"LIB-301\"}"
```

### 4. Add a Sensor Reading
```bash
curl -X POST http://localhost:8080/smart-campus-tomcat/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d "{\"value\": 25.3}"
```

### 5. Get All Readings for a Sensor
```bash
curl -X GET http://localhost:8080/smart-campus-tomcat/api/v1/sensors/TEMP-001/readings
```

## Error Handling

| Status Code | Scenario |
|-------------|----------|
| 409 Conflict | Deleting a room that has sensors assigned |
| 422 Unprocessable Entity | Creating a sensor with non-existent roomId |
| 403 Forbidden | Adding reading to a MAINTENANCE sensor |
| 500 Internal Server Error | Any unexpected runtime error |

## Report - Question Answers

### 1. Question: In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.
 When using JAX-RS, a resource instance is created for every incoming HTTP request. This is what we call per-request scope. So every request gets a brand object, which is good because one request for data will not get in the way of another.
This also creates a problem when we want to store data. Since every request gets an object, any data we store in a regular instance variable will be gone after the request is over. To get around this problem, I use variables like public static Map called rooms, which are shared by all instances and stay around for as long as the application is running. We have to be careful when using data in environments where many threads are running at the same time. We should use thread-safe collections like ConcurrentHashMap to prevent problems with multiple threads trying to access the data at the same time, which is what most people in the industry do.

### 2. Question: Why is the provision of “Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?
HATEOAS (Hypermedia as the Engine of Application State) refers to an API that contains links to actions and resources other than simply information within a response. For instance, if one were to request a room, the response would not only contain the data about the room but also include some hyperlinks such as “delete”: “/api/v1/rooms/LIB-301” and “sensors”: “/api/v1/rooms/LIB-301/sensors”.
It provides a benefit for developers because instead of having to remember or hard-code URLs, all one needs is to follow the hyperlinks given by the API. In addition, changes to the URL structure can be made without affecting any clients since they use hyperlinks to navigate through an application.

### 3. Question: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.
Sending back just ID:
•	Response will be small, with less bandwidth usage
•	Requires further calls to obtain more information about each room
•	Suitable in case the client wants to know which rooms are available

Sending back room object:

•	Bigger response, more bandwidth usage
•	All data will be returned with one request; no need to make further calls
•	Suitable in case the client wants to render the information right away

For the API for smart campus, I chose the option to send back room objects as the facility managers need all the information, such as the name, capacity, and sensors, at once.

### 4. Question: Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.
Yes, the DELETE method is idempotent in our implementation. "Idempotent" means that executing the same request several times leads to the same resulting server state.
•	The first DELETE request to LIB-301 results in the removal of the room and a response code of 200 OK.
•	The second DELETE request to LIB-301 leads to the fact that the room is not found and, as a result, an error message is returned with the status code 404 Not Found.
Both requests result in the server being in the same final state – the room has been deleted. It should be noted that although the return codes of the two requests are different (200 and 404), the server's final state is absolutely the same for both.

### 5. Question: We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?
If a client sends a request with the content type set to text/plain to the endpoint that uses the annotation @Consumes(MediaType.APPLICATION_JSON), then the JAX-RS framework refuses to process this request. The response code returned will be an HTTP 415 Unsupported Media Type.
The reason for this is simple – when a request comes in, the framework looks at its Content-Type header. If it finds a mismatch between the types, then the framework takes care of rejecting the request all by itself.

### 6. Question: You implemented this filtering using@QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?
Using @QueryParam like GET /api/v1/sensors?type=CO2 is better than a path like GET /api/v1/sensors/type/CO2 for these reasons:
•	Query parameters are optional — the base URL GET /api/v1/sensors still works perfectly without any filter. With path parameters, you would need a completely separate endpoint for the unfiltered version.
•	Query parameters are made for filtering — they are specifically designed in the HTTP standard for searching, filtering, and sorting collections. Path segments are meant to identify specific resources, not filter them.
•	Multiple filters are easy — you can combine query params naturally like ?type=CO2&status=ACTIVE. With path segments, this becomes messy and hard to read.

### 7. Question: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?
Using the Sub-Resource Locator pattern allows delegation of URL hierarchy to its own specific class. Instead of cramming all the reading logic into the SensorResource class, we have used the SensorReadingResource class to achieve that.
The advantages include:
Separation of Concerns - Each class has its specific responsibility, where the SensorResource is in charge of managing sensors, while the SensorReadingResource takes care of readings.
Scalability - As the API evolves, every resource class will remain small and compact without this pattern, where a single large controller class is responsible for handling hundreds of endpoints.
Reuse - The subclass has the flexibility of being instantiated with a different context depending on the parent resource (sensorId).
Testability — smaller, focused classes are much easier to unit test independently

### 8. Question: WhyisHTTP422oftenconsideredmoresemanticallyaccurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?
When a client tries to create a sensor with a roomId that does not exist, the request itself arrived correctly, and the JSON was valid and well-formed. The problem is not that a resource was not found in the traditional sense — the sensor URL exists and works fine. The problem is that the content of the request references something that does not exist.
HTTP 404 means the URL you requested does not exist. HTTP 422 means your request was understood, but the data inside it is semantically invalid. Since the issue is with the data inside a valid request — a broken reference to a non-existent room — 422 Unprocessable Entity is far more accurate and informative for the client developer. In our implementation, we throw a LinkedResourceNotFoundException, which is mapped to 422 by the LinkedResourceNotFoundExceptionMapper.

### 9. Question: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?
Exposing raw Java stack traces to external API consumers is dangerous for several reasons:
Reveals internal structure — an attacker can see exact class names, package names, and file paths of your application, making it easier to target specific components.
Reveals technology stack —stack traces expose which frameworks and libraries you use, such as Jersey 2.41 and Apache Tomcat, allowing attackers to look up known vulnerabilities in those specific versions.
Reveals business logic — method names and line numbers hint at how the application works internally, helping attackers craft more targeted attacks.
Aids injection attacks — if database errors leak through, attackers learn your data structure, table names, and query patterns.
Here GlobalExceptionMapper solves this by catching all unexpected errors and returning only a generic, structured ErrorMessage with a 500 status code, keeping all internal details completely hidden from the client.

### 10. Question: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?
It is much more efficient to use the same LoggingFilter that implements both ContainerRequestFilter and ContainerResponseFilter than to put logging manually into each resource method because:
Single responsibility - logging is a cross-cutting concern, so you need to implement logging in only one place, and not in dozens of resource methods.
Code reuse - when using a filter, each new endpoint comes already with implemented logging without any additional coding.stFilter and ContainerResponseFilter than to put logging manually into each resource method because:
