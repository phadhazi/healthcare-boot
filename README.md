# FHIR Patient Service

This project is a Spring Boot-based RESTful API service that implements HL7 FHIR R4 standards to manage **Patient** and **Organization** resources. It uses the **HAPI FHIR** library to expose standard-compliant endpoints and supports operations like **create**, **read**, and **search**.

---

## 🚀 Features

- FHIR R4-compliant REST API using HAPI FHIR.
- Create and fetch `Patient` and `Organization` resources.
- Role-based authorization using JWT (Read / Write roles).
- Supports both local and remote deployments.
- Postman collections available for easy testing.

---

## 🧾 Technologies Used

- **Java 17+**
- **Spring Boot**
- **HAPI FHIR** (Server)
- **JWT** for authentication
- **PostgreSQL** (Assumed for persistence)
- **Docker** + **Kubernetes** support
- **Postman** for API testing

---

## 📂 Project Structure

├── .github/workflows # GitHub Actions workflows (CI/CD)
├── .idea # IntelliJ project settings
├── kubernetes # Kubernetes manifests
├── postman # Postman collections (local & test server)
├── src
│ ├── main
│ │ ├── java
│ │ │ └── com.example.fhirpatientservice
│ │ │ ├── config
│ │ │ ├── model
│ │ │ ├── provider # FHIR resource providers (Patient, Organization)
│ │ │ ├── repository
│ │ │ ├── service
│ │ │ └── util # Transformer between FHIR & internal models
│ │ └── resources
│ └── test
│ └── java
│ └── com.example.fhirpatientservice.service
└── target # Build artifacts



---

## 🌐 Endpoints

The FHIR API is exposed via the following base URLs:

- **Localhost:** `http://localhost:8080/fhir`
- **Test Server:** `http://91.229.23.226:30080/fhir`

### Supported Resources

- `Patient`
    - `@Read`: `/Patient/{id}`
    - `@Search`: `/Patient?family={lastName}`
    - `@Create`: `POST /Patient`

- `Organization`
    - `@Read`: `/Organization/{id}`
    - `@Search`: `/Organization`
    - `@Create`: `POST /Organization`

---

## 🔐 Authentication

JWT-based authentication is used with two roles:

| Role | Description | Example JWT Token |
|------|-------------|-------------------|
| **READ** | Can read/search data | `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGUiOiJSRUFEIiwiZXhwIjo5OTk5OTk5OTk5fQ.WGefMCq-Tr5KnSN2FWdgAg9Jsjc1SbfYlgzRjHZ3aTA` |
| **WRITE** | Can create data | `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGUiOiJXUklURSIsImV4cCI6OTk5OTk5OTk5OX0.laNvC5NyccKLdbUVx8Sv2H7FIarb73d_C142IVWMaxo` |

Use these tokens in Postman or other tools:

**Header:**



---

## 📦 Postman Collections

The project includes 4 Postman collections:

- ✅ Localhost - Patient
- ✅ Localhost - Organization
- ✅ Test Server - Patient
- ✅ Test Server - Organization

> Collections are located in the `/postman` directory.

---

## 🧪 Testing

- Run unit tests using:
```bash
./mvnw test
