# Acceptance Observation — Week 5

| Item | Detail |
|---|---|
| Version | v0.1 |
| Date | 2026-11-07 |
| Observer |R8|
| Environment | GitHub Codespaces, forwarded URL for port 80, Docker Compose --profile full |
| Build / CI | tested commit 30f776b; local run 2026-11-07 |

## Observations

### ACC-W05-001

| Field | Detail |
|---|---|
| Date | 2026-11-07 |
| Observer | R8 (self-observed) |
| Parcel type | Standard parcel (small) |
| Scenario | Register → shelve → verify → confirm |
| Steps observed | Registration UI not implemented in this build (`/parcels` and `/parcels/new` are placeholders). A parcel `ACC-UI-20261107-1` was created via the API, and the operator completed verify and confirm through `/pickup`. |
| Expected | 201 / 200 / 200; parcel collected; code used |
| Actual | Verify accepted code `72182490` and the pickup page allowed confirm; confirm succeeded. DB after: `parcels.status=collected`, `pickup_codes.status=used` (`used_at=2026-11-06 16:49:40`), 1 handover row with `result=collected`. |
| Completion time | not measured (timing not started before submission) |
| Blockers | No UI path to register a parcel; `/parcels/new` is a placeholder. Parcel created via API. Pickup verify + confirm worked in the UI. |
| Verdict | PASS |
| Evidence | `evidence/acceptance-1-db.txt` |

### ACC-W05-002

| Field | Detail |
|---|---|
| Date | 2026-11-07 |
| Observer | R8 (self-observed) |
| Parcel type | N/A |
| Scenario | Duplicate waybill rejection |
| Steps observed | Could not be observed through the UI: the registration page (`/parcels/new`) is a placeholder in this build. |
| Expected | Duplicate rejected with clear error; no second row |
| Actual | Not observable in the UI. Covered by system test ST-SYS-002 at the API level (HTTP 409, `DUPLICATE_TRACKING_NO`, 1 row). |
| Completion time | N/A |
| Blockers | Registration UI not implemented; duplicate rejection has no UI surface. |
| Verdict | NOT OBSERVED |
| Evidence | `system-test-report-W05.md` ST-SYS-002 |

### ACC-W05-003

| Field | Detail |
|---|---|
| Date | 2026-11-07 |
| Observer | R8 (self-observed) |
| Parcel type | Standard parcel (small) — same parcel as ACC-W05-001 |
| Scenario | Used pickup code rejection |
| Steps observed | Entered the used code `72182490` on `/pickup`; the page did not allow a second handover; repeat confirm returned the completed state. |
| Expected | Verify of a used code is rejected with a clear error; repeating confirm returns the already-recorded handover without creating a second collection. No double collection. |
| Actual | No second handover was created. DB after: `handovers count=1`, `pickup_codes.status=used`, `used_at` unchanged at `2026-11-06 16:49:40`. |
| Completion time | not measured |
| Blockers | None |
| Verdict | PASS |
| Evidence | `evidence/acceptance-3-db.txt` |

## Summary

| Observation | Date | Parcel type | Verdict |
|---|---|---|---|
| ACC-W05-001 | 2026-11-07 | Standard parcel (small) | PASS |
| ACC-W05-002 | 2026-11-07 | N/A | NOT OBSERVED |
| ACC-W05-003 | 2026-11-07 | Standard parcel (small) | PASS |

## Blockers and follow-ups
- Landing page (/dashboard), /parcels and /parcels/new are placeholders in this build. A staff user has no in-app navigation or UI to register a parcel; /pickup and /locations are the only working staff screens. Registration was exercised via the API. This is a Sprint 2 scope gap, recorded for the team.
- ACC-W05-002 (duplicate waybill) could not be observed in the UI for the same reason; it is covered by system test ST-SYS-002.
- Completion times were not measured for ACC-W05-001 and ACC-W05-003 because timing was not started before submission. Recorded honestly rather than estimated.
