# Frontend API Dependencies

- Status: Draft for review with R6
- Owners: R4 Frontend Lead, R6 Backend Lead
- Consumers: R4 and R5 frontend implementation

## Contract Rules

- The backend owns the final endpoint paths and response fields.
- The frontend must not invent fields after the OpenAPI draft is agreed.
- Requests and responses use JSON.
- The frontend needs a stable success response and a stable error shape.
- Dates use ISO 8601 strings and are formatted in the frontend.
- Parcel and location status values must be stable strings, not UI labels.

## Suggested Endpoint List

| Priority | Method and path | Used by | Purpose | Important fields | Backend owner |
|---|---|---|---|---|---|
| P0 | `POST /api/auth/login` | Login | Staff or admin login | username/email, password, user, roles, token | R6 |
| P0 | `GET /api/auth/me` | App shell | Restore current session | user, roles, siteId | R6 |
| P0 | `GET /api/parcels` | Parcel list | List and filter parcels | page, pageSize, status, keyword, items, total | R6/R7 |
| P0 | `POST /api/parcels` | Parcel create | Register incoming parcel | trackingNo, carrier, recipient, phone, size, locationId | R6/R7 |
| P0 | `GET /api/parcels/:id` | Parcel detail | Retrieve parcel and history | parcel, statusHistory, pickupCodeSummary | R6/R7 |
| P0 | `PATCH /api/parcels/:id/status` | Parcel detail | Controlled status update | status, reason, expectedVersion | R6/R7 |
| P0 | `GET /api/locations` | Dashboard, location board | List locations and occupancy | id, zone, size, status, parcelId | R6/R7 |
| P0 | `POST /api/locations/recommend` | Parcel create | Recommend an available location | size, cabinet/zone preference, recommendedLocation | R6 |
| P0 | `POST /api/pickup/verify` | Pickup workflow | Validate a pickup code | pickupCode, parcelSummary, location | R6/R7 |
| P0 | `POST /api/pickup/confirm` | Pickup workflow | Confirm handover | pickupCode, operatorId, result, auditId | R6/R7 |
| P1 | `GET /api/exceptions` | Exception list | List abnormal parcels | status, type, parcel, location, assignee | R6/R7 |
| P1 | `POST /api/exceptions/:id/resolve` | Exception detail | Resolve an exception | resolution, note, resolvedAt | R6/R7 |
| P1 | `GET /api/reports/summary` | Dashboard/reports | Retrieve summary metrics | dateRange, inbound, pickedUp, occupancy, exceptions | R6/R7 |
| P1 | `GET /api/users` | User management | List staff, residents and couriers | role, active, page, items, total | R6 |
| P1 | `GET /api/audit-logs` | Audit log page | List audit records | actor, action, target, time | R6 |
| P1 | `GET /api/settings` | Settings page | Load system rules | storageHours, reminderHours, prefixes | R6 |
| P1 | `PUT /api/settings` | Settings page | Update system rules | storageHours, reminderHours, prefixes | R6 |

## Status Values Needed by the Frontend

### Parcel

`registered`, `in_storage`, `ready_for_pickup`, `out_for_delivery`, `picked_up`, `returned`, `exception`.

### Location

`available`, `reserved`, `occupied`, `disabled`.

### Exception

`open`, `processing`, `resolved`.

The backend should provide the canonical values. The frontend maps them to display labels and colours.

## Shared API Wrapper

The frontend will use one module:

```text
frontend/src/api/http.js
```

It will be responsible for:

- Base URL selection.
- JSON serialisation.
- Authentication header.
- Common error messages.
- Redirect on 401.
- No business logic.

Feature-specific calls go in:

```text
frontend/src/api/auth.js
frontend/src/api/parcels.js
frontend/src/api/locations.js
frontend/src/api/pickup.js
frontend/src/api/reports.js
```

## Mock Strategy

Until an endpoint is ready, each feature uses a local mock module with the same function name and return shape as the real API. Switching from mock to real API should change the import at the API boundary, not the page code.

## Review Checklist Before Implementation

- R6 has confirmed endpoint names and response shapes.
- R5 has confirmed the fields needed by each page.
- Status values and error codes are stable.
- The pickup confirmation endpoint defines idempotency behaviour.
- The location recommendation endpoint defines what happens when no location is available.
