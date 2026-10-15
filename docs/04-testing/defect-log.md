# Defect Log

This document records all defects found during testing. 
It is created in Sprint 1 (before any code is written) so that the format and rules are already in place when the first bug appears in Sprint 2.

## Why is it valuable to create an empty defect log first?

Although Sprint 1 has no code and no defects yet, creating `defect-log.md` in advance has three benefits:

1. **No need to invent a format later**: Defects will definitely appear in the first week of Sprint 2. By then, testers can simply copy the table and fill in the data, instead of spending time discussing which fields should be recorded.
2. **Unified severity rules**: P0 is fixed on the same day, and P1 is fixed within the sprint. With these rules written in advance, developers immediately know which bug should be fixed first.
3. **Enforced closure**: Closing a defect must reference a test case that has changed from failed to passed (for example, `TC-PARC-001`). This prevents cases where a defect is closed without verification after someone claims it has been fixed.

Therefore, this is an empty "defect report template", but it ensures that bug management from the start of Sprint 2 is orderly rather than chaotic.

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

### Status meanings
- **Open** – Logged by QA; not fixed yet.
- **Fixed** – Developer says it is fixed; waiting for QA verification.
- **Closed** – QA verified the failed test case now passes.

**Closing Rule**: A defect is closed **only when a test case that failed now passes**. The test case ID must be referenced in the closing evidence (for example, `TC-PARC-001`).






