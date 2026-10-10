# Backend State Machines

- Status: Draft
- Date: 2026-10-10
- Owners: R6 Backend Lead

## Parcel

States:

```text
registered
in_storage
ready_for_pickup
out_for_delivery
picked_up
returned
exception
```

Allowed transitions:

```text
registered -> in_storage
in_storage -> ready_for_pickup
ready_for_pickup -> picked_up
ready_for_pickup -> returned
in_storage -> exception
ready_for_pickup -> exception
exception -> in_storage
exception -> ready_for_pickup
exception -> returned
```

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
