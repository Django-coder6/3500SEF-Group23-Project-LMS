# ADR-002: Backend Architecture

- Status: Proposed
- Date: 2026-10-10
- Owners: R6 Backend Lead, R7 Backend Developer

## Context

The backend must expose a stable REST API to the Vue frontend and must keep parcel and location state changes reliable under concurrent use.

## Decision

Use a layered architecture inside a single deployable Spring Boot service.

### Layers

1. Controller: HTTP handling, request validation, response mapping.
2. Service: business rules and transactions.
3. Repository: Spring Data JPA data access.
4. Entity: JPA model.

### Package structure

```text
com.linyi
├── config
├── common
├── auth
├── parcel
├── location
├── pickup
├── exception
├── report
├── user
├── audit
└── setting
```

### Cross-cutting rules

- Every response uses a shared envelope: `data`, `error`, `requestId`.
- All unexpected exceptions are handled by `@RestControllerAdvice`.
- Date and time values use ISO 8601 strings in UTC.
- State changes go through dedicated service methods, never directly in controllers.
- Sensitive changes are recorded in the AuditLog.

## Alternatives Considered

### Controller-only services

Rejected. Business logic would be scattered and hard to test.

### Microservices

Rejected. The system is small and the team has limited time.

## Consequences

- Code is easy to review and test.
- Service methods can use `@Transactional` at one place.
- A single service keeps deployment simple for the course deadline.
