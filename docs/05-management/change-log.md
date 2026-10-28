# Change Log

## COMP 3500SEF Group 23

### Community Parcel Collection Point Management System

| Item | Detail |
|---|---|
| Task | T-00-06 |
| Owner | R1, with process sections maintained by R2 |
| Version | v1.15 |
| Status | Active |

## 1. What this log records

Any change to scope, requirements or design is recorded here before it is
carried out. A change that is not in this log did not happen.

### 1.1 The change process

The process has three stages. It follows the change management process
described in the course material on requirements engineering.

| Stage | What happens | Who does it |
|---|---|---|
| 1. Problem analysis and change specification | The request is written down as a change record: what is wrong or missing, and what the requester wants instead. | The person who raises it |
| 2. Change analysis and cost | The effect on requirements, design, code, tests and the schedule is worked out. The cost is stated in hours and in risk. | R2 for requirement and scope effect, R6 on contract and design risk, R7 on implementation effect |
| 3. Change implementation | The change is applied to every affected artefact, and the record is closed with a reference to the pull requests that carried it out. | The owner named in the record |

A change is only started once stage 2 is complete. If the analysis shows the
change would not fit the remaining schedule, the record is closed as rejected,
with the reason stated.

### 1.2 Change record format

| Field | Meaning |
|---|---|
| Change ID | CR-nnn, numbered in order |
| Date | When the record was opened |
| Type | Requirement, design, scope, or process |
| Item affected | The requirement ID, design document or plan entry |
| Before | What the document said before the change |
| Reason | Why the change is needed, with the evidence |
| After | What it will say after the change |
| Impact | High, medium or low, and which artefacts are affected |
| Raised by | The member who raised it |
| Implementing sprint | Which sprint carries the work |
| Status | Proposed, approved, rejected, implemented or closed |

### 1.3 How a record number is taken

**A number is taken when it appears in the table in section 2**, not when it is mentioned in an
issue, a comment or a review.

A number written into an issue is a *reservation*: a suggestion to whoever writes next. It is
courteous to honour it, and this team has honoured it every time it noticed, but **the table is what
decides**. Three collisions have already come from treating a reservation as a claim — `CR-018`,
`CR-026` and `CR-027` — and each was resolved the same way: the record that exists keeps the number
and the reservation moves.

**So when two records reach for the same number**, the one already in the table keeps it. The other
takes the next free one, and the issue or comment holding the reservation is edited to say so.

## 2. Change records

| ID | Date | Type | Item affected | Before | Reason | After | Impact | Raised by | Sprint | Status |
|---|---|---|---|---|---|---|---|---|---|---|
| CR-001 | Week 1 | Design | Backend framework and database, ADR-002 and ADR-005 | The first design baseline specified Node.js with Express 4 and MySQL 8, with Sequelize for migrations | Three reasons: the API contract work was already drawing on a Java/Spring setup, the team had accepted Java as the backend language, and the backend had to follow the same object model and data access style as the chosen architecture | The design baseline was changed to Java with Spring Boot and PostgreSQL, with Flyway or equivalent migrations for schema changes | High. Affects backend architecture, database design and the first implementation plan | R1 | Sprint 1 | Implemented |
| CR-002 | Week 1 | Scope | Elicitation method, plan entry T-10-02 and T-10-03 | The plan included three stakeholder interviews and a competitor analysis of three platforms | With a ten-member team and a tight schedule, that scope was too large for the time available | The elicitation plan was narrowed to the required project interviews, questionnaire analysis and a small design baseline review | Medium. Affects the plan and the requirements collection schedule | R1 | Sprint 1 | Implemented |
| CR-003 | Week 1 | Requirement | FR-18 batch registration, backlog priority | Batch registration of up to 50 parcels was listed as a should-have | Staff questionnaire S3: 4 of 7 respondents named batch intake as an operational need and the plan already expected a bulk intake flow | Batch registration was raised to a must-have requirement for the Sprint 2 workflow and recorded in the requirements baseline | Medium. Affects the backlog, the SRS and the test plan | R3 | Sprint 1 | Implemented |
| CR-004 | Week 1 | Requirement | FR-19 and FR-20, storage location recommendation | The recommendation feature was listed as a must-have and was described in early material as a headline feature | R3 and R6 judged that the algorithm had not been designed and the feature required more investigation than the sprint allowed | The recommendation feature was reduced to a future-work concept and the must-have language was removed from the requirement row | Medium. Affects the SRS, the design notes and the backlog | R3 | Sprint 1 | Implemented |
| CR-005 | Week 1 | Requirement | FR-9 and FR-10, delegated collection | Delegated collection was listed in scope as IS-8 | Resident questionnaire R5: 0 of 11 chose the delegated collection code | Delegated collection was removed from the current scope and left as a future work item | Low. Affects the scope and the resident workflow | R2 | Sprint 1 | Implemented |
| CR-006 | Week 1 | Design | Parcel status enumeration, ERD.md | The status enumeration was `registered, in_storage, ready_for_pickup, out_for_delivery, picked_up, returned, exception` | `out_for_delivery` was not a valid lifecycle status in this project and the design needed a simpler, parcel-specific set | The status set was narrowed to the approved values used by the product flow and recorded in the design baseline | Medium. Affects the data model and the parcel lifecycle documentation | R3 | Sprint 1 | Implemented |
| CR-007 | Week 1 | Design | Pickup confirmation endpoint, openapi.yaml | The `/pickup/confirm` request body required `operatorId` to be supplied by the client | The operator must be taken from the authenticated session, not from a client-supplied field, so the audit trail remains reliable | The request contract was corrected to read the operator from the session and record it in the operation log | High. Affects the contract, the API and the security design | R6 | Sprint 1 | Implemented |
| CR-008 | Week 2 | Requirement | SRS chapters 1 and 2: background, problem statement, scope, functions, user roles, references, assumptions and open issues | Chapters 1 and 2 were written before the team agreed the four role names and the product boundary | The requirement baseline had to be aligned with the agreed personas and with the no-hardware-software-only scope statement | The SRS was revised to reflect the final product, role definitions and design constraints | High. Affects the SRS and the requirements traceability | R2 | Sprint 1 | Implemented |
| CR-009 | Week 2 | Requirement | Persona identifiers, `Personas.md` and SRS section 2.3 | The four personas carried invented personal names (Emily Chen, Daniel Wu, Linda Zhang, Alex Ho) and were described as actual people | The ethics statement and the project process require that personas remain illustrative composites and not real individuals | The persona documents were rewritten to use role names only and to state the composite origin clearly | Medium. Affects the elicitation and SRS drafting | R2 | Sprint 1 | Implemented |
| CR-010 | Week 2 | Design | Exception type enumeration: `ERD.md`, `class-parcel-and-location.puml` and `ui-prototype.md` | The exception types were not defined consistently. `ERD.md` and the class diagram carried different names from the prototype and the glossary | The project needed one domain vocabulary for exception handling across design and screens | The exception types were standardised and aligned with the approved list used elsewhere in the documents | Medium. Affects the domain model and the UI prototype | R3 | Sprint 1 | Implemented |
| CR-011 | Week 2 | Design | Exception type list in `prototype/README.md`, `PLAN.md` and `glossary.md` | The exception types were written down in seven places with four different wordings. `prototype/README.md` and `PLAN.md` used older wording | One term set was needed to make the exception flow consistent across the product design and implementation plan | The glossary and project docs were aligned around the final exception type list | Medium. Affects documentation consistency across the design set | R3 | Sprint 1 | Implemented |
| CR-012 | Week 2 | Process | `docs/glossary.md` as the single source for domain vocabulary; the retired-term rule | Domain terms were repeated in full in the SRS, the ERD, the class diagram, the prototype and the logbook | The team needed one canonical definition to prevent the same concept being described differently in multiple artefacts | The glossary was adopted as the single source and a retired-term rule was defined for wording cleanup | Medium. Affects all design and requirements documents | R2 | Sprint 1 | Implemented |
| CR-013 | Week 2 | Scope | Sprint 2 contract: the resident read path. `frontend-page-map.md`, `user-journey-maps.md`, `frontend-api-dependencies.md`, `openapi.yaml`, `product-backlog.md` | `frontend-page-map.md` and `user-journey-maps.md` still described a larger resident journey than the sprint could support | The sprint had to be kept to a realistic, testable slice and the contract had to match the planned front-end work | The resident read path was narrowed to the agreed sprint slice and reflected in the API and backlog | Medium. Affects the product backlog, front-end and API contract | R6 | Sprint 1 | Implemented |
| CR-014 | Week 2 | Design | Design system palette: `design-system.md` 5.1 and 5.2, `frontend/src/styles/tokens.css`, and the high fidelity pages | The palette was the teal and pale-slate set fixed in Week 1, but the implementation had drifted away from that source | The team needed one stable visual system to avoid local overrides in multiple pages | The design tokens and hi-fi pages were brought back into one palette and the token values were corrected | Medium. Affects front-end styles and the design baseline | R4 | Sprint 1 | Implemented |
| CR-015 | Week 2 | Design | Design baselines delivered in week 2 with no issue of their own: `docs/02-design/location-recommender-rules.md` and `docs/02-design/overdue-fee-rules.md` | Two design baselines were drafted but not yet tied to a specific approval record | The team needed to record the change in the log before the design review, so future work could be traced properly | The design baselines were recorded as approved design inputs and linked to the proper owner and review path | Low. Affects only the design baseline and traceability | R1 | Sprint 1 | Implemented |
| CR-016 | Week 2 | Design | Contract and plan decisions raised by R6 while reviewing `T-20-15`: `openapi.yaml`, `PLAN.md` T-42-02, `ADR-004-location-recommendation.md` and the unique partial constraint | The initial recommendation design had a conflicting constraint: location scoring and rank ordering were both specified, but the rule set could not support both without a further decision | The design had to record the ruling that the partial recommendation is a deterministic rule and the storage location ranking is a review aid rather than a system-driven assignment | The contract and plan were rewritten to one consistent recommendation interpretation | Medium. Affects design, contract and implementation plan | R6 | Sprint 1 | Implemented |
| CR-017 | Week 2 | Scope | The week 2 scope of issue #73 and four stale issue references: `README.md`, `backend/Dockerfile` and `docker-compose.yml` | Issue #73 asked for "four jobs and all are guaranteed to run" but the sprint plan only supported two jobs that matched the design work | The scope had to be corrected to the actual work the team could complete during week 2 without misreporting implementation progress | The issue and references were narrowed to the jobs that existed in the sprint plan and the stale references were fixed | Low. Affects planning documents and project setup notes | R1 | Sprint 1 | Implemented |
| CR-018 | Week 2 | Design | Completing CR-014: the sidebar token group and `--color-surface-alt` in `frontend/src/styles/tokens.css`, `docs/02-design/design-system.md` 5.2, `docs/02-design/high-fidelity-pages/README.md` | The design palette had drifted and the sidebar tokens were not consistent with the actual implementation | A single design token definition was required so the UI style remained coherent across the pages | The sidebar token group and alternative surface token were corrected to the approved design baseline | Medium. Affects front-end CSS and design specification | R4 | Sprint 1 | Implemented |
| CR-019 | Week 2 | Process | `docs/06-logbook/README.md`, the template every personal logbook was copied from | Three logbooks carry a wrong period and two carry a placeholder date, and both come from the template | The template was wrong and the project process requires a clean handoff for the retention and review of personal work logs | The logbook template and the copied files were corrected to the correct period and date format | Low. Affects process documentation and personal work logs | R1 | Sprint 1 | Implemented |
| CR-020 | Week 2 | Process | Three remaining references to issue #75 in `CHANGELOG.md` and `docs/07-report/chapters/00-outline.md` | The Spring Boot skeleton is delivered by `T-30-02`, and the remaining references still pointed at issue #75 | The issue references had drifted and needed to be aligned to the actual work carried out | The references were corrected to the correct issue so the project report and change log stayed consistent | Low. Affects project process records and report references | R1 | Sprint 1 | Implemented |
| CR-021 | Week 3 | Design | `docs/02-design/database/ERD.md`, `docs/02-design/adr/ADR-002-backend-architecture.md` | `ERD.md` held eight entities and none carried a collection point identifier, though the project already needed multi-point data scoping | The design needed a collection point entity to support the planned multi-point model and to match the requirements for owner- and site-scoped data | The ERD and architecture notes were revised to include a `CollectionPoint` record and the required relationships | Medium. Affects the design and the schema plan | R3 | Sprint 2 | Implemented |
| CR-022 | Week 3 | Scope | `docs/01-requirements/specification/SRS.md` `OS-3`; `PLAN.md` `T-44-01`; finding A of `docs/05-management/minutes/20261016_requirements-walkthrough.md` | The walkthrough identified that online payment and settlement were out of scope, but the previous wording still implied they were part of the product | The project had to settle the boundary clearly so the design and billing work stayed aligned with the agreed scope | The scope statement was corrected to keep payment and settlement out of scope, while preserving the overdue fee calculation as a non-payment operation | Medium. Affects the requirements and the design plan | R2 | Sprint 2 | Implemented |
| CR-023 | Week 3 | Requirement | FR-19 and FR-20, `docs/02-design/location-recommender.md`, `ui-prototype.md`, `T-42-07` | `CR-004` kept storage-location recommendation as a should-have optional feature, but later documents had treated it as a must-have | The design needed a single, clear decision: recommendation is optional and ignorable, not an automatic assignment | The requirement and design documents were rewritten to say the recommendation is optional, staff may ignore it, and it is not automatic | Medium. Affects the SRS, prototype and design docs | R2 | Sprint 2 | Implemented |
| CR-024 | Week 3 | Scope | `T-42-04` full-shelf handling and the `shelve later` path | The contract had no explicit continuation when a storage location was unavailable, so the flow depended on an informal assumption rather than a requirement | The team needed to defer the shelve-later path and record the incomplete state formally so the Sprint 2 scope stayed honest | `T-42-04` became the relevant place for full-shelf handling, and the default was documented as deferred until a later change record was approved | Medium. Affects the Sprint 2 scope, the contract and the shelving path | R2 | Sprint 2 | Implemented |
| CR-025 | Week 3 | Requirement | FR-18 batch limit, `openapi.yaml`, `ui-prototype.md`, `T-42-01` | The batch requirement was defined as up to 50 parcels in one operation, but the contract still described a different limit or no clear validation rule | The project needed a single agreed limit and a stable validation rule so the batch intake flow could be tested consistently | The batch cap was formalised at 50 entries per request, with validation and error responses recorded in the contract and the requirement text | Medium. Affects the requirement, API contract and UI prototype | R2 | Sprint 2 | Implemented |
| CR-026 | Week 3 | Design | `docs/02-design/database/ERD.md` (`Setting`) and `docs/02-design/overdue-fee-rules.md` section 8 | `ERD.md` described `Setting.key` as `unique`, which reads as globally unique rather than unique within the collection point | The overdue-fee rules already assumed a collection-point-scoped setting, so the schema needed to match that scope rule | `Setting.key` was corrected to be unique within a collection point, not globally unique, and the design notes were aligned to the same meaning | Medium. Affects the ERD and the overdue-fee rules | R2 | Sprint 2 | Implemented |
| CR-027 | Week 4 | Process | T-30-05 quality tooling and PR #230 | Issue #177 assumes the backend does not exist, but develop now contains the Spring Boot application, Maven wrapper, Java tests and CI entries for the new workflow | The project needed to correct the stale assumption and record the tooling change as a process update rather than a new feature | The quality tooling baseline was updated to the actual repository state, preserving the current CI setup and the reviewed formatting configuration | Medium. Affects the project tooling baseline and the QA process | R2 | Sprint 2 | Implemented |
| CR-028 | Week 4 | Scope | `T-42-01`/`T-42-03` shelving path, and `CR-024`'s record of it | `SRS.md` 3.5 says no defined fallback exists when a storage location is unavailable, and `CR-024`'s `After` column claims "the normal full-shelf failure path" — a path that has never been specified. `#98` has carried the gap since week 3 | A. Use a temporary `PENDING` storage slot when the shelf is full or when a parcel already in storage must be reassigned. This is the least disruptive default: it keeps the shelving flow moving without forcing staff into an unacceptable 409 rejection, and it matches the existing `T-42-03` status model by extending the state set rather than inventing a new operational path. B is already judged unacceptable in operation by the project findings, and C pushes the parcel into an exception flow before the Sprint 3 exception-ticket work exists. | Medium. Affects SRS 3.5, the `T-42-01`/`T-42-03` shelving path, the contract in `openapi.yaml`, and the validation/tests that cover full-shelf handling. | R2 | Sprint 2 | Proposed |

## 3. Requests that were considered and rejected

Recording a rejected request is as useful as recording an accepted one, because
it shows the option was weighed.

| ID | Date | Request | Reason for rejection |
|---|---|---|---|
| CRJ-001 | Week 1 | Adopt a complex scoring algorithm for the storage location recommendation | A deterministic rule is easier to test and explain, and the survey did not support prioritising the algorithmic complexity over clarity |
| CRJ-002 | Week 1 | Add a mobile application | Out of scope. The system is a responsive web application |
| CRJ-003 | Week 1 | Integrate directly with courier platforms | Out of scope. Parcel data is entered manually |

## Revision history

| Version | Date | Change | Author |
|---|---|---|---|
| v1.0 | Week 1 | Initial log with seven change records and three rejected requests | R1 |
| v1.1 | Week 2 | Added CR-008 and CR-009; corrected the CR-008 wording on exception types; added CR-010, CR-011 and CR-012 and recorded CR-013 as Proposed | R1 |
| v1.2 | Week 2 | Added CR-014, the design system palette change | R4 |
| v1.3 | Week 2 | Added CR-015, the recommender and overdue fee design baselines, recorded as Proposed pending two value sign-offs | R1 |
| v1.4 | Week 2 | CR-015 confirmed and moved to Implemented; added CR-016 for the recommender contract ruling, the turnover score conflict and the constraint dependency | R1 |
| v1.5 | Week 2 | Added CR-017, narrowing issue #73 to the two jobs that can run in week 2 and correcting four references that named issue #75 for `T-30-02` | R1 |
| v1.6 | Week 2 | Added CR-018, completing CR-014: the sidebar group becomes the shell's single definition, `--color-surface-alt` is removed and the `hi-fi.css` declarations that shadowed those tokens are corrected | R1 |
| v1.7 | Week 2 | Added CR-019, fixing the logbook template that three logbooks copied a wrong period and two copied a placeholder date from | R1 |
| v1.8 | Week 2 | Added CR-020, pointing the three remaining issue #75 references at issue #137, which CR-017 recorded as four | R1 |
| v1.9 | Week 3 | Added CR-021, the collection point decision that closes the design half of issue #127: Sprint 2 assumes more than one collection point, `CollectionPoint` is added to `ERD.md`, the relationship is recorded and the owner data model is fixed | R1 |
| v1.10 | Week 3 | Added CR-022, the billing scope ruling from the requirements walkthrough: the horizon is eight weeks, payment and settlement stay out of scope, and the overdue fee calculation is kept in-scope without taking payment | R1 |
| v1.11 | Week 3 | Added CR-023, CR-024 and CR-025 for the week 3 adjudication: the recommendation panel is demoted, the full-shelf fallback is deferred, and the 50-parcel batch cap is kept without a new requirement | R1 |
| v1.12 | Week 3 | Added CR-026, making `Setting.key` unique within a collection point rather than globally. The schema already did this and `overdue-fee-rules.md` section 8 already asked for it, so the log records the discrepancy and the correction | R1 |
| v1.13 | Week 4 | Recorded CR-027 for the PR #230 tooling correction: replace the obsolete pre-scaffold assumption with additive integration, preserve current CI, and adopt the reviewed formatting and check scripts | R1 |
| v1.14 | Week 4 | Added section 1.3, which says a record number is taken when it appears in the section 2 table rather than when it is mentioned in an issue. Three collisions came from the other interpretation and this wording fixes the process rule | R1 |
| v1.15 | Week 4 | Added CR-028 to define the default full-shelf fallback as a temporary `PENDING` hold and corrected the CR-024 record wording to align with the agreed shelving-path decision | R2 |
