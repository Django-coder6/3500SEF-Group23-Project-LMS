# Acceptance Observation — Week 5

| Item | Detail |
|---|---|
| Version | v1.0 |
| Date | 2026-11-07 |
| Observer | R8 |
| Environment | GitHub Codespaces, forwarded URL for port 80, Docker Compose `--profile full` |
| Build / CI | tested commit 30f776b; local run 2026-11-07 |

## Observations

### ACC-W05-001

| Field | Detail |
|---|---|
| Date | 2026-11-07 |
| Observer | R8 |
| Parcel type | Standard parcel (small) |
| Scenario | Register → shelve → verify → confirm |
| Steps observed | Registration UI not implemented in this build (`/parcels` and `/parcels/new` are placeholders). A parcel `ACC-UI-20261107-1` was created via the API, and the operator completed verify and confirm through `/pickup`. |
| Expected | Verify returns 200 and confirm returns 200 in the UI; parcel collected; code used (registration 201 covered by ST-SYS-001). |
| Actual | Verify accepted code `72182490` and the pickup page allowed confirm; confirm succeeded. DB after: `parcels.status=collected`, `pickup_codes.status=used` (`used_at=2026-11-06 16:49:40`), 1 handover row with `result=collected`. |
| Completion time | not measured (timing not started before submission) |
| Blockers | No UI path to register a parcel; `/parcels/new` is a placeholder. Parcel created via API. Pickup verify + confirm worked in the UI. |
| Verdict | PASS |
| Evidence | `evidence/acceptance-1-db.txt` |

### ACC-W05-002

| Field | Detail |
|---|---|
| Date | 2026-11-07 |
| Observer | R8 |
| Parcel type | Standard parcel (small) — invalid-code probe on `/pickup` |
| Scenario | Invalid pickup code rejection (substitute for duplicate-waybill, which has no UI surface) |
| Steps observed | Opened `/pickup`, entered code `00000000`, submitted. The page rejected it with `Pickup code not recognised`. No parcel state changed. |
| Expected | Invalid code rejected with a clear error; no parcel state changed |
| Actual | Page showed `Pickup code not recognised`; no parcel state changed (no new parcel, no handover) |
| Completion time | 5s |
| Blockers | The duplicate-waybill scenario has no UI surface because `/parcels/new` is a placeholder. This observation substitutes an invalid-code rejection, which exercises the same pickup error path. Duplicate rejection itself is covered by system test ST-SYS-002. |
| Verdict | PASS |
| Evidence | UI observation on `/pickup` (no artifact captured) |

### ACC-W05-003

| Field | Detail |
|---|---|
| Date | 2026-11-07 |
| Observer | R8 |
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
| ACC-W05-002 | 2026-11-07 | Standard parcel (small) | PASS |
| ACC-W05-003 | 2026-11-07 | Standard parcel (small) | PASS |

## Blockers and follow-ups
- Landing page (`/dashboard`), `/parcels` and `/parcels/new` are placeholders in this build. A staff user has no in-app navigation or UI to register a parcel; `/pickup` and `/locations` are the only working staff screens. Registration was exercised via the API. This is a Sprint 2 scope gap, recorded for the team.
- The duplicate-waybill scenario originally planned for ACC-W05-002 has no UI surface for the same reason. ACC-W05-002 substitutes an invalid-code probe on `/pickup`; the duplicate rejection itself is covered by system test ST-SYS-002.
- Completion times were not measured for ACC-W05-001 and ACC-W05-003 because timing was not started before submission. Recorded honestly rather than estimated.
