# W06 backend architecture notes and Q&A

Task [T-60-06 / #327](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/issues/327).
Baseline `5ac9d89`, [CI run 43037569806](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/actions/runs/43037569806).
Audience: R1 for slides 6 and 7, and anyone taking architecture questions on 11/17. The
review has no live demo, so these answers rely on committed tests and captures, not a running
stack.

## 1. Slide 6, architecture in one line

```text
Vue 3 SPA -> REST contract -> Spring Boot controller / service / repository
-> PostgreSQL with Flyway, plus one audit aspect.
```

That is ADR-002: a layered monolith. ADR-003 fixes the frontend, ADR-005 selects Spring
Boot. The schema is one Flyway baseline of nine tables.

## 2. Slide 7, implementation evidence in one line

```text
Contract frozen before frontend work; 9 operations served, 6 named as gaps;
90 backend tests and 88.79% line coverage on CI 43037569806.
```

The honest edge matters more than the total: FR-11 `GET /parcels` has no controller and the
resident read paths exist only in the contract. Details and links are in
[w06-backend-evidence](../../05-management/plans/w06-backend-evidence.md).

## 3. Why a layered monolith

Four developers had eight weeks, so every service boundary would have cost deployment,
discovery and cross-service testing time. The domain also argues for one process: a handover
changes parcel, location and audit together, and those writes are simplest to keep correct
inside one transaction. ADR-002 records the choice and the rejected microservice option.

## 4. Reuse and custom code

Reused: Spring Boot, Vue 3, PostgreSQL, Flyway, Nginx and Testcontainers. None of these is
presented as our work.

Written for this project: the parcel lifecycle, the pickup-code and handover transaction,
and the audit aspect with payload masking. Those are the parts worth defending, and they are
the parts covered by the integration tests.

## 5. Contract first

The OpenAPI file was frozen before frontend feature work, so Vue pages and Spring services
could proceed in parallel. Where the backend was not ready, the operation stayed in the
contract marked "specified and not built" instead of drifting or disappearing. CR-013 added
the three resident read paths on the same principle: the shape is agreed even though the
implementation is later.

## 6. Transaction and audit at a glance

```text
POST /parcels (locationId)
  one transaction
    parcel row (registered -> shelved)
    location row occupied
    pickup code row issued
    audit: parcel.register, parcel.shelve

POST /pickup/confirm (code)
  lock code -> status check -> parcel collected
    location freed
    handover row + audit id
    audit: handover.confirm
  repeat call returns the stored handover, no new rows
```

The operator comes from the authenticated session, not from the request body. Phone numbers
and codes are masked in audit payloads.

## 7. Q&A

**1. Why a layered monolith rather than microservices?**
Four developers, eight weeks. Splitting the service would have added deployment, discovery
and cross-service testing for no gain at this scale. Handover also writes parcel, location
and audit together, which is one transaction in one process. ADR-002 records it.

**2. What did you reuse, and what did you write?**
We reused Spring Boot, Vue 3, PostgreSQL, Flyway, Nginx and Testcontainers. We wrote the
parcel lifecycle rules, the registration/shelving/handover transactions and the audit
aspect. Those are the parts the tests target.

**3. How do you know registration, shelving and code issuance share one transaction?**
`ParcelRegistrationService.register` is `@Transactional` and calls `shelve`, which uses the
default propagation and joins it. The integration test
`registersAndShelvesWithPickupCodeInOneRequest` checks the parcel, the occupied location and
the issued code from one request, and ST-SYS-001 shows the same through the API and the
database.

**4. FR-11 is Must. Why is `GET /parcels` not built?**
Must is the product priority, not a delivery date. The endpoint has no controller, and the
RTM records it as a Sprint 3 gap under #289. We kept the contract and the requirement rather
than lower the priority or quietly drop the operation.

**5. How do you prevent a parcel being collected twice?**
The pickup code is unique and has a status. Verifying a used code returns 404
`PICKUP_CODE_USED`, so the counter cannot start a second handover. Confirming twice is
idempotent: the second call returns the stored handover with 200 and writes no new handover
or audit row. ST-SYS-003 captures both cases.

**6. What does an audit record contain?**
The actor from the session, the action, the target and a timestamp, plus a sanitised payload.
`Handover.auditId` ties the handover to its row. Passwords, tokens, pickup codes, actor ids
and phone numbers are masked before the row is stored.

**7. What does 88.79% line coverage mean here, and what does it not mean?**
It is 808 of 910 lines executed by 90 tests on `5ac9d89`. It does not mean every requirement
is implemented; FR-11 and the resident reads are not built. It also does not mean every
branch is tested, since branch coverage is 64.64%.

## 8. Do not say

- "Microservices." The design is a layered monolith by decision.
- "Every endpoint works." Six contract operations have no controller.
- "The audit log can be searched in the app." There is no query endpoint.
- "Coverage proves correctness." Coverage shows which lines ran; the system-test captures
  show behaviour.
- "Demo." There is no live demo on 11/17.

## 9. Sources

- [ADR-002 backend architecture](../../02-design/adr/ADR-002-backend-architecture.md)
- [Report chapter 5, architecture](../../07-report/chapters/05-architecture.md)
- [Report chapter 6, implementation](../../07-report/chapters/06-implementation.md)
- [FR-12 traceability chain](../../01-requirements/traceability-chain-FR-12.md)
- [W06 backend evidence](../../05-management/plans/w06-backend-evidence.md)
- [System test report W05](../../04-testing/reports/system-test-report-W05.md)

## 10. Revision history

| Version | Date | Change | Author |
|---|---|---|---|
| v1.0 | 2026-11-12 | First architecture notes and seven Q&A answers on baseline `5ac9d89` | R6 |
