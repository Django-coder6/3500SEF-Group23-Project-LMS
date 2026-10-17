# Test case conventions

All test cases must use the standard 12-column format. Copy the Markdown table below when creating a new test case:

## Naming Convention
### Test Case ID
- **Format**: `TC-<MODULE>-<nnn>`
- **TC**: Test Case
- **`<MODULE>`**: 4 uppercase letters representing the module (e.g., `PARC` for Parcel, `PICK` for Pickup).
- **`<nnn>`**: 3-digit sequential number starting from `001`.
- **Examples**: `TC-PARC-001`, `TC-PICK-007`.


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
| TC-AUTH-001 | FR-1 | Slice | A registered resident signs in and receives a session | Sprint 2 |
| TC-AUTH-002 | FR-45 | Slice | A resident is refused an admin-only route | Sprint 2 |
| TC-AUTH-003 | NFR-11 | Component | Every endpoint refuses a role it does not permit | Sprint 2 |
| TC-PARC-001 | FR-12 | Integration | A single arriving parcel is registered | Sprint 2 |
| TC-PARC-002 | FR-17 | Integration | A duplicate waybill number is rejected | Sprint 2 |
| TC-PARC-003 | FR-18 | Integration | A batch of 50 is registered in one operation | Sprint 2 |
| TC-PICK-001 | FR-21 | Integration | An active pickup code is verified | Sprint 2 |
| TC-PICK-002 | NFR-7 | Integration | A used code is refused a second handover | Sprint 2 |
| TC-PICK-003 | NFR-4 | Integration | Two concurrent confirmations write one handover | Sprint 2 |
| … | … | … | … | … |
