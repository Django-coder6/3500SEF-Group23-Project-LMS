# Defect Log

This document records all defects found during testing.

| Item | Detail |
|---|---|
| Version | v1.5 |
| Status | Active |
| Owner | R8 |

The format and severity rules were defined in Sprint 1, before any code existed.
The log stayed empty through Sprint 2 because the tests were passing; the Week 4
reachability review then found the first real P0 defects by tracing the running
path rather than by adding another happy-path test.

## Why create the template before the first bug?

Creating `defect-log.md` in advance has three benefits:

1. **No need to invent a format later**: testers can copy the table and fill in the data.
2. **Unified severity rules**: P0 is fixed on the same day, and P1 is fixed within the sprint.
3. **Enforced closure**: closing a defect must reference a test that changed from failed to passed.

The first entries below show the rule in use.

## Defect Severity Rules 

| Severity | Meaning | Response |
|---|---|---|
| **P0** | The system cannot be demonstrated, or data is corrupted | Fixed before any other work, **same day** |
| **P1** | A must-have requirement does not work | Fixed **within the sprint** |
| **P2** | A should-have requirement does not work, or the workaround is awkward | Scheduled into the **next sprint** |
| **P3** | Cosmetic or a minor inconvenience | Recorded, fixed if time allows |

## Defect List 

| Defect ID | Severity | Week Found | Found By | Related Requirement | Reproduction Steps | Status | Closing Evidence |
|---|---|---|---|---|---|---|---|
| *(Example)* DEF-XXX-000 | *(P0-P3)* | *(Wxx)* | *(Name)* | *(FR/NFR)* | *(Steps)* | *(Open/Fixed/Closed)* | *(TC-XXX-001 or PR #xx)* |
| DEF-PARC-001 | P0 | W04 | R1 reachability review | FR-12, FR-14, FR-21 | Register a parcel through the single-parcel form, choosing a location. The API stores `status=registered` and `locationId`, but never calls the location endpoint or marks the location occupied. A later `PATCH /parcels/{id}/location` rejects the parcel because it already names a location, and pickup verification rejects it because it is not `shelved`. | Closed | `ParcelRegistrationIntegrationTest.registersAndShelvesWithPickupCodeInOneRequest`; `ParcelShelvingIntegrationTest.placesARegisteredParcelOnAnAvailableLocation`; develop CI `43009104923` (commit `3359c39`; backend job success). Supersedes `41658317363`, which is stale because `b6011b5..3359c39` includes backend code and test changes. |
| DEF-PARC-002 | P0 | W04 | R1 reachability review | FR-8, FR-21, NFR-7 | Search production code for `new PickupCode` / `INSERT INTO pickup_codes`: only tests and seed data create codes. A newly shelved parcel therefore has no code to give the resident, and the seed codes expired on 2026-11-01/02. | Closed | `PickupCodeIssuanceServiceTest`; `ParcelRegistrationIntegrationTest.registersAndShelvesWithPickupCodeInOneRequest`; `V4__refresh_demo_pickup_codes.sql`; develop CI `43009104923` (commit `3359c39`; backend job success). Supersedes `41658317363`. |
| DEF-SEC-001 | P0 | W04 | R1 reachability review | NFR-3 | Send `PATCH /api/parcels/{id}/location` as staff. `SecurityConfig` only matched `/api/parcels/*/status`, so the new endpoint fell through to `denyAll` and returned 403 even though the controller method existed. | Closed | `AuthSecurityIntegrationTest` endpoint matrix includes the location path; develop CI `43009104923` (commit `3359c39`; backend job success). Supersedes `41658317363`. |
| DEF-OPS-001 | P0 | W04 | R1 reachability review | NFR-14 | Run the full Compose stack and open `http://localhost:80`. The frontend image has no Nginx `/api` proxy, so the browser's login request never reaches `backend:8080`; `docker-smoke` only checked the backend directly. | Closed | `frontend/nginx.conf`; `frontend/Dockerfile`; `docker-smoke` health/login through port 80 passed in CI `43009104923` (commit `3359c39`; automated closing evidence). Supersedes `41658317363`. R10 video evidence attached in [Evidence handoff #293](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/issues/293) is supplementary manual evidence: complete port-80 staff flow on 4 November, registration returned 201 for SF1793777829702, verification and confirmation returned 200, and SQL confirmed the collected parcel and used code. Closure relies on the CI `docker-smoke` output in `43009104923`, not the video alone. |
| DEF-DOC-001 | P1 | W04 | R1 document audit | Report quality | Report chapter 7 said 17 test classes / 74 tests and 85.9% coverage as current; chapter 8 said CR-013 added resident endpoints that `openapi.yaml` marks specified and not built. | Closed | Chapters 7 and 8 corrected on 2026-11-03; outline v0.7 records 90 backend methods; coverage report output post-merge: **88.79% line / 64.64% branch** (commit `3359c39`, CI run `43009104923`). **Evidence type: documentation correction, not an automated test.** This is a recorded exception to the strict test-only closing rule; closure approved by R8 as document evidence against the final report. |
| DEF-UI-001 | P2 | W04 | Local test run | NFR-13 | Run the full frontend suite from a cold start: `router-guard.spec.js` "lets staff open the pickup page" can exceed the 5 s Vitest timeout; the same test passes when the module cache is warm. | Closed | `vite.config.js` test timeout raised to 15 s; cold-cache run passed 68/68 on 2026-11-03 (local evidence, supplementary). Automated closing evidence: `frontend` job success in develop CI `43009104923` (commit `3359c39`). Supersedes `41658317363`. |

### Status meanings
- **Open** – Logged by QA; not fixed yet.
- **Fixed** – Developer says it is fixed; waiting for QA verification.
- **Closed** – QA verified the failed test case now passes.

**Closing Rule**: A defect is closed **only when a test case that failed now passes**. The closing evidence must reference the test case ID (for example, `TC-PARC-001`), or the test name plus CI/output where a TC ID is not yet assigned.  
**Recorded exception**: DEF-DOC-001 is a documentation defect; its closure is based on corrected report chapters and reviewer confirmation, not on an automated test. This exception is explicitly noted in the row and in the reconciliation appendix.

## Reconciliation (2026-11-11)

All six defects were reconciled against merged fixes and the current develop CI evidence.

- **Final evidence commit**: `3359c3942d739aa9372e31102152096448b5dd76`
- **Final CI run**: [`43009104923`](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/actions/runs/43009104923) — success
- **Jobs**: sanity, commitlint, frontend, backend, docker-smoke — all success
- **Superseded run**: `41658317363` (commit `b6011b5`). It is stale because `b6011b5..3359c39` includes backend code and test changes (`ParcelShelvingIntegrationTest` +193 lines, `ParcelShelvingService`, `ParcelRepository`, `LocationRepository`).
- **#321**: merged on 2026-11-11 as `47168151718c89798cc2b7dd1a4c61d1da8d6bcc`; current develop HEAD is `3359c39`.
- **Canonical coverage**: 88.79% line (808/910), 64.64% branch (170/263). `Coverage-Report3.md` (807/909 = 88.78%) is superseded and must not be cited as current.

| Defect ID | Reconciled status | Evidence type | Test / command | CI run | Verified commit | Result | Caveat |
|---|---|---|---|---|---|---|---|
| DEF-PARC-001 | Closed | CI automated test | `ParcelRegistrationIntegrationTest.registersAndShelvesWithPickupCodeInOneRequest`; `ParcelShelvingIntegrationTest.placesARegisteredParcelOnAnAvailableLocation` | `43009104923` | `3359c39` | backend job success | Uses new test version; old run superseded |
| DEF-PARC-002 | Closed | CI automated test | `PickupCodeIssuanceServiceTest`; `ParcelRegistrationIntegrationTest.registersAndShelvesWithPickupCodeInOneRequest` | `43009104923` | `3359c39` | backend job success | Old run superseded |
| DEF-SEC-001 | Closed | CI automated test | `AuthSecurityIntegrationTest` endpoint matrix | `43009104923` | `3359c39` | backend job success | Old run superseded |
| DEF-OPS-001 | Closed | CI automated test + supplementary manual | `docker-smoke` health/login through port 80 | `43009104923` | `3359c39` | docker-smoke success | R10 video #293 is supplementary, not closure basis |
| DEF-DOC-001 | Closed | Documentation correction | Chapters 7/8 corrected; coverage report **88.79% / 64.64%** | `43009104923` | `3359c39` | Document corrected | Not an automated test; recorded exception |
| DEF-UI-001 | Closed | CI automated test + supplementary local run | frontend suite 68/68; `vite.config.js` timeout 15s | `43009104923` | `3359c39` | frontend job success | 2026-11-03 cold-cache run is local evidence, supplementary |

### Reconciliation summary

| Metric | Value |
|---|---|
| Total defects | 6 |
| Closed with evidence | 6 |
| Open | 0 |
| Blockers | 0 |
| Closed without evidence | 0 |
| Awaiting re-test | 0 |
| Open P0 demo blockers | 0 |

## Revision history

| Version | Date | Change | Author |
|---|---|---|---|
| v1.0 | Week 1 | Empty template with severity and closure rules | R8 |
| v1.1 | Week 5 | Added the first six real defects from the reachability review and the cold-start test, with reproduction and test evidence | R1 |
| v1.2 | Week 5 | Closed DEF-PARC-001/002 and DEF-SEC-001 against develop CI `41658317363`; DEF-OPS-001 remains open only for R10's manual Compose flow | R1 |
| v1.3 | Week 5 | Recorded local browser and database verification for DEF-OPS-001. R8 owns closure. | R10 |
| v1.4 | Week 5 | Closed DEF-OPS-001 with `docker-smoke` CI `41658317363` as automated evidence and R10 video in #293 as supplementary manual evidence; confirmed DEF-DOC-001/UI-001 evidence; no Open P0 remains. | R8 |
| v1.5 | 2026-11-11 | Reconciled all six defects against merged fixes and current develop CI run `43009104923` (commit `3359c39`). All closed items have passing evidence; `41658317363` marked superseded because `b6011b5..3359c39` includes backend code/test changes. DEF-DOC-001 recorded as documentation evidence exception with canonical coverage 88.79% / 64.64%; DEF-UI-001 local run marked supplementary. No open P0; no closed item without evidence. | R8 |
