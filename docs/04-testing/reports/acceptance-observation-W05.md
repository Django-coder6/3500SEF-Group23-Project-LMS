# Acceptance Observation — Week 5

| Item | Detail |
|---|---|
| Version | v0.1 |
| Date | 2026-11-05 |
| Observer | |
| Environment | Docker Compose, http://localhost:80 |
| Build / CI | tested commit 30f776b (w05r81); report commit faa2afa; local run 2026-11-06 |

## Observations

### ACC-W05-001

| Field | Detail |
|---|---|
| Date | 2026-11-05 |
| Observer | <name> |
| Parcel type | Standard parcel |
| Scenario | Register → shelve → verify → confirm |
| Steps observed | <簡述觀察步驟> |
| Expected | Verify of a used code is rejected with a clear error; repeating confirm returns the already-recorded handover without creating a second collection. No double collection.|
| Actual | <實際結果> |
| Completion time | <開始到完成，例如 3m 20s> |
| Blockers | None / <卡點> |
| Verdict | PASS / FAIL |
| Evidence | <video timestamp / screenshot / CI run> |

### ACC-W05-002

| Field | Detail |
|---|---|
| Date | <date> |
| Observer | <name> |
| Parcel type | <e.g. Large parcel / Fragile parcel / Document envelope> |
| Scenario | Duplicate waybill rejection |
| Steps observed | <簡述> |
| Expected | Duplicate rejected with clear error; no second row |
| Actual | <實際結果> |
| Completion time | <time> |
| Blockers | <卡點或 None> |
| Verdict | PASS / FAIL |
| Evidence | <link> |

### ACC-W05-003

| Field | Detail |
|---|---|
| Date | <date> |
| Observer | <name> |
| Parcel type | <e.g. Standard parcel> |
| Scenario | Used pickup code rejection |
| Steps observed | <簡述> |
| Expected | Used code rejected; no double collection |
| Actual | <實際結果> |
| Completion time | <time> |
| Blockers | <卡點或 None> |
| Verdict | PASS / FAIL |
| Evidence | <link> |

## Summary

| Observation | Date | Parcel type | Verdict |
|---|---|---|---|
| ACC-W05-001 | 2026-11-05 | Standard parcel | PASS/FAIL |
| ACC-W05-002 | <date> | <type> | PASS/FAIL |
| ACC-W05-003 | <date> | <type> | PASS/FAIL |

## Blockers and follow-ups
<fill in>
