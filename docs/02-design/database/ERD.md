# ERD - Community Parcel Collection Point System

| Item | Detail |
|---|---|
| Status | Complete for Sprint 1 |
| Date | 2026-10-19 |
| Owners | R6 Backend Lead, R7 Backend Developer |
| Version | v1.4 |
| Database | PostgreSQL 16 |
| ORM | Spring Data JPA |

## Entities

### CollectionPoint

- id: UUID
- code: string, unique
- name: string
- address: string
- active: boolean
- createdAt: timestamp
- updatedAt: timestamp

### User

- id: UUID
- username: string, unique
- passwordHash: string
- fullName: string
- role: enum(resident, staff, owner, admin)
- phone: string
- active: boolean
- collectionPointId: FK -> CollectionPoint, nullable
- createdAt: timestamp
- updatedAt: timestamp

### Parcel

- id: UUID
- trackingNo: string, unique
- carrier: string
- recipientName: string
- recipientPhone: string
- size: enum(small, medium, large)
- status: enum(registered, shelved, collected)
- locationId: FK -> Location
- registeredById: FK -> User
- pickupCodeId: FK -> PickupCode
- expectedVersion: long
- collectionPointId: FK -> CollectionPoint
- createdAt: timestamp
- updatedAt: timestamp

The three status values are the ones ruled by CR-006 and confirmed as decision
D3 in the week 1 review. `out_for_delivery`, `ready_for_pickup` and `returned`
described courier and logistics stages, and SRS section 1.5 puts dispatch and
delivery tracking outside the system boundary, so they are not parcel states.
An exception is not a parcel state either; see `ParcelException` below.

### Location

- id: UUID
- code: string, unique
- zone: string
- size: enum(small, medium, large)
- status: enum(available, reserved, occupied, disabled)
- parcelId: FK -> Parcel, nullable
- collectionPointId: FK -> CollectionPoint
- updatedAt: timestamp

### PickupCode

- id: UUID
- parcelId: FK -> Parcel
- code: string, unique
- status: enum(active, used, expired, revoked)
- expiresAt: timestamp
- usedAt: timestamp
- createdAt: timestamp

### Handover

- id: UUID
- parcelId: FK -> Parcel
- pickupCodeId: FK -> PickupCode
- operatorId: FK -> User
- result: enum(collected, returned, exception)
- auditId: FK -> AuditLog
- createdAt: timestamp

### ParcelException (entity `ExceptionRecord`)

The exception flow is separate from the parcel lifecycle, as ruled by CR-006 and
recorded as decision D5 in the week 1 review: `ParcelException` is the class and
`ExceptionRecord` is the persisted entity. A parcel keeps its
`registered / shelved / collected` status while an open exception ticket
describes the problem, so an exception never adds a value to `Parcel.status`.

- id: UUID
- parcelId: FK -> Parcel
- type: enum(overdue, damaged, wrong_recipient, refused, lost)
- status: enum(open, processing, resolved)
- note: string
- assigneeId: FK -> User
- resolvedAt: timestamp
- createdAt: timestamp

### AuditLog

- id: UUID
- actorId: FK -> User
- action: string
- targetType: string
- targetId: UUID
- before: jsonb
- after: jsonb
- collectionPointId: FK -> CollectionPoint
- createdAt: timestamp

### Setting

- id: UUID
- key: string, unique within a collection point: `UNIQUE (collectionPointId, key)`, so each
  collection point holds its own value for the same key. There is no global default; see `CR-026`
- value: string
- description: string
- updatedById: FK -> User
- collectionPointId: FK -> CollectionPoint
- updatedAt: timestamp

## Relationships

- CollectionPoint 1 --- N Parcel
- CollectionPoint 1 --- N Location
- CollectionPoint 1 --- N AuditLog
- CollectionPoint 1 --- N Setting
- CollectionPoint 1 --- 0..N User (staff assignment only; see Collection point scope)
- User 1 --- N Parcel (registeredBy)
- Location 1 --- 0..1 Parcel (occupied location)
- Parcel 1 --- 0..1 PickupCode
- PickupCode 1 --- 0..1 Handover
- Parcel 1 --- 0..N ParcelException
- User 1 --- 0..N Handover (operator)
- User 1 --- 0..N AuditLog

## Collection point scope

`FR-46` requires every collection-point-scoped record to carry a collection point
identifier. The decision recorded in CR-021 is that Sprint 2 assumes **more than one
collection point**, because `survey-results.md` O1 records that six of seven owners
manage more than one point, and because FR-26, FR-27, FR-28, FR-30, FR-34 and FR-47
already require behaviour that spans more than one point.

The five entities that are scoped directly carry `collectionPointId`. `PickupCode`,
`Handover` and `ParcelException` reach their collection point through `parcelId`, so they
do not carry a second copy of it, which keeps the identifier in one place per record.

**Two parts of this are not designed yet and are named rather than guessed:**

- The Flyway migration that adds `CollectionPoint` and the five foreign keys is part of
  `T-41-01` (Registration, login, JWT, four-role RBAC), which owns the first migration.
  The partial unique index on `Location.parcelId` from CR-016 must land in the same
  migration.
- An owner manages several points and an administrator reads across all of them, so
  neither can be expressed as a single `User.collectionPointId`. A join table between
  `User` and `CollectionPoint` is needed for FR-27, FR-28, FR-30, FR-32, FR-33 and
  FR-34. `User.collectionPointId` is therefore nullable and holds the staff member's
  assigned point only, and the join table is recorded as Sprint 2 design work against
  `T-41-01`.

## Concurrency Notes

- `Parcel.status` changes are guarded by `expectedVersion` optimistic locking. A
  client sends the version it read; if the row has moved on, the update is
  rejected instead of silently overwriting a newer state.
- `Parcel.trackingNo` is unique, so the same waybill cannot be registered twice.
- `PickupCode.code` has a unique constraint.
- `Location.parcelId` has a unique partial constraint, so one location cannot
  hold two parcels at once. This is the defence for RSK-08.
- `Handover` is idempotent by `pickupCodeId`. The confirm step runs as one
  transaction that marks the pickup code used and writes the handover row. A
  repeated confirm finds the code already used and returns the recorded result
  instead of writing a second handover, and two staff members confirming the
  same code at the same time cannot both succeed because the update on the code
  row holds the row for the length of the transaction.

## Status Value Notes

- `Parcel.status` uses the three lifecycle values from CR-006:
  `registered`, `shelved`, `collected`.
- `Location.status` (`available`, `reserved`, `occupied`, `disabled`) and
  `PickupCode.status` (`active`, `used`, `expired`, `revoked`) are deliberately
  kept as they are. They describe the state of records inside the system rather
  than courier stages, so the CR-006 reduction does not apply to them. They
  already agree with the state machines and with the OpenAPI enums.
- `Handover.result` (`collected`, `returned`, `exception`) is the outcome of one
  handover, not a parcel status.

## State Machine Sources

The lifecycle of each status value is modelled by these PlantUML sources:

- `Parcel.status`: `docs/02-design/uml/state-parcel-lifecycle.puml`
- `Location.status`: `docs/02-design/uml/state-storage-location.puml`
- `PickupCode.status`: `docs/02-design/uml/state-pickup-code.puml`
- `ParcelException.status`: `docs/02-design/uml/state-exception-ticket.puml`

`ParcelException` is a separate record, so its ticket states do not extend
`Parcel.status`.

## Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v1.0 | Week 1 | Initial entity model for the eight entities | R6 |
| v1.1 | Week 2 | `Parcel.status` reduced to the three values ruled by `CR-006`, and the exception types aligned with the SRS as `CR-010` requires | R6 |
| v1.2 | Week 3 | `CollectionPoint` added, with `collectionPointId` on the five entities that are scoped directly and a section on what is still open, per `CR-021` | R1 |
| v1.3 | Week 3 | Status changed from draft to complete for Sprint 1. The nine entities, the ten relationships and the collection point scope are the ones Sprint 2 builds from | R1 |
| v1.4 | Week 3 | `Setting.key` corrected from `unique` to unique within a collection point (`CR-026`). The schema has always used `UNIQUE (collection_point_id, key)`, and this document's own relationship line already says `CollectionPoint 1 --- N Setting`, so the correction makes the entity match both | R1 |
