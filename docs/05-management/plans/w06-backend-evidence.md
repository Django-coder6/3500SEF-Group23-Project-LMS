# W06 backend/API evidence

Task [T-60-06 / #327](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/issues/327).

Review baseline `5ac9d898995c5f7ad27e0befc7623e6d4c929676`, the merge of #340 into `develop`.
The [develop push run 43037569806](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/actions/runs/43037569806)
passes all five jobs. The 11/17 review has no live demo, so every claim below is tied to a
test, a CI artifact or a committed system-test capture.

Owner: R6 (backend). Consumers: R1 (implementation-evidence slide and report), R3 (RTM
consistency). This file is supporting material; it does not change the API contract.

## 1. Slide line, checked clause by clause

Text under review:

```text
POST /parcels with locationId -> 201, status=shelved, pickup code issued;
POST /pickup/verify -> 200;
POST /pickup/confirm -> 200, collected / code used / location free.
```

| Clause | Holds on `5ac9d89` | Evidence |
|---|---|---|
| `POST /parcels` with `locationId` returns 201 | Yes | [`ParcelRegistrationIntegrationTest`](../../../backend/src/test/java/com/group23/parcel/ParcelRegistrationIntegrationTest.java), [ST-SYS-001-register](../../04-testing/reports/evidence/ST-SYS-001-register.txt) |
| Response status is `shelved` | Yes | same |
| A pickup code is issued in the same request | Yes | `ParcelRegistrationIntegrationTest.registersAndShelvesWithPickupCodeInOneRequest` |
| `POST /pickup/verify` returns 200 | Yes | [`PickupHandoverIntegrationTest`](../../../backend/src/test/java/com/group23/pickup/PickupHandoverIntegrationTest.java), [ST-SYS-001-verify](../../04-testing/reports/evidence/ST-SYS-001-verify.txt) |
| `POST /pickup/confirm` returns 200 | Yes | `PickupHandoverIntegrationTest`, [ST-SYS-001-confirm](../../04-testing/reports/evidence/ST-SYS-001-confirm.txt) |
| Parcel becomes `collected` | Yes | ST-SYS-001-confirm, [ST-SYS-001-db](../../04-testing/reports/evidence/ST-SYS-001-db.txt) |
| Pickup code becomes `used` | Yes | same |
| Location returns to `available` | Yes | same |

Verify returns the parcel summary with `pickupCode` null, so the code is not echoed back at
the counter. ST-SYS-001-verify shows the parcel and its location while the location is
still occupied.

## 2. Endpoint checklist

Operations with a controller on `5ac9d89`:

| Operation | Endpoint | Expected on success | Evidence |
|---|---|---|---|
| Health | `GET /health` | 200 | `HealthControllerTest` |
| Login | `POST /auth/login` | 200 + token | `AuthSecurityIntegrationTest` |
| Register, optionally shelve | `POST /parcels` with `locationId` | 201, `shelved`, pickup code | `ParcelRegistrationIntegrationTest` |
| Batch register | `POST /parcels/batch` | 201, `created` and `rejected` arrays | `ParcelRegistrationIntegrationTest.batchCreatesValidEntryAndReportsDuplicateEntry` |
| Move a parcel to a location | `PATCH /parcels/{id}/location` | 200, location occupied | `ParcelShelvingIntegrationTest` |
| List locations | `GET /locations` | 200 with occupancy | `LocationManagementIntegrationTest` |
| Change location state | `PATCH /locations/{id}/status` | 200 | `LocationManagementIntegrationTest` |
| Verify a code | `POST /pickup/verify` | 200, parcel and location, no code | `PickupHandoverIntegrationTest`, ST-SYS-001-verify |
| Confirm handover | `POST /pickup/confirm` | 200, collected, code used, location free | `PickupHandoverIntegrationTest`, ST-SYS-001-confirm |

A batch over fifty entries returns 400 `BATCH_LIMIT_EXCEEDED` and creates nothing.

## 3. Error and repeat paths

| Case | Result | Evidence |
|---|---|---|
| Duplicate tracking number | 409 `DUPLICATE_TRACKING_NO`, one row kept | `ParcelRegistrationIntegrationTest`, [ST-SYS-002-duplicate](../../04-testing/reports/evidence/ST-SYS-002-duplicate.txt) |
| Verify a used code | 404 `PICKUP_CODE_USED` | `PickupHandoverIntegrationTest`, [ST-SYS-003-verify-used](../../04-testing/reports/evidence/ST-SYS-003-verify-used.txt) |
| Confirm the same handover twice | 200 with the original handover, no new rows | `PickupHandoverIntegrationTest`, [ST-SYS-003-confirm-repeat](../../04-testing/reports/evidence/ST-SYS-003-confirm-repeat.txt) |
| Concurrent shelving of one location | 422 `UNPROCESSABLE_ENTITY`, not 500 | `ParcelShelvingIntegrationTest` |

Used code and repeat confirm are different paths: verify rejects the code because the parcel
is already out, while confirm returns the stored handover so a double click at the counter
does not create a second record. A used code with no handover row still returns 404.

## 4. Contract operations without a controller

`openapi.yaml` declares fifteen operations. Nine have a controller. Six do not, and each is
already recorded in the RTM gap table. Keep them separate from the implemented list.

| Operation | Requirement | Status | RTM |
|---|---|---|---|
| `GET /parcels` | FR-11 (also FR-27, FR-28, FR-34) | No controller; Sprint 3 gap #289 | GAP 14 |
| `PATCH /parcels/{id}/status` | FR-16 generic status update | Not implemented; not the same as `PATCH /parcels/{id}/location` | GAP 15 |
| `GET /my-parcels` | FR-2 | CR-013 contract only | GAP 16 |
| `GET /my-parcels/{id}` | FR-3 | CR-013 contract only | GAP 17 |
| `GET /my-parcels/{id}/pickup-code` | FR-8 | CR-013 contract only | GAP 18 |
| `POST /locations/recommend` | FR-19, FR-20 | Deferred under CR-023 (Could); returns 404 | GAP 19 |

Do not describe the resident paths as "coming in this sprint". CR-013 fixed their shape; the
implementation is later work. Do not describe `POST /locations/recommend` as broken either:
it is a deliberately deferred Could feature.

Count check: `openapi.yaml` marks five operations "specified and not built" (GAP 15-19).
`GET /parcels` is the sixth operation without a controller and is recorded separately as
GAP 14. Saying "five paths are specified and not built" is correct for the contract note;
saying "six operations have no controller" is correct for the endpoint audit. State which
count is meant.

## 5. CI and test numbers

From the artifacts of [run 43037569806](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/actions/runs/43037569806)
on `5ac9d89`:

| Measure | Value |
|---|---|
| Backend tests | 90 passed, 0 failed, 0 errors, 0 skipped across 21 test classes |
| Testcontainers tests | 35 across 7 classes (audit 1, location 5, migration 5, registration 6, shelving 8, pickup 7, user 3) |
| Line coverage | 808 / 910 = 88.79% |
| Branch coverage | 170 / 263 = 64.64% |
| Instruction coverage | 3346 / 3762 = 88.94% |

Artifacts: [Surefire](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/actions/runs/43037569806/artifacts/13864081220),
[JaCoCo](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/actions/runs/43037569806/artifacts/13864675501).
The 88.78% figure quoted in earlier material is the previous `807/909` baseline and should
not be reused.

## 6. FR-12 traceability

```text
SRS FR-12
-> sequence-staff-intake.puml
-> POST /parcels
-> ParcelRegistrationService
-> ParcelRegistrationIntegrationTest
-> CI 43037569806
```

The full chain, including the ADR and report anchors, is in
[traceability-chain-FR-12.md](../../01-requirements/traceability-chain-FR-12.md). Use the
current run 43037569806; the older 42803306061 belongs to `75f3fb1`.

## 7. Transaction and audit facts

Registration:

- `ParcelRegistrationService.register` is `@Transactional` and calls `ParcelShelvingService.shelve`.
- `shelve` uses the default propagation, so it joins the registration transaction.
- Parcel row, location occupancy and pickup-code row therefore commit or roll back together.
- The location turns occupied inside `shelve` through `location.occupy(parcel.getId())`.
- The parcel is read with `findForUpdateById`; the location is read with
  `findForUpdateByIdAndCollectionPointId`, which serialises competing shelves of one location.
- A losing concurrent shelve returns 422 instead of a 500.

Handover:

- `HandoverMutationService.complete` carries `@Audited(action = "handover.confirm")`.
- `AuditLogAspect` writes the audit row on the same transaction and only on a normal return,
  so a rolled-back call leaves no audit row.
- `PendingHandover.attachAudit(auditId)` saves the handover and its audit id together.
- The operator comes from `@AuthenticationPrincipal AuthenticatedUser`; the aspect reads the
  actor from `SecurityContextHolder`. `actorId` and `operatorId` in a request body are
  ignored and masked in the stored payload.
- `AuditPayloadSanitizer` redacts passwords, tokens, authorisation headers, pickup codes,
  actor and operator ids, recipient and full names, and Chinese mobile numbers.
- Repeat confirm locks the pickup code, returns the existing handover with 200 and does not
  enter the mutation service, so no second handover or audit row appears. A used code with
  no handover still returns 404.

Audit volume: registration with shelving writes two rows (`parcel.register`,
`parcel.shelve`); handover writes one (`handover.confirm`).

## 8. What must not be claimed

- "All endpoints are implemented." Six contract operations have no controller.
- "The resident portal reads parcels from the API." Only the contract exists.
- "Coverage is 88.78%." The current line figure is 88.79% on `5ac9d89`.
- "Docker smoke proves the pickup flow." Docker smoke checks startup, both health routes and
  a demo staff login, not the parcel or pickup behaviour.
- "The audit log is queryable." There is no audit query endpoint; the rows are written and
  checked through tests and SQL.
- "FR-11 is deferred because it is optional." It is Must and is late, recorded as GAP 14.

## 9. Consistency checks for other owners

Observed while collecting this evidence; R6 is not editing these files.

| File | Observation | Suggested owner |
|---|---|---|
| `docs/07-report/report-draft.md` | Says thirty-six change records; `change-log.md` holds CR-001 to CR-037 | R1 |
| `docs/07-report/report-draft.md` | Figure 3 cites CI 41977146617; latest green is 43037569806 | R1 |
| `docs/01-requirements/RTM.md` | FR-21, FR-22 and FR-25 still read `designed`, though handlers and tests exist | R3, R8 |

## 10. Delivery checkpoints

| Date | Item | Audience |
|---|---|---|
| 11/11 | Endpoint checklist v0.1 | R1, R3 |
| 11/12 18:00 | Slide backend line, CI run and coverage numbers | R1, R9 |
| 11/13 | Architecture notes and Q&A (see [w06-backend-architecture-notes](../../07-report/presentation/w06-backend-architecture-notes.md)) | R1 |
| 11/16 | Final endpoint and CI inventory | R1 |
| 11/17 | Architecture Q&A only; no live demo | All |

## 11. Revision history

| Version | Date | Change | Author |
|---|---|---|---|
| v1.0 | 2026-11-12 | First evidence pack on baseline `5ac9d89`; CI 43037569806 | R6 |
