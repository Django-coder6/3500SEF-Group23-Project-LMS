# Test Plan

## COMP 3500SEF Group 23

### Community Parcel Collection Point Management System

| Item | Detail |
|---|---|
| Task | T-50-19 |
| Owner | R8 |
| Version | v0.1 (draft for Sprint 1) |
| Status | Draft, to be completed once the SRS is frozen |

## 1. Purpose and scope

This plan describes how the system will be tested, at which levels, and what
counts as evidence that a requirement has been met.

The plan follows the three levels used in the course material: component
testing, system testing, and customer or acceptance testing. Integration
testing sits between the first two, because the intake to handover path crosses
several components.

### 1.1 In scope

All must-have and should-have functional requirements, and the measurable
targets in the non-functional requirements.

### 1.2 Out of scope

Hardware behaviour, because the system controls no hardware. Courier platform
integration and payment, because both are outside the system boundary.

## 2. Test levels

| Level | What it covers | Tool | Owner | Evidence |
|---|---|---|---|---|
| Component | One class or one service method in isolation, with dependencies mocked | JUnit 5, Mockito | The developer who wrote it | `backend/src/test/java/` |
| Slice | One controller with its request and response mapping, without a running server | MockMvc | The developer, reviewed by R8 | `backend/src/test/java/` |
| Integration | Several components together, against a real PostgreSQL instance | JUnit 5 with Testcontainers or a Docker database | R8 | `docs/04-testing/reports/` |
| System | A complete business flow through the running application | Manual scripted cases plus Playwright | R8 | `docs/04-testing/test-cases/` |
| Performance | Response time and throughput targets under load | A load tool against the deployed build | R8 | `docs/04-testing/reports/` |
| Acceptance | A person who has not used the system completes a task unaided | Manual, observed and timed | R8 | `docs/04-testing/reports/` |

## 3. Coverage targets

These come from the non-functional requirements. They are targets, not
aspirations: the build reports them, and a shortfall is recorded as a defect.

| Area | Target |
|---|---|
| Line coverage overall | At least 60 per cent |
| Handover service | At least 80 per cent |
| Storage location recommender | At least 80 per cent |
| Overdue fee engine | At least 80 per cent |
| Must-have requirements with at least one test case | 100 per cent |
| Checkstyle errors | Zero |

## 4. What must be tested in every case

Every test case records the following, so that a result can be reproduced:

| Field | Meaning |
|---|---|
| Case ID | `TC-<MODULE>-nnn`, for example `TC-PICK-007` |
| Requirement | The FR or NFR identifier it verifies |
| Level | Component, slice, integration, system, performance or acceptance |
| Preconditions | The state the system must be in before the steps |
| Steps | Numbered actions |
| Test data | The exact values used |
| Expected result | What the system must do |
| Actual result | What it did |
| Verdict | Pass or fail |
| Defect | The defect ID if it failed |
| Executed by | Who ran it |
| Date | When it ran |

## 5. Test scope dimensions

Testing is organised around five dimensions. This framing comes from the review
of a previous cohort's project and is used here because it covers more than a
list of features would.

| # | Dimension | What it means here |
|---|---|---|
| 1 | Correctness of functional modules | Each module does what its requirement says |
| 2 | Completeness of functional workflows | A whole flow completes, not just each step in isolation |
| 3 | Accuracy of input and output | Values in equal values out, including formatting and rounding of fees |
| 4 | Handling of abnormal input | Missing fields, wrong types, duplicate waybill numbers, expired pickup codes |
| 5 | Implementation of business rules | The recommender, the retention period and the overdue fee follow the stated rules |

## 6. High priority test areas

These are the areas where a defect would damage the demonstration or a
requirement target. They are tested first and re-tested after every change.

| Area | Why it is high priority | Requirement |
|---|---|---|
| Handover idempotency under concurrency | A double handover loses a parcel and cannot be undone | NFR-4 |
| One storage location cannot hold two parcels | Enforced by a database constraint, so the test must attempt the violation | NFR-5 |
| Overdue fee calculation | Must agree with an independent recalculation for at least 20 prepared cases | FR-39, NFR-12 |
| Pickup code single use and expiry | Replay of a used code would allow a second handover | NFR-7 |
| Duplicate waybill rejection | Two records for one parcel cause a dispute that cannot be settled | FR-17 |
| Notification delay | The resident requirement states notice within one hour | FR-6, NFR-1 |
| Mobile viewport usability | 9 of 11 residents use a phone | NFR-10 |

## 7. Defect management

| Severity | Meaning | Response |
|---|---|---|
| P0 | The system cannot be demonstrated, or data is corrupted | Fixed before any other work, same day |
| P1 | A must-have requirement does not work | Fixed within the sprint |
| P2 | A should-have requirement does not work, or the workaround is awkward | Scheduled into the next sprint |
| P3 | Cosmetic or a minor inconvenience | Recorded, fixed if time allows |

A defect is closed only when a test case that failed now passes, and the test
case is referenced in the closing comment.

## 8. Entry and exit criteria

### Entry to system testing

- The build compiles and the continuous integration pipeline is green.
- All must-have features for the sprint are present.
- Component and slice tests pass.
- The test data set is loaded.

### Exit from system testing, before the progress review

- Every must-have requirement has at least one test case, and all pass.
- No P0 or P1 defect is open.
- The concurrency test for handover passes.
- The performance targets have been measured and recorded, whether met or not.

## 9. Test environment

| Item | Value |
|---|---|
| Backend | Java 17, Spring Boot 3.3, built with the Maven wrapper |
| Database | PostgreSQL 16, populated from the Flyway migration and the seed script |
| Frontend | Vue 3 build served by the development server |
| Environment | Docker Compose, so the same environment is reproducible on any machine |
| Browsers | Current Chrome, Edge and Firefox, plus a 375 px viewport |
| Test data | Generated. No real personal data is used |

## 10. Schedule

| Sprint | Testing activity |
|---|---|
| Sprint 1 | This plan. Test case templates prepared. No execution yet |
| Sprint 2 | Component and slice tests with each module. First integration pass on the intake to handover path |
| Sprint 3 | Second integration pass. Test cases for notifications, exceptions and stocktake. Overdue fee cases |
| Sprint 4 | Full system testing across the five dimensions, performance, security and acceptance testing. Test report |

## 11. Reporting

Progress is reported in `docs/04-testing/reports/`, in three forms:

1. A defect list, updated continuously, with severity and status.
2. A coverage report, produced by the continuous integration pipeline on each build.
3. A test report at the end of Sprint 4, containing the results, the defects
   found and closed, the coverage figures, and an insight and recommendation
   entry for each significant finding.

## Revision history

| Version | Date | Change | Author |
|---|---|---|---|
| v0.1 | Week 2 | Initial draft from the plan and the survey findings | R8 |
