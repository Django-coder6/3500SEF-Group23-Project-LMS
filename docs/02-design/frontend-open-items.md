# Frontend open items from the Sprint 2 contract review

| Item | Detail |
|---|---|
| Status | Working list, not a contract |
| Version | v1.2 |
| Owner | R4 Frontend Lead, with R5 |
| Raised | 2026-10-13, from R6's Sprint 2 contract note and R4's read of the contract |
| Revised | 2026-10-15, against OpenAPI v0.3.0-draft after CR-013 was approved, and again the same day to close item 5. Revised again on 2026-10-23 to close item 4 and implement the pickup page behaviour |
| Source of truth | `docs/03-api/openapi.yaml`. The rows below are what the frontend needs from it |

These are the points that surfaced while aligning the frontend with the Sprint 2
contract (OpenAPI v0.3.0-draft). They are recorded here so the decision can be
revisited in one place instead of being buried in a chat message. Every row
names who decides and which file changes when it is decided.

| # | Item | Why it matters to the frontend | Decision / owner | File to change later |
|---|---|---|---|---|
| 1 | The contract had no resident self-service endpoint (`GET /my-parcels`, pickup code, notifications), while `frontend-page-map.md` lists `/my-parcels` and `/my-parcels/:id` as P0 | Those two pages could not be built against the contract | **Decided 2026-10-15.** R2 ruled the resident read path into Sprint 2 and it is recorded as CR-013 (Approved). The contract now carries `GET /my-parcels` (FR-2), `GET /my-parcels/{id}` (FR-3) and `GET /my-parcels/{id}/pickup-code` (FR-8). Notification delivery (FR-6, FR-7) is not part of this ruling and still has no endpoint | `docs/03-api/openapi.yaml` — done. `docs/02-design/frontend-page-map.md` when the pages are built |
| 2 | ~~`Parcel` responses carry only `id, trackingNo, status, size, locationId, expectedVersion`~~ | Staff could not match a parcel to a resident in a list or detail view, and there were no timestamps for the audit view | **Closed.** The `Parcel` schema now also carries `carrier`, `recipientName`, `recipientPhone` (masked in list responses, 138****8888), `createdAt` and `updatedAt` | `docs/03-api/openapi.yaml` — done |
| 3 | `/locations` returns a bare array while `/parcels` returns `PagedParcels` | Two list shapes for the frontend to handle | Accepted for Sprint 2; note it in the contract | `docs/03-api/openapi.yaml` |
| 4 | ~~`POST /parcels/batch` answers `created[]` plus `rejected[{ index, trackingNo, reason }]`~~ | The batch registration screen must show a per-row error and must not fail the whole batch | **Done 2026-10-23.** The activity diagram now shows the batch result and row-level errors separately from the single-parcel path | `docs/02-design/uml/activity-parcel-intake.puml` — done |
| 5 | ~~The intake sequence diagram does not show `POST /locations/recommend`, although the UML README indexes it~~ | The recommendation is only a suggestion that staff may ignore, so the diagram should show it or the index should drop it | **Done 2026-10-13.** The diagram shows the call and both of its answers, a free location and `recommendedLocation: null` with `reason: no_location_available`, so the diagram and the index now agree. Commit `7cf3929`, PR #95 | - |
| 6 | Parcel status (`registered/shelved/collected`), exception status (`open/processing/resolved`) and location status (`available/reserved/occupied/disabled`) are stable strings, not labels | `StatusTag` must map every value to one label and one colour, in one place | R4, when `StatusTag` is built | `docs/02-design/frontend-page-map.md` (component map) |
| 7 | `PATCH /parcels/{id}/status` requires `expectedVersion` and answers 409 when it is stale | The detail page must send the version and show a "someone else just changed this" prompt | R5, Sprint 2 | page work; `docs/02-design/frontend-page-map.md` |
| 8 | `POST /pickup/confirm` is idempotent and no longer accepts `operatorId` | The handover button must not double-submit | **Frontend behaviour done 2026-10-23.** The pickup page disables the action while a request is in flight and accepts the same successful result on repeat; real API integration remains `T-42-09` | `PickupVerifyView.vue` — done; integration pending |

Items 1 and 2 are decided, so nothing in this list blocks a Sprint 2 feature
page any more. Item 3 is a scope note that is accepted. Items 4 and 5 are done.
The activity diagram now distinguishes the single-parcel path from batch
validation, records the contract's `created[]` and
`rejected[{ index, trackingNo, reason }]` result, and states that one rejected
row does not fail the whole batch. Items 6 to 8 are page work the frontend does
when it builds the corresponding screen.



## Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v1.0 | Week 2 | First version recorded in this file. The open items from the Sprint 2 contract review. Any earlier edit is not listed: this file had no revision history until week 3, so records of those edits do not exist here. | R1 |
| v1.1 | Week 3 | Closed item 4 by adding the batch path and row-level `created[]` / `rejected[]` result to `activity-parcel-intake.puml` | R4 |
| v1.2 | Week 3 | Implemented the pickup page's idempotent frontend behaviour for item 8; real API integration remains with `T-42-09` | R4 |
