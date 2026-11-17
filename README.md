# Tourist Record Management API

A Spring Boot 3 REST API for a hotel check-in system. It stores tourist contact information and visited destinations in PostgreSQL. Tourist endpoints require a validated JWT Bearer token.

## Technology

- Java 17
- Spring Boot 3.4.5
- Spring Security (stateless JWT validation)
- PostgreSQL 16
- Spring Data JPA + Flyway
- Swagger UI / OpenAPI
- JUnit 5

## 1. Prerequisites

Install Java 17+, Maven 3.8+, and Docker Desktop (or a local PostgreSQL server).

## 2. Start PostgreSQL

From the project root:

```bash
docker compose up -d
```

This creates database `tourist_db` with username `postgres` and password `postgres`. Flyway creates the schema and inserts two sample tourist records on the first application startup.

If you use your own PostgreSQL instance, set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.

## 3. Run the application

```bash
mvn spring-boot:run
```

Or build a runnable JAR:

```bash
mvn clean package
java -jar target/tourist-record-management-1.0.0.jar
```

Base URL: `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 4. Get a JWT token

The project includes a **demo-only** token endpoint so that the complete flow can be tested immediately.

`POST /api/auth/token`

Request:

```json
{
  "username": "hoteladmin",
  "password": "Hotel@123"
}
```

Example response:

```json
{
  "tokenType": "Bearer",
  "accessToken": "<signed-jwt>",
  "expiresInSeconds": 3600
}
```

In Swagger UI, execute this endpoint and copy `accessToken` from the response. Click **Authorize**, paste the token (without adding the word `Bearer` because Swagger's HTTP Bearer scheme adds it), and click **Authorize**.

## 5. Test tourist endpoints

### Get tourist

`GET /api/tourists/1`

Requires a valid Bearer token. Sample response:

```json
{
  "id": 1,
  "fullName": "Aarav Sharma",
  "email": "aarav@example.com",
  "contactNumber": "+91 9876543210",
  "visitedDestinations": ["Charminar", "Golconda Fort"],
  "createdAt": "2026-01-01T00:00:00Z",
  "updatedAt": "2026-01-01T00:00:00Z"
}
```

### Update tourist details

`PUT /api/tourists/1`

Requires a valid Bearer token. Send only fields you want to update:

```json
{
  "contactNumber": "+91 9988776655",
  "visitedDestinations": [
    "Charminar",
    "Golconda Fort",
    "Ramoji Film City"
  ]
}
```

You may also update `fullName` and `email`. A field omitted or set to `null` is left unchanged. An empty `visitedDestinations` array clears the list.

Success returns HTTP `200 OK` with the updated tourist record.

## 6. Token validation test cases

| Test | Expected result |
|---|---|
| Valid token + existing tourist ID | 200 OK |
| No Authorization header | 401 Unauthorized |
| Invalid/malformed token | 401 Unauthorized |
| Expired token | 401 Unauthorized |
| Valid token + unknown tourist ID | 404 Not Found |
| Invalid email or contact number | 400 Bad Request |
| Email already used by another tourist | 409 Conflict |

For the invalid-token test in Swagger, click **Authorize**, enter an invalid value such as `not-a-real-token`, then execute `GET /api/tourists/1`. Clear authorization and retry to test the missing-token case.

## 7. cURL quick test

Get a token:

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/token \
  -H "Content-Type: application/json" \
  -d '{"username":"hoteladmin","password":"Hotel@123"}' | python -c "import sys,json; print(json.load(sys.stdin)['accessToken'])")
```

Update a tourist:

```bash
curl -i -X PUT http://localhost:8080/api/tourists/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"contactNumber":"+91 9988776655","visitedDestinations":["Charminar","Ramoji Film City"]}'
```

## 8. Configuration and production notes

Override configuration through environment variables:

- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `JWT_SECRET` (must be a strong random secret of at least 32 bytes for HS256)
- `JWT_EXPIRATION_SECONDS`
- `DEMO_USERNAME`, `DEMO_PASSWORD`

**Important security note:** The included `/api/auth/token` endpoint uses configurable demo credentials to make local Swagger testing self-contained. It is not a production hotel login system. Before deployment, replace it with your actual staff authentication / identity provider, store passwords using a secure password hashing algorithm, keep secrets in a secret manager, enforce HTTPS, and add authorization rules so hotel staff can update only records they are permitted to access. Do not use the sample credentials or development JWT key in production.

## 9. Run tests

```bash
mvn test
```

The tests cover JWT generation, valid token parsing, and malformed-token rejection. The Swagger steps above are the manual API integration test flow.
