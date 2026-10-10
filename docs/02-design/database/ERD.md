# ERD - Community Parcel Collection Point System

- Status: Draft
- Date: 2026-10-10
- Owners: R6 Backend Lead, R7 Backend Developer
- Database: PostgreSQL 16
- ORM: Spring Data JPA

## Entities

### User

- id: UUID
- username: string, unique
- passwordHash: string
- fullName: string
- role: enum(resident, courier, staff, admin)
- phone: string
- active: boolean
- createdAt: timestamp
- updatedAt: timestamp

### Parcel

- id: UUID
- trackingNo: string, unique
- carrier: string
- recipientName: string
- recipientPhone: string
- size: enum(small, medium, large)
- status: enum(registered, in_storage, ready_for_pickup, out_for_delivery, picked_up, returned, exception)
- locationId: FK -> Location
- registeredById: FK -> User
- pickupCodeId: FK -> PickupCode
- expectedVersion: long
- createdAt: timestamp
- updatedAt: timestamp

### Location

- id: UUID
- code: string, unique
- zone: string
- size: enum(small, medium, large)
- status: enum(available, reserved, occupied, disabled)
- parcelId: FK -> Parcel, nullable
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

### Exception

- id: UUID
- parcelId: FK -> Parcel
- type: enum(overdue, damaged, wrong_recipient, other)
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
- createdAt: timestamp

### Setting

- id: UUID
- key: string, unique
- value: string
- description: string
- updatedById: FK -> User
- updatedAt: timestamp

## Relationships

- User 1 --- N Parcel (registeredBy)
- Location 1 --- 0..1 Parcel (occupied location)
- Parcel 1 --- 0..1 PickupCode
- PickupCode 1 --- 0..1 Handover
- Parcel 1 --- 0..N Exception
- User 1 --- 0..N Handover (operator)
- User 1 --- 0..N AuditLog

## Concurrency Notes

- Parcel.status changes use `expectedVersion` for optimistic locking.
- PickupCode.code has a unique constraint.
- Location.parcelId has a unique partial constraint so one location cannot hold two parcels at once.
- Handover is idempotent by pickupCodeId.
