# System Test Report — Week 5

| Item | Detail |
|---|---|
| Version | v0.1 |
| Date | 2026-11-05 |
| Environment | Docker Compose, http://localhost:80 |
| Tester | R8 / <name> |
| Build / CI | commit <sha>; CI run <id> |

## Scope
End-to-end staff flow through the Nginx proxy on port 80.

## Test cases

### ST-SYS-001 — register → shelve → verify → confirm

| Field | Detail |
|---|---|
| Requirement | FR-12, FR-14, FR-21 |
| Preconditions | Staff logged in; at least one available location |
| Steps | 1. Open http://localhost:80 and log in as staff.<br>2. Register a parcel through the single-parcel form, choosing a location.<br>3. Confirm the parcel is shelved and a pickup code is issued.<br>4. Verify the pickup code.<br>5. Confirm pickup. |
| Expected | Registration returns 201; parcel becomes `shelved`; pickup code is issued; verification returns 200; confirmation returns 200; parcel becomes `collected` and code becomes used. |
| Actual | <fill in: status codes, DB/SQL result, screenshot/video timestamp> |
| Verdict | PASS / FAIL |
| Evidence | <CI run, video timestamp, SQL output, screenshot link> |

### ST-SYS-002 — duplicate waybill

| Field | Detail |
|---|---|
| Requirement | FR-12 (duplicate handling) |
| Preconditions | A parcel with waybill `<waybill>` already exists |
| Steps | 1. Log in as staff.<br>2. Register another parcel using the same waybill.<br>3. Observe the response and database state. |
| Expected | The system rejects the duplicate with a clear error (e.g. 409/400); no second parcel row is created. |
| Actual | <fill in: HTTP status, error message, SQL count> |
| Verdict | PASS / FAIL |
| Evidence | <request/response example, SQL output> |

### ST-SYS-003 — used code

| Field | Detail |
|---|---|
| Requirement | FR-21, NFR-7 |
| Preconditions | A parcel has been collected and its pickup code is already used |
| Steps | 1. Log in as staff.<br>2. Try to verify the already-used pickup code.<br>3. Try to confirm pickup again. |
| Expected | Verification/confirmation is rejected; the parcel is not collected twice; the code remains used. |
| Actual | <fill in: HTTP status, error message, SQL state> |
| Verdict | PASS / FAIL |
| Evidence | <request/response example, SQL output> |

## Summary

| Test case | Verdict |
|---|---|
| ST-SYS-001 | PASS/FAIL |
| ST-SYS-002 | PASS/FAIL |
| ST-SYS-003 | PASS/FAIL |

## Notes / blockers
<fill in any blocker, e.g. environment issue, missing seed data>
