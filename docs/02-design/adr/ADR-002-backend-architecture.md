# ADR-002: Backend Architecture

- Status: Accepted
- Accepted: 2026-10-12
- Date: 2026-10-10
- Owners: R6 Backend Lead, R7 Backend Developer

## Context

The backend must expose a stable REST API to the Vue frontend and must keep parcel and location state changes reliable under concurrent use.

## Decision

Use a layered architecture inside a single deployable Spring Boot service.

### Deployment model

One shared PostgreSQL database serves one or more collection points. There is no database
per point and no tenant column pair: a record belongs to a point through
`collectionPointId`, and a query is filtered by it rather than by connection routing.

The decision is recorded in CR-021 and the entity is in `ERD.md`. It follows from
`FR-46`, from `survey-results.md` O1 (six of seven owners manage more than one point) and
from administrator A5, who asked for one system rather than one installation per point.

Two consequences worth stating, because both shape the Sprint 2 code:

- Every repository query that reads a scoped record must filter by `collectionPointId`.
  A query that omits the filter returns another point's rows, so the filter belongs in the
  repository method rather than in the controller, where it can be forgotten once.
- An owner's access spans several points and an administrator's spans all of them, so
  authorisation is checked against a set of permitted points rather than against a single
  identifier on the user row.

### Layers

1. Controller: HTTP handling, request validation, response mapping.
2. Service: business rules and transactions.
3. Repository: Spring Data JPA data access.
4. Entity: JPA model.

### Package structure

```text
com.group23
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

- Success responses return the payload as defined in the contract, unwrapped; only failures are wrapped as `{ error: { code, message, requestId } }`.
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
