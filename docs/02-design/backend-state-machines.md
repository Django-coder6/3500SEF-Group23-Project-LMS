# Backend State Machines

| Item | Detail |
|---|---|
| Status | Complete for Sprint 1 |
| Version | v1.0 |
| Date | 2026-10-10 |
| Owners | R6 Backend Lead |
| Related | CR-006, CR-007, docs/02-design/database/ERD.md |

## Parcel

States (CR-006):

```text
registered
shelved
collected
```

Allowed transitions:

```text
registered -> shelved
shelved -> collected
```

An exception is not a parcel state. It is recorded as a `ParcelException`
(`open -> processing -> resolved`) against the parcel, and the parcel keeps the
status it had. The three earlier values `ready_for_pickup`, `out_for_delivery`
and `returned` described courier and logistics stages, which SRS section 1.5
puts outside the system boundary. The state machine diagrams in
`docs/02-design/uml/` must use this same state list.

## Location

States:

```text
available
reserved
occupied
disabled
```

Allowed transitions:

```text
available -> reserved
reserved -> occupied
reserved -> available
occupied -> available
available -> disabled
disabled -> available
```

## PickupCode

States:

```text
active
used
expired
revoked
```

Allowed transitions:

```text
active -> used
active -> expired
active -> revoked
```

## Exception

States:

```text
open
processing
resolved
```

Allowed transitions:

```text
open -> processing
processing -> resolved
open -> resolved
```

## Rule

Illegal transitions are rejected by the service layer and produce a `422 Unprocessable Entity` error.



## Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v1.0 | Week 2 | First version recorded in this file. The four state machines the backend implements, with the enum values `CR-006` fixed. Any earlier edit is not listed: this file had no revision history until week 3, so records of those edits do not exist here. | R1 |
