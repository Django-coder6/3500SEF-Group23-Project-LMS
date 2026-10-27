# Test case conventions

| Item | Detail |
|---|---|
| Status | Active |
| Version | v1.1|

All test cases must use the standard 12-column format. Copy the Markdown table below when creating a new test case:

## Naming Convention
### Test Case ID
- **Format**: `TC-<MODULE>-<nnn>`
- **TC**: Test Case
- **`<MODULE>`**: 4 uppercase letters representing the module (e.g., `PARC` for Parcel, `PICK` for Pickup).
- **`<nnn>`**: 3-digit sequential number starting from `001`.
- **Examples**: `TC-PARC-001`, `TC-PICK-007`.
- **Note**:`TC-AUTH-001`–`TC-AUTH-004` are implemented by `AuthSecurityIntegrationTest`. Its test methods are named after the behaviour under test rather than after the reserved TC IDs. The RTM records the traceability mapping.


### Defect ID
- **Format**: `DEF-<MODULE>-<nnn>`
- **DEF**: Defect
- **`<MODULE>`**: The same module code as the failed test case (e.g., `PARC`, `PICK`).
- **`<nnn>`**: 3-digit sequential number starting from `001`.
- **Examples**: `DEF-PARC-001`, `DEF-PICK-003`.
- **Usage Rule**: When a test case fails (Verdict = Fail), a defect ID must be generated and filled into the `Defect` column of the test case



## Test Template
Every test case must use the following 12-column format. Copy this table when creating new cases

| Field | Meaning |
|---|---|
| **Case ID** | TC-XXX-001 |
| **Requirement** | FR-x / NFR-x |
| **Level** | System / Integration / Component / Slice / Performance / Acceptance |
| **Preconditions** | 1. System state before the steps<br>2. Additional precondition |
| **Steps** | 1. Step one<br>2. Step two<br>3. Step three |
| **Test data** | Input value 1: xxx<br>Input value 2: yyy |
| **Expected result** | What the system must do |
| **Actual result** | What it actually did (leave blank until executed) |
| **Verdict** | Pass / Fail (leave blank until executed) |
| **Defect** | DEF-001 (leave blank if passed) |
| **Executed by** | Testers' Names |
| **Date** | YYYY-MM-DD |



## Five Testing Dimensions Checklist
To ensure comprehensive testing coverage, the following five aspects must be checked before each test and acceptance:

| Done | # | Dimension | What to check | Who checks | Evidence location |
|---|---|---|---|---|---|
| [ ] | 1 | **Correctness of functional modules** | - Does the module strictly follow its FR?<br>- Are all methods returning expected types? | Developer / R8 | `backend/src/test/java/` (JUnit/Mockito reports) |
| [ ] | 2 | **Completeness of functional workflows** | - Does the intake-to-handover flow work end-to-end?<br>- Are there any broken transitions? | R8 | `docs/04-testing/reports/` (Integration test logs) |
| [ ] | 3 | **Accuracy of input and output** | - Are fee calculations rounded correctly?<br>- Is input data saved exactly as entered? | Developer / R8 | `docs/04-testing/reports/` (Test data logs) |
| [ ] | 4 | **Handling of abnormal input** | - Are missing fields rejected?<br>- Are duplicate waybill numbers blocked?<br>- Are expired codes rejected? | R8 | `docs/04-testing/test-cases/` (Verdict = Fail/Pass) |
| [ ] | 5 | **Implementation of business rules** | - Does the recommender follow rules?<br>- Is the retention period enforced?<br>- Is the overdue fee correct? | R8 | `docs/04-testing/reports/` (Acceptance/Performance reports) |

## Reserved test case identifiers

| ID | Requirement | Level | What it verifies | Written in |
|---|---|---|---|---|
| TC-ADM-001 | FR-40 | Slice | An administrator lists accounts across all points | Sprint 2 |
| TC-AUD-001 | FR-35 | Integration | An owner views logs only for managed points | Sprint 2 |
| TC-AUTH-001 | FR-1 | Slice | A registered resident signs in and receives a session | Sprint 2 |
| TC-EXC-001 | FR-23 | Component | Staff record an exception as exactly one of five approved types | Sprint 2 |
| TC-FEE-001 | FR-39 | Component | An overdue fee is calculated without taking payment | Sprint 2 |
| TC-LOC-001 | FR-14 | Integration | Staff record a parcel's storage location when shelving | Sprint 2 |
| TC-NOTI-001 | FR-6 | Integration | Shelving a parcel creates one in-system arrival notification | Sprint 2 |
| TC-OPS-001 | FR-46 | Component | Every scoped record carries a collection point identifier | Sprint 2 |
| TC-OWN-001 | FR-26 | Slice | An owner views the daily summary for their managed points | Sprint 2 |
| TC-PARC-001 | FR-12 | Integration | A single arriving parcel is registered | Sprint 2 |
| TC-PICK-001 | FR-21 | Integration | An active pickup code is verified before handover | Sprint 2 |
| TC-RES-001 | FR-2 | Integration | A resident sees only their own parcels in the list | Sprint 2 |
| TC-UI-001 | NFR-9 | System | Every error names what failed and what to do next | Sprint 2 |
| TC-ADM-002 | FR-41 | Slice | An administrator approves a pending registration | Sprint 2 |
| TC-AUD-002 | FR-36 | Slice | An owner filters logs by parcel ID | Sprint 2 |
| TC-AUTH-002 | FR-45 | Slice | A user is refused an operation their role does not permit | Sprint 2 |
| TC-EXC-002 | FR-24 | Component | An exception moves only through open → processing → resolved | Sprint 2 |
| TC-FEE-002 | NFR-12 | Component | The overdue fee matches an independent recalculation | Sprint 2 |
| TC-LOC-002 | FR-15 | Slice | Storage location availability is displayed to staff | Sprint 2 |
| TC-NOTI-002 | FR-7 | Integration | A resident views their own in-system arrival notifications | Sprint 2 |
| TC-OPS-002 | NFR-13 | System | The test suite stays green on every commit to develop | Sprint 2 |
| TC-OWN-002 | FR-27 | Integration | An owner views parcel records from their managed points | Sprint 2 |
| TC-PARC-002 | FR-17 | Integration | A duplicate waybill number is rejected | Sprint 2 |
| TC-PICK-002 | NFR-7 | Integration | A used code is refused a second handover | Sprint 2 |
| TC-RES-002 | FR-3 | Integration | A resident opens a detail view of their own parcel | Sprint 2 |
| TC-UI-002 | NFR-10 | Acceptance | The system is usable at 375 px with no horizontal scroll | Sprint 2 |
| TC-ADM-003 | FR-42 | Slice | An administrator disables a user account | Sprint 2 |
| TC-AUD-003 | FR-37 | Slice | An owner filters logs by storage location ID | Sprint 2 |
| TC-AUTH-003 | NFR-3 | Component | Every endpoint refuses a role it does not permit | Sprint 2 |
| TC-LOC-003 | FR-16 | Component | Staff manually select a storage location | Sprint 2 |
| TC-NOTI-003 | NFR-1 | Performance | A resident is notified of arrival within a bounded delay | Sprint 2 |
| TC-OPS-003 | NFR-14 | System | One command starts the system; schema changes use migrations | Sprint 2 |
| TC-OWN-003 | FR-28 | Slice | An owner filters parcels by a managed point | Sprint 2 |
| TC-PARC-003 | FR-18 | Integration | A batch of 50 is registered in one operation | Sprint 2 |
| TC-PICK-003 | NFR-4 | Integration | Two concurrent confirmations write one handover | Sprint 2 |
| TC-RES-003 | FR-4 | Slice | Each parcel shows its current approved lifecycle status | Sprint 2 |
| TC-ADM-004 | FR-43 | Slice | An administrator creates a collection point | Sprint 2 |
| TC-AUD-004 | FR-47 | Integration | An administrator views logs across all points | Sprint 2 |
| TC-AUTH-004 | NFR-6 | Component | The handover operator comes from the session, never the request | Sprint 2 |
| TC-LOC-004 | FR-19 | Slice | An optional location suggestion is shown, never auto-applied | Sprint 2 |
| TC-OWN-004 | FR-29 | Integration | An overdue report covers only the owner's managed points | Sprint 2 |
| TC-PARC-004 | FR-11 | Integration | Staff view parcels at their assigned collection point | Sprint 2 |
| TC-PICK-004 | FR-22 | Integration | A confirmed handover records the operator from the session | Sprint 2 |
| TC-RES-004 | FR-5 | Slice | A shelved parcel's storage location is shown to its resident | Sprint 2 |
| TC-ADM-005 | FR-44 | Slice | An administrator updates a point's basic configuration | Sprint 2 |
| TC-AUD-005 | NFR-11 | Component | Every sensitive operation is audited; phone numbers are masked | Sprint 2 |
| TC-LOC-005 | FR-20 | Slice | Staff can ignore a suggestion and shelve manually | Sprint 2 |
| TC-OWN-005 | FR-30 | Component | An owner compares volume across their managed points | Sprint 2 |
| TC-PARC-005 | FR-13 | Integration | Staff view a parcel's handling history | Sprint 2 |
| TC-PICK-005 | FR-25 | Integration | Confirming a handover creates one audit entry | Sprint 2 |
| TC-RES-005 | FR-8 | Integration | A resident retrieves their own pickup code | Sprint 2 |
| TC-LOC-006 | NFR-5 | Component | One storage location holds at most one active parcel | Sprint 2 |
| TC-OWN-006 | FR-31 | Slice | An owner views the collected count per managed point | Sprint 2 |
| TC-RES-006 | NFR-2 | Performance | The resident collection journey completes within a bounded time | Sprint 2 |
| TC-OWN-007 | FR-32 | Slice | An owner lists staff at their managed points | Sprint 2 |
| TC-OWN-008 | FR-33 | Slice | An owner assigns a staff account to their managed point | Sprint 2 |
| TC-OWN-009 | FR-34 | Integration | Owner parcel queries are limited to managed points | Sprint 2 |
| TC-OWN-010 | FR-38 | Slice | An owner views the retention period of a managed point | Sprint 2 |
| TC-OWN-011 | NFR-8 | Component | Each point's data is separated by a collection point identifier | Sprint 2 |
| TC-PARC-006 | FR-18 | Integration | A mixed batch registers every valid row and reports each invalid row (partial success); valid rows remain committed and visible | Sprint 2 |
| TC-PARC-007 | FR-18 | Integration | A batch where every row is invalid registers nothing and returns an actionable error for each row | Sprint 2 |
| TC-PARC-008 | FR-18 | Integration | **BLOCKED — #168, R6 ruling pending:** same-batch duplicate waybill behaviour (reject whole batch / dedupe / per-row error) is undefined, so no expected result can be written yet. Do not execute until R6 rules. | Sprint 2 (blocked — #168) |


## Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v1.0 | Week 2 | First version recorded in this file. The test case format, the identifier conventions and the 59 reserved identifiers. `#171` adds cases to it. Any earlier edit is not listed: this file had no revision history until week 3, so records of those edits do not exist here. | R1 |
| v1.1 | Week 3 | #171: added TC-PARC-006 (mixed batch partial success), TC-PARC-007 (all-invalid batch), and TC-PARC-008 (same-batch duplicate waybill), marked BLOCKED pending R6 ruling on #168. Reserved identifiers now 62. | R8 |
