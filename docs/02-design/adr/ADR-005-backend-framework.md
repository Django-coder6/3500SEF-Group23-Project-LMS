# ADR-005: Backend Framework

- Status: Proposed
- Date: 2026-10-10
- Owners: R6 Backend Lead, R7 Backend Developer

## Context

The repository originally assumed Express + Sequelize for the backend. The backend workstream owns parcel registration, location management, pickup handover, billing, stocktake and reporting. Several of these flows need transactions, optimistic locking and clear validation.

## Decision

Use Spring Boot 3 as the backend framework with:

- Java 17
- Spring Web
- Spring Data JPA
- PostgreSQL 16
- Flyway for database migrations
- Spring Security + JWT for authentication
- Bean Validation for request validation
- springdoc-openapi for API documentation
- JUnit 5 + MockMvc for testing
- Maven as the build tool

## Alternatives Considered

### Express + Sequelize

Kept the original choice. Rejected because the backend members are more familiar with Java and because transaction, locking and validation features are more structured in Spring Boot.

### Spring Boot with MyBatis

Rejected. Spring Data JPA reduces boilerplate for the small number of entities and integrates well with Flyway and Bean Validation.

### NestJS

Rejected. It adds a framework learning curve and does not provide a clear advantage over Spring Boot for this team.

## Consequences

- README and CI must be updated from Node to Java/Maven.
- Backend members need a Java 17 toolchain.
- Frontend is unaffected because it only depends on the OpenAPI contract.
- Transactions and optimistic locking can be implemented consistently with `@Transactional` and `@Version`.

## Verification

- `mvn -q test` passes in CI.
- `GET /api/health` returns a healthy response.
- OpenAPI remains the source of truth for frontend field names.
