# E-Commerce Microservices Demo

A demo project illustrating a microservices architecture using Spring Boot and Spring Cloud. Built for learning purposes.

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Architecture](#2-architecture)
3. [Services Description](#3-services-description)
4. [Technologies Used](#4-technologies-used)
5. [Project Structure](#5-project-structure)
6. [Communication Flow](#6-communication-flow)
7. [Configuration Server](#7-configuration-server)
8. [How to Run Locally](#8-how-to-run-locally)
9. [Ports Reference](#9-ports-reference)
10. [Example Endpoints](#10-example-endpoints)
11. [Troubleshooting](#11-troubleshooting)

---

## 1. Project Overview

This project demonstrates a microservices-based backend where two domain services (students and courses) are independently deployed, register with a service discovery server, are exposed through a single API gateway, and receive their configuration from a central config server on startup.

---

## 2. Architecture

Each service registers independently with Eureka. The gateway resolves `lb://` URIs by querying Eureka at request time using Spring Cloud LoadBalancer. `microservice-course` calls `microservice-student` internally via Feign, also resolved through Eureka.

```
                    ┌──────────────────────┐
                    │   microservice-config │
                    │      (port 8888)      │
                    └──────────┬───────────┘
                               │ serves config on startup (all services)
         ┌─────────────────────┼──────────────────────┐
         │                     │                      │
         ▼                     ▼                      ▼
┌────────────────┐   ┌──────────────────┐   ┌──────────────────┐
│microservice-   │   │microservice-     │   │microservice-     │
│eureka          │   │student           │   │course            │
│(port 8761)     │   │(port 8090)       │   │(port 9090)       │
└───────▲────────┘   └────────▲─────┬──┘   └──▲──────────┬────┘
        │                     │     │          │          │
        │  registers          │     │          │          │ Feign call
        ├─────────────────────┘     └──────────┘          │
        │  registers                  registers            ▼
        │                                        ┌──────────────────┐
        │◄───────────────────────────────────────│microservice-     │
        │  registers + discovers lb:// URIs      │gateway           │
        │                                        │(port 8080)       │
        │                                        └────────▲─────────┘
        │                                                 │
        └─────────────────────────────────────────────────┘
                                              Client requests
```

---

## 3. Services Description

| Service | Responsibility |
|---|---|
| **microservice-config** | Central configuration server. On startup it serves full `application.yaml` configurations to all other services from its local classpath. |
| **microservice-eureka** | Service registry (Eureka Server). All other services register here by name, enabling address resolution without hardcoded URLs. |
| **microservice-gateway** | Single intended entry point for external requests. Routes traffic to domain services using Eureka-based load balancing. |
| **microservice-student** | Domain service. Manages student data. Persists to `studentDb` (MySQL). |
| **microservice-course** | Domain service. Manages course data and fetches enrolled students from `microservice-student` via Feign. Persists to `courseDb` (MySQL). |

---

## 4. Technologies Used

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Language |
| Spring Boot | 3.5.13 | Application framework |
| Spring Cloud | 2025.0.2 | Cloud-native tooling (Gateway, Eureka, Config, Feign) |
| Spring Cloud Gateway | — | API gateway and request routing |
| Spring Cloud Netflix Eureka | — | Service discovery and registration |
| Spring Cloud Config | — | Centralized configuration (native/filesystem mode) |
| Spring Cloud OpenFeign | — | Declarative REST client for inter-service calls |
| Spring Cloud LoadBalancer | — | Client-side load balancing for `lb://` URI resolution |
| Spring Data JPA / Hibernate | — | ORM and database access |
| MySQL | 8.x | Relational database (one schema per service) |
| Maven | 3.8+ | Multi-module build tool |

---

## 5. Project Structure

This is a Maven multi-module project. The root `pom.xml` declares all modules:

```
ecommerce-microservices-demo/        ← parent pom (packaging: pom)
├── microservice-config/             ← Spring Cloud Config Server
├── microservice-eureka/             ← Eureka Server
├── microservice-gateway/            ← Spring Cloud Gateway
├── microservice-student/            ← Student domain service
└── microservice-course/             ← Course domain service
```

You can build all modules at once from the root:

```bash
./mvnw clean install -DskipTests
```

Or build and run a specific module independently:

```bash
cd microservice-student
./mvnw spring-boot:run
```

---

## 6. Communication Flow

### External: Client → Gateway → Service

All external requests are intended to go through the gateway on port `8080`. The gateway matches the request path against its configured routes and forwards the request to the appropriate service.

The `lb://` URI scheme does **not** use DNS. It instructs Spring Cloud LoadBalancer to query the Eureka registry at request time, retrieve the list of healthy instances for the target service, and select one. The resolved address is a real IP and port reported by the registered instance.

```
GET http://localhost:8080/student/all
  └─► Gateway matches route: Path=/student/**
        └─► LoadBalancer queries Eureka for "microservice-student"
              └─► Forwards to http://<instance-ip>:8090/student/all
```

> **Note:** In this local demo, nothing prevents a client from calling a service directly (e.g. `http://localhost:8090/student/all`). The gateway is the intended entry point, but there is no network-level enforcement.

### Gateway Routes

| Route ID | Path Pattern | Resolved via |
|---|---|---|
| `students` | `/student/**` | `lb://microservice-student` |
| `courses` | `/course/**` | `lb://microservice-course` |

### Internal: Service → Service (Feign)

`microservice-course` calls `microservice-student` using a Feign client. The `name` attribute in `@FeignClient` is the **Eureka service ID** — the value of `spring.application.name` that `microservice-student` used when it registered. Feign uses Spring Cloud LoadBalancer to resolve it, same as the gateway.

```java
@FeignClient(name = "microservice-student")  // "microservice-student" is the Eureka service ID
public interface StudentClient {
    @GetMapping("/student/searchByCourseId/{id}")
    List<StudentDto> findStudentsByCourseId(@PathVariable("id") Long courseId);
}
```

This call is triggered when a client requests `GET /course/search-students/{id}` — the course service fetches the matching students internally before composing the response.

---

## 7. Configuration Server

### How it works

`microservice-config` runs in **native mode**, meaning it reads configuration files directly from a local directory on its classpath rather than from a Git repository. This is a dev/demo setup — in production, native mode would typically read from an external filesystem path or a Git-backed store.

**Location of config files inside `microservice-config`:**

```
microservice-config/
└── src/main/resources/
    └── configurations/
        ├── microservice-eureka.yaml
        ├── microservice-student.yaml
        ├── microservice-course.yaml
        └── microservice-gateway.yaml
```

### File naming convention

Each file must be named **exactly** after the value of `spring.application.name` in the corresponding service. The match is case-sensitive. For example, a service with `name: microservice-student` will only match `microservice-student.yaml` — not `Microservice-Student.yaml` or `student.yaml`.

### Minimal local configuration

Each microservice's local `application.yaml` is intentionally minimal. It only declares its own name and where to find the config server:

```yaml
spring:
  application:
    name: microservice-student    # must match the filename in configurations/
  config:
    import: optional:configserver:http://localhost:8888
```

The `optional:` prefix means the service will **not fail to start** if the config server is unreachable. It will instead start with only the local properties, which in this setup means it will be missing its port, database credentials, and Eureka URL. Always ensure the config server is running before starting any other service.

On startup, each service contacts the config server, sends its application name, and receives the corresponding full configuration file.

---

## 8. How to Run Locally

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8.x running locally

Create the two required schemas:

```sql
CREATE DATABASE studentDb;
CREATE DATABASE courseDb;
```

> **Credentials warning:** MySQL credentials are currently hardcoded in the config files as `root` / `password.123`. Update `microservice-student.yaml` and `microservice-course.yaml` inside `microservice-config/src/main/resources/configurations/` to match your local MySQL credentials before starting. Never commit real credentials — use environment variables or a secrets manager in any non-local environment.

### Startup Order

Services must start in this exact order. Wait for each service to finish starting before proceeding to the next step.

**Step 1 — Config Server** *(must be first — all services fetch config from here on startup)*
```bash
cd microservice-config
./mvnw spring-boot:run
```

**Step 2 — Eureka Server** *(must be up before domain services and gateway register)*
```bash
cd microservice-eureka
./mvnw spring-boot:run
```

Verify Eureka is running: [http://localhost:8761](http://localhost:8761)

**Step 3 — Domain Services** *(require both config server and Eureka to be healthy)*
```bash
cd microservice-student
./mvnw spring-boot:run
```
```bash
cd microservice-course
./mvnw spring-boot:run
```

**Step 4 — Gateway**
```bash
cd microservice-gateway
./mvnw spring-boot:run
```

---

## 9. Ports Reference

| Service | Port |
|---|---|
| microservice-config | `8888` |
| microservice-eureka | `8761` |
| microservice-gateway | `8080` |
| microservice-student | `8090` |
| microservice-course | `9090` |

---

## 10. Example Endpoints

All requests below go through the gateway (`http://localhost:8080`).

### Students

| Method | Path | Description |
|---|---|---|
| `POST` | `/student/create` | Create a student |
| `GET` | `/student/all` | List all students |
| `GET` | `/student/search/{id}` | Find student by ID |
| `GET` | `/student/searchByCourseId/{courseId}` | Find students by course ID |

**POST /student/create — request body:**
```json
{
  "name": "Luis",
  "lastName": "Farfan",
  "email": "luis@example.com",
  "courseId": 1
}
```

### Courses

| Method | Path | Description |
|---|---|---|
| `POST` | `/course/create` | Create a course |
| `GET` | `/course/all` | List all courses |
| `GET` | `/course/search/{id}` | Find course by ID |
| `GET` | `/course/search-students/{id}` | Find course with enrolled students (triggers Feign call) |

**POST /course/create — request body:**
```json
{
  "name": "Microservices with Spring Boot",
  "teacher": "John Doe"
}
```

### curl Examples

```bash
# Create a course
curl -X POST http://localhost:8080/course/create \
  -H "Content-Type: application/json" \
  -d '{"name": "Microservices with Spring Boot", "teacher": "John Doe"}'

# Create a student enrolled in course 1
curl -X POST http://localhost:8080/student/create \
  -H "Content-Type: application/json" \
  -d '{"name": "Luis", "lastName": "Farfan", "email": "luis@example.com", "courseId": 1}'

# Get all students
curl http://localhost:8080/student/all

# Get a course with its enrolled students (triggers Feign internally)
curl http://localhost:8080/course/search-students/1
```

---

## 11. Troubleshooting

**Service starts with missing configuration (wrong port, no DB connection)**
- The config server is not running or is unreachable.
- Because `config.import` uses `optional:`, the service will start without fetching remote config.
- Fix: ensure `microservice-config` is fully started on port `8888` before starting any other service.

**Service fails to register with Eureka on startup**
- Eureka is not yet ready. Spring will retry registration automatically — wait a few seconds and check the Eureka dashboard at [http://localhost:8761](http://localhost:8761).

**MySQL connection refused**
- MySQL is not running, or the credentials in the config files do not match your local setup.
- Update `microservice-student.yaml` and `microservice-course.yaml` in `configurations/` with the correct credentials and restart the config server.

**Feign call fails: `microservice-student` not found**
- `microservice-student` is not registered in Eureka. Check that it started correctly and appears in the Eureka dashboard before sending requests to `/course/search-students/{id}`.

**Gateway returns 404 for a valid path**
- Confirm the gateway started after Eureka and that the target service is registered.
- Check that the request path matches the configured route patterns (`/student/**` or `/course/**`).
