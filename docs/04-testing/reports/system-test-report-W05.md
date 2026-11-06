# System Test Report — Week 5

| Item | Detail |
|---|---|
| Version | v1.0 |
| Date | 2026-11-06 |
| Environment | GitHub Codespaces, Docker Compose (`--profile full`), http://localhost:80 |
| Tester | R8 / <name> |
| Build / CI | commit <sha>; CI run <id> |
| Contract | docs/03-api/openapi.yaml v0.7.0-draft |

## Scope
End-to-end staff flow through the Nginx proxy on port 80.

### Environment baseline (observed)
- `docker compose --profile full ps`: postgres, redis, backend, frontend all `healthy`.
- Backend built from `backend/` (Spring Boot), frontend nginx proxies `/api` to `backend:8080`.
- Demo data seeded by Flyway `V2__seed_data.sql`; signed in as `demo_staff_1`.
- Available location used for all cases: `30000000-0000-4000-8000-000000000003` (code `DEMO-A-01-3`).

## Test cases

### ST-SYS-001 — register → shelve → verify → confirm

| Field | Detail |
|---|---|
| Requirement | FR-12, FR-14, FR-21 |
| Preconditions | Staff logged in as `demo_staff_1`; at least one available location |
| Steps | 1. `POST /api/auth/login` as `demo_staff_1`.<br>2. `POST /api/parcels` with `trackingNo=TEST-SYS001-1793977073` and `locationId=30000000-0000-4000-8000-000000000003`.<br>3. `POST /api/pickup/verify` with the issued code `65584873`.<br>4. `POST /api/pickup/confirm` with the same code.<br>5. Check DB state. |
| Expected | Registration returns 201; parcel becomes `shelved`; pickup code is issued; verification returns 200; confirmation returns 200; parcel becomes `collected` and code becomes used. |
| Actual | **Register**: `POST /api/parcels` → **HTTP 201**; response `status="shelved"`, `pickupCode="65584873"`, `id="c6707091-121f-4c77-bc4e-cc369e8c6328"`, `expectedVersion=2`.<br>**Verify**: **HTTP 200**; `parcelSummary.status="shelved"`, `parcelSummary.pickupCode=null` (credential omitted per contract), `location.status="occupied"`, `location.parcelId` matches parcel id.<br>**Confirm**: **HTTP 200**; `Handover{id="0a52b5b0-c438-4b09-9586-ddd8d20c16cf", parcelId="c6707091-...", result="collected"}`.<br>**DB**: `parcels.status="collected"`, `parcels.location_id=NULL`; `pickup_codes.status="used"`, `used_at=2026-11-06 15:00:17.687032+00`; `handovers count=1`; `locations.status="available"`, `parcel_id=NULL`. |
| Verdict | **PASS** |
| Evidence | `evidence/ST-SYS-001-register.txt`, `ST-SYS-001-verify.txt`, `ST-SYS-001-confirm.txt`, `ST-SYS-001-db.txt` |

### ST-SYS-002 — duplicate waybill

| Field | Detail |
|---|---|
| Requirement | FR-12, FR-17 (duplicate handling) |
| Preconditions | Parcel `TEST-SYS001-1793977073` already exists |
| Steps | 1. Log in as staff.<br>2. `POST /api/parcels` again with the same `trackingNo`.<br>3. Observe response and DB count. |
| Expected | 409 with `error.code=DUPLICATE_TRACKING_NO`; no second parcel row. |
| Actual | **HTTP 409**; `error.code="DUPLICATE_TRACKING_NO"`, `message="This tracking number is already registered. Open the existing parcel or use a different number."`, `requestId="e901a5e8-..."`.<br>**DB**: `COUNT(parcels WHERE tracking_no=...) = 1`; `locations.status="available"`, `parcel_id=NULL` (second request did not occupy the shelf). |
| Verdict | **PASS** |
| Evidence | `evidence/ST-SYS-002-duplicate.txt`, `ST-SYS-002-db.txt` |

### ST-SYS-003 — used code

| Field | Detail |
|---|---|
| Requirement | FR-21, NFR-4 (idempotent handover), NFR-7 |
| Preconditions | Parcel `c6707091-...` collected; code `65584873` already used |
| Steps | 1. `POST /api/pickup/verify` with the used code.<br>2. `POST /api/pickup/confirm` again with the same code.<br>3. Check DB. |
| Expected | Verify returns 404 with `PICKUP_CODE_USED`. Confirm is idempotent per contract v0.4.0/NFR-4: returns 200 with the same recorded handover, no second handover row. Parcel stays `collected`; code stays `used`. |
| Actual | **Verify**: **HTTP 404**; `error.code="PICKUP_CODE_USED"`, `message="The pickup code has already been used. Do not hand over the parcel again."`.<br>**Repeat confirm**: **HTTP 200**; returned `Handover.id="0a52b5b0-c438-4b09-9586-ddd8d20c16cf"` — identical to the first confirm; no second handover written.<br>**DB**: `handovers count=1`; `parcels.status="collected"` unchanged; `pickup_codes.status="used"`, `used_at` unchanged. |
| Verdict | **PASS** |
| Evidence | `evidence/ST-SYS-003-verify-used.txt`, `ST-SYS-003-confirm-repeat.txt`, `ST-SYS-003-db.txt` |

## Summary

| Test case | Verdict |
|---|---|
| ST-SYS-001 | PASS |
| ST-SYS-002 | PASS |
| ST-SYS-003 | PASS |

## Notes / blockers
1. **Contract vs template wording for ST-SYS-003.** The original template said confirmation is "rejected" for a used code. OpenAPI v0.4.0 states `/pickup/confirm` is idempotent and returns the recorded handover with the same 200 response (NFR-4). The test was judged against the contract: verify is rejected with 404, confirm is idempotent with 200 and the same handover id. Recorded here so the template can be corrected next sprint.
2. **Shelf released on confirm (observed).** After confirm, `locations.status` returns to `available` and `parcels.location_id` is set to `NULL`. The contract does not state this explicitly; it is a reasonable behaviour (a collected parcel should not hold a shelf), recorded for the team to confirm it is intended.
3. **`recipientPhone` masked in create response.** The registration response masks the phone as `138****8888`, matching the ethics statement's masking rule; the test does not depend on the unmasked value.
4. **Compose profile.** `docker compose` commands must run with `--profile full` (or `COMPOSE_PROFILES=full` in the same shell), otherwise `frontend` fails with `undefined service "backend"` because the backend service lives in the `full` profile. This is a startup ergonomics issue, not a system defect.
5. **Codespaces port-forward.** `curl` in the Codespaces terminal uses `http://localhost:80`; browser/UI checks use the PORTS-panel forwarded URL.
