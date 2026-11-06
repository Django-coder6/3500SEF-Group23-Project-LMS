# Interim Test Report — Week 5

| Item | Detail |
|---|---|
| Report type | Interim (end of Week 5) |
| Version tested | commit `30f776b` (w05r81) |
| Report commit | `6cd943f` |
| Environment | GitHub Codespaces, Docker Compose `--profile full`, http://localhost:80 |
| Test period | 2026-11-06 – 2026-11-07 |
| Prepared by | R8 |
| Evidence base | `system-test-report-W05.md`, `acceptance-observation-W05.md` |

## 1. Scope

**In scope:**
- Staff parcel lifecycle: register → shelve → verify → confirm (FR-12, FR-14, FR-21)
- Duplicate waybill rejection (FR-12, FR-17)
- Used pickup code rejection and idempotent handover (FR-21, NFR-4, NFR-7)

**Out of scope this phase:**
- Resident read paths (`/my-parcels`, CR-013) — specified and not built
- Batch registration (`POST /parcels/batch`, FR-18)
- Location recommendation (`/locations/recommend`, deferred by CR-023)
- Exception handling (`/exceptions`, FR-23/24, still P1 Proposed)

## 2. Execution Statistics

### System tests

| Metric | Count |
|---|---|
| Planned | 3 |
| Executed | 3 |
| Passed | 3 |
| Failed | 0 |
| Blocked | 0 |
| Pass rate | 100% (3/3) |

### Acceptance observations

| Metric | Count |
|---|---|
| Planned | 3 |
| Executed | 3 |
| Passed | 3 |
| Blocked | 0 |

> ACC-W05-002 was originally planned as duplicate-waybill, which has no UI surface because `/parcels/new` is a placeholder. It was substituted with an invalid-code probe on `/pickup`, exercising the same pickup error path. Duplicate rejection itself is covered at the API level by ST-SYS-002.

## 3. Findings

| ID | Finding | Severity | Status |
|---|---|---|---|
| F-01 | Staff registration UI not implemented: `/dashboard`, `/parcels`, `/parcels/new` are placeholders. Only `/pickup` and `/locations` are working staff screens. | Major (scope gap) | Open — Sprint 2 scope |
| F-02 | Shelf released automatically on confirm (`locations.status` → `available`, `parcels.location_id` → `NULL`). Contract does not state this explicitly. | Minor (needs confirmation) | Open — confirm intended |
| F-03 | CI run 42239430380 failed at `commitlint` on commit `9273b705` (message format only, not build/test). | Minor (process) | Resolved in report commit `6cd943f` |
| F-04 | `recipientPhone` masked in create response (`138****8888`), not only in list responses. | Info | Accepted |

## 4. Coverage Gaps

| Area | Status | Impact |
|---|---|---|
| Staff registration (FR-12) — UI | Not covered in UI; API covered by ST-SYS-001/002 | A staff user cannot register through the browser in this build. |
| Duplicate waybill (FR-17) — UI | Not observable; API covered by ST-SYS-002 | No UI form for registration. |
| Resident read paths (FR-2/3/8) | Not built | Outside this phase's scope. |
| Batch registration (FR-18) | Not tested | Not exercised this phase. |

## 5. Risks and Open Items

| Item | Type | Next step |
|---|---|---|
| Registration UI missing | Risk | Team to confirm Sprint 2 / 3 scope. |
| Shelf release on confirm | To confirm | Confirm with R2 / R5 whether intended. |
| ACC-002 substituted | To confirm | Team to accept the API-level substitute or schedule a UI re-test when registration UI exists. |

## 6. Conclusion

**Conditional pass for Week 5.** All three system tests and all three acceptance observations pass. API behaviour for register → shelve → verify → confirm, duplicate rejection and used-code handling is correct. The staff registration UI is not implemented in this build, so the browser-based intake flow cannot be exercised end-to-end; this is a known scope item, not a test failure.

## 7. Attachments

- `system-test-report-W05.md` — 3 system tests, full actual results, DB evidence
- `acceptance-observation-W05.md` — 3 UI observations with dates and parcel types
- `evidence/` — 9 system-test evidence files + 3 acceptance DB outputs
EOF
