# Frontend API Dependencies

- Status: Draft aligned with PR #38 OpenAPI v0.1
- Task: T-30-04
- Owners: R4 Frontend Lead, R6 Backend Lead
- Consumers: R4 and R5 frontend implementation
- Base URL: `/api`

## Contract Rules

- The backend owns the final endpoint paths and response fields.
- The frontend must not invent fields before R6 updates the OpenAPI draft.
- Requests and responses use JSON.
- The shared response envelope from ADR-002 is `data`, `error` and `requestId`.
- Dates use ISO 8601 strings in UTC and are formatted in the frontend.
- Parcel sizes use `small`, `medium` and `large`.
- Parcel, location and exception status values are stable strings, not UI labels.
- Endpoint paths in the table omit the `/api` base URL.

## P0 Endpoint List

| Priority | Method and path | Used by | Purpose | Important fields | Status |
|---|---|---|---|---|---|
| P0 | `GET /health` | Development | Service health check | status | In OpenAPI draft |
| P0 | `POST /auth/login` | Login | Login | username, password, token, user.roles | In OpenAPI draft |
| P0 | `GET /auth/me` | App shell | Restore current session | user.id, user.username, user.roles | Proposed, needs R6 confirmation |
| P0 | `GET /parcels` | Parcel list | List and filter parcels | page, pageSize, status, keyword, items, total | In OpenAPI draft |
| P0 | `POST /parcels` | Parcel create | Register an incoming parcel | trackingNo, carrier, recipientName, recipientPhone, size, locationId | In OpenAPI draft |
| P0 | `GET /parcels/{id}` | Parcel detail | Retrieve one parcel | id, trackingNo, status, size, locationId, expectedVersion | Proposed, needs R6 confirmation |
| P0 | `PATCH /parcels/{id}/status` | Parcel detail | Controlled status update | status, reason, expectedVersion | In OpenAPI draft |
| P0 | `GET /locations` | Dashboard, location board | List storage locations | id, code, zone, size, status, parcelId | In OpenAPI draft |
| P0 | `POST /locations/recommend` | Parcel create | Recommend an available location | size, zone, recommendedLocation, reason | In OpenAPI draft |
| P0 | `POST /pickup/verify` | Pickup workflow | Validate a pickup code | pickupCode, parcelSummary, location | In OpenAPI draft |
| P0 | `POST /pickup/confirm` | Pickup workflow | Confirm handover | pickupCode, operatorId, result | In OpenAPI draft |

## P1 Endpoint List

| Priority | Method and path | Used by | Purpose | Status |
|---|---|---|---|---|
| P1 | `GET /exceptions` | Exception list | List abnormal parcels | Proposed, needs SRS requirement IDs |
| P1 | `POST /exceptions/{id}/resolve` | Exception detail | Resolve an exception | Proposed, needs SRS requirement IDs |
| P1 | `GET /reports/summary` | Dashboard and reports | Retrieve summary metrics | Proposed, needs SRS requirement IDs |
| P1 | `GET /users` | User management | List staff and resident accounts | Proposed, needs SRS requirement IDs |
| P1 | `GET /audit-logs` | Audit page | List audit records | Proposed, needs SRS requirement IDs |
| P1 | `GET /settings` | Settings page | Load system configuration | Proposed, needs SRS requirement IDs |
| P1 | `PUT /settings` | Settings page | Update system configuration | Proposed, needs SRS requirement IDs |

## Status Values Needed by the Frontend

### Parcel

`registered`, `in_storage`, `ready_for_pickup`, `out_for_delivery`, `picked_up`, `returned`, `exception`.

### Location

`available`, `reserved`, `occupied`, `disabled`.

### Exception

`open`, `processing`, `resolved`.

The backend provides the canonical values. The frontend maps them to display labels and colours.

## Shared API Wrapper

Use one Axios instance:

```text
frontend/src/api/http.js
```

The wrapper is responsible for:

- Base URL `/api`.
- JSON request and response handling.
- Authentication header or token injection.
- Response-envelope normalisation.
- Error normalisation to `code`, `message` and `requestId`.
- Redirecting to `/login` on HTTP 401.
- No feature-specific business logic.

Feature-specific calls go in:

```text
frontend/src/api/auth.js
frontend/src/api/parcels.js
frontend/src/api/locations.js
frontend/src/api/pickup.js
frontend/src/api/exceptions.js
frontend/src/api/reports.js
```

## Shared State

Pinia stores are used for data shared across routes:

```text
frontend/src/stores/auth.js
frontend/src/stores/parcels.js
frontend/src/stores/locations.js
```

Pages should not duplicate authentication or parcel-list state.

## Mock Strategy

Until an endpoint is approved, each feature uses a local mock module with the same function name and return shape as the real API. Switching from mock to real API should change the API boundary, not page code.

## Review Checklist Before Implementation

- R6 confirms the final paths, schema fields and response envelope.
- R5 confirms the fields required by each page.
- SRS FR and NFR IDs are linked to the relevant endpoints.
- Status values and error codes are stable.
- Pickup confirmation defines idempotency behaviour.
- Location recommendation defines `recommendedLocation = null` and `reason`.
