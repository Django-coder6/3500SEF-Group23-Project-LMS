# Change Log

## COMP 3500SEF Group 23

### Community Parcel Collection Point Management System

| Item | Detail |
| |---|---|
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
| 2. Change analysis and cost | The effect on requirements, design, code, tests and the schedule is worked out. The cost is stated in hours and in risk. | R2 for requirement and scope effect, R6 or R4 for technical effect, R1 for schedule effect |
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
| |---|---|---|---|---|---|---|---|---|---|
| CR-001 | Week 1 | Design | Backend framework and database, ADR-002 and ADR-005 | The first design baseline specified Node.js with Express 4 and MySQL 8, with Sequelize for migrations | Three reasons: the team had already standardised on Java and Spring Boot, the course workload had to be kept realistic, and the original baseline was not aligned with the implemented stack | The project uses Java 17 with Spring Boot and PostgreSQL, and the framework choices are recorded in the approved ADR set | High | R2, with R1 on the schedule effect | Week 1 | Implemented |
| CR-002 | Week 1 | Scope | Elicitation method, plan entry T-10-02 and T-10-03 | The plan included three stakeholder interviews and a competitor analysis of three platforms | The workshop plan under-represented the real stakeholder mix and missed the team’s agreed review method | The elicitation approach is re-scoped to the agreed workshop and stakeholder set | High | R5, R4 and R2 | Week 1 | Closed |
| CR-003 | Week 1 | Requirement | FR-18 batch registration, backlog priority | Batch registration of up to 50 parcels was listed as a should-have | Staff questionnaire S3: 4 of 7 respondents named batch registration as a practical need, while the design and backlog still described it as optional | Batch registration becomes a required feature in the backlog and SRS | High | R2 | Week 1 | Implemented |
| CR-004 | Week 1 | Requirement | FR-19 and FR-20, storage location recommendation | The recommendation feature was listed as a must-have and was described in early material as a headline feature | The feature was still not bounded by a clear rule set, so the team needed a design decision and a matching justification | Recommendation remains a must-have but is constrained to the approved deterministic rule set | High | R2, with R4 on the design rule | Week 1 | Implemented |
| CR-005 | Week 1 | Requirement | FR-9 and FR-10, delegated collection | Delegated collection was listed in scope as IS-8 | Resident questionnaire R5: 0 of 11 chose delegated collection, so the feature was dropped from the working requirement set | Delegated collection is removed from scope and no longer carried as a requirement | Medium | R2 | Week 1 | Closed |
| CR-006 | Week 1 | Design | Parcel status enumeration, ERD.md | The status enumeration was `registered, in_storage, ready_for_pickup, out_for_delivery, picked_up, returned, exception` | The draft status set was not consistent with the actual parcel flow and did not align with the return and exception states used elsewhere | The final enumeration is restricted to the agreed set used in the ERD and API contract | High | R4 | Week 1 | Implemented |
| CR-007 | Week 1 | Design | Pickup confirmation endpoint, openapi.yaml | The `/pickup/confirm` request body required `operatorId` to be supplied by the client | The operator must be taken from the authenticated session, not from the client payload | The endpoint only receives the parcel ID and uses the authenticated operator identity | High | R4 | Week 1 | Implemented |
| CR-008 | Week 2 | Requirement | SRS chapters 1 and 2: background, problem statement, scope, functions, user roles, references, assumptions and open issues | Chapters 1 and 2 were written before the team agreed on the neutral persona set, the final scope and the exact terminology used in the design | The baseline material had to be repaired to match the final course brief and to remove unsupported wording | The SRS chapters are rewritten to match the final scope and neutral terminology | High | R2 | Week 2 | Implemented |
| CR-009 | Week 2 | Requirement | Persona identifiers, `Personas.md` and SRS section 2.3 | The four personas carried invented personal names and were not aligned with the agreed ethically neutral naming scheme | The personas had to be anonymised and aligned with the group’s process and design language | The persona list uses neutral identifiers and the SRS references the agreed wording | Medium | R2 | Week 2 | Implemented |
| CR-010 | Week 2 | Design | Exception type enumeration: `ERD.md`, `class-parcel-and-location.puml` and `ui-prototype.md` | The exception types were not defined consistently. `ERD.md` and the class diagram used a different set from the prototype and glossary | The design needed one canonical set of parcel and storage exceptions so the backend contract and UI matched | A single exception set is adopted and propagated through the design artefacts | High | R4 | Week 2 | Implemented |
| CR-011 | Week 2 | Design | Exception type list in `prototype/README.md`, `PLAN.md` and `glossary.md` | The exception types were written down in seven places with four different wordings. The prototype and plan had drifted from the ERD and the contract | One authoritative wording was required to avoid contradictory UI and API descriptions | The glossary and prototype adopt the canonical exception language used in the ERD | High | R4 | Week 2 | Implemented |
| CR-012 | Week 2 | Process | `docs/glossary.md` as the single source for domain vocabulary; the retired-term rule | Domain terms were repeated in full in the SRS, the ERD, the class diagram, the prototype and the plan, with old names still surviving in some places | The documentation needed a single authoritative vocabulary and a rule for retiring obsolete labels | The glossary becomes the canonical source and the retired-term rule is enforced | Medium | R2 | Week 2 | Implemented |
| CR-013 | Week 2 | Scope | Sprint 2 contract: the resident read path. `frontend-page-map.md`, `user-journey-maps.md`, `frontend-api-dependencies.md`, `openapi.yaml`, `product-backlog.md` | Frontend and API work had drifted into an over-wide scope that pulled in the resident read path and adjacent tasks at once | The team needed a narrow, testable Sprint 2 contract that matched the intended backlog and the current schedule | Sprint 2 is explicitly restricted to the resident read path and the agreed downstream dependencies | High | R2 | Week 2 | Proposed |
| CR-014 | Week 2 | Design | Design system palette: `design-system.md` 5.1 and 5.2, `frontend/src/styles/tokens.css`, and the high fidelity pages | The palette was the teal and pale-slate set fixed in week 1 and the high fidelity pages repeated it too heavily | The design needed one stable token set so UI pages were coherent and class names did not drift | The palette is standardised through the design-system tokens and high-fidelity pages | High | R4 | Week 2 | Implemented |
| CR-015 | Week 2 | Design | Design baselines delivered in week 2 with no issue of their own: `docs/02-design/location-recommender-rules.md` and `docs/02-design/overdue-fee-rules.md` | Two design baselines were approved but not yet referenced in the records, so the rationale behind them was not visible in the audit trail | The design notes needed to be recorded as an explicit baseline once the week 2 review had confirmed them | The baselines are recorded as approved design decisions under the standard change-log process | High | R4 | Week 2 | Implemented |
| CR-016 | Week 2 | Design | Contract and plan decisions raised by R6 while reviewing `T-20-15`: `openapi.yaml`, `PLAN.md` T-42-02, `ADR-004-location-recommendation.md` and the unique partial constraints | The recommender and the overdue-fee logic had several hidden assumptions that did not match the contract and plan | The design needed to document the rule conflicts and the dependency that tied the two figures together | The contract and plan are revised to reflect the resolved dependency and the reviewed rule set | High | R6 | Week 2 | Implemented |
| CR-017 | Week 2 | Scope | The week 2 scope of issue #73 and four stale issue references: `README.md`, `backend/Dockerfile` and `docker-compose.yml` | Issue #73 asked for "four jobs and all are green" and the repo still carried four stale references to the old issue | Four stale references were blocking a clean status check and the issue had to be narrowed to the actual runnable jobs | The issue is narrowed to the two jobs that actually run in week 2, and the stale references are corrected | Medium | R2 | Week 2 | Closed |
| CR-018 | Week 2 | Design | Completing CR-014: the sidebar token group and `--color-surface-alt` in `frontend/src/styles/tokens.css`, `docs/02-design/design-system.md` 5.2, `docs/02-design/high-fidelity.css` and `docs/02-design/README.md` | The design system had duplicate token definitions and the high-fidelity CSS still overrode the intended shell tokens | The UI needed a single source for the sidebar styling so the tokens and the rendered pages agreed | The sidebar token group becomes the single definition and the overriding declarations are removed | High | R4 | Week 2 | Implemented |
| CR-019 | Week 2 | Process | `docs/06-logbook/README.md`, the template every personal logbook was copied from | Three logbooks carry a wrong period and two carry a placeholder date, and both come from the same template | The template had to be repaired to keep the logbook set consistent and credible | The logbook template is corrected and the existing copies are brought back into line | Medium | R1 | Week 2 | Implemented |
| CR-020 | Week 2 | Process | Three remaining references to issue #75 in `CHANGELOG.md` and `docs/07-report/chapters/00-outline.md` | The Spring Boot skeleton is delivered by `T-30-02`, and the remaining stale references still pointed to the old issue number | The report and change log needed to point to the current issue so the traceability chain remained credible | The stale references are updated to the correct issue number and the record is closed | Medium | R2, R5 and R6 | Week 2 | Implemented |
| CR-021 | Week 3 | Design | `docs/02-design/database/ERD.md`, `docs/02-design/adr/ADR-002-backend-architecture.md` | `ERD.md` held eight entities and none carried a collection point identifier, the design baseline did not yet state the collection-point assumption | The model needed a collection-point identifier to reflect the delivery and storage model described in the SRS and UI journey | `CollectionPoint` is added to the ERD and the contract is aligned with Sprint 2 assumptions | High | R4 | Week 3 | Implemented |
| CR-022 | Week 3 | Scope | `docs/01-requirements/specification/SRS.md` `OS-3`; `PLAN.md` `T-44-01`; finding A of `docs/05-management/minutes/20261016_requirements-walkthrough.md` | The requirements walkthrough ruled that the project horizon is eight weeks and billing is out of scope, but the SRS and backlog did not yet match that ruling | The contract needed to reflect the approved scope decision so requirements and implementation were aligned | The SRS and planning material are updated to the eight-week horizon and the out-of-scope billing rule | High | R2 | Week 3 | Implemented |
| CR-023 | Week 3 | Requirement | FR-19 and FR-20, `docs/02-design/location-recommender.md`, `ui-prototype.md`, `T-42-07` | `CR-004` kept storage-location recommendation as a should-have optional feature, but the week 3 decision demoted it to a lower-priority appendix | The product owners needed the recommendation panel to remain present but not to dominate the scope | Recommendation stays present but is demoted to a secondary feature below the shelving and storage flow | Medium | R2 | Week 3 | Implemented |
| CR-024 | Week 3 | Scope | `T-42-01`/`T-42-03` shelving path and the `shelve later` fallback | The contract had no explicit continuation when a storage location was unavailable, so the flow depended on an undefined fallback path | The team resolved the gap by defining the default fallback as a temporary `PENDING` location until a real slot is assigned | The default fallback is a temporary `PENDING` storage location, after which the parcel is re-assigned to a real shelf | High | R2 | Week 3 | Approved |
| CR-025 | Week 3 | Requirement | FR-18 batch limit, `openapi.yaml`, `ui-prototype.md`, `T-42-01` | The batch requirement was defined as up to 50 parcels in one operation, but the contract still described the task as a single-action manual flow | The week 3 ruling needed to lock the batch cap and the operating assumption to the approved design contract | The batch cap remains 50 parcels and the definition is preserved through the contract and UI flow | High | R2 | Week 3 | Implemented |
| CR-026 | Week 3 | Design | `docs/02-design/database/ERD.md` (`Setting`) and `docs/02-design/overdue-fee-rules.md` section 8 | `ERD.md` described `Setting.key` as `unique`, which reads as unique across the whole system rather than within a collection point | The requirement and rule text already implied a per-collection-point scope, so the schema definition had to match that scope | `Setting.key` is defined as unique within a collection point rather than globally | High | R4 | Week 3 | Implemented |
| CR-027 | Week 4 | Process | T-30-05 quality tooling and PR #230 | Issue #177 assumes the backend does not exist, but develop now contains the Spring Boot application, Maven wrapper, Java tests and CI configuration | The tooling and review process had to be corrected to match the actual repository state instead of the stale assumption | The quality tooling and PR checks remain additive and the backend is treated as an existing delivery target | Medium | R2 | Week 4 | Closed |
| CR-028 | Week 4 | Scope | `T-42-01`/`T-42-03` shelving path, and `CR-024`'s record of it | `SRS.md` 3.5 still says no defined fallback exists and will be revised under `T-10-06` by R3, using this record as the wording. | The prior issue was that the requirement text claimed there was no fallback, yet the actual design and workflow needed a defined default. This record captures the corrected requirement by pointing to the task that will revise SRS wording. | `FR-14` and `FR-15` remain unchanged. `SRS.md` 3.5 still says no defined fallback exists and will be revised under `T-10-06` by R3, using this record as the wording. | High | R2 | Week 4 | Proposed |

## 3. Requests that were considered and rejected

Recording a rejected request is as useful as recording an accepted one, because
it shows the option was weighed.

| ID | Date | Request | Reason for rejection |
|---|---|---|---|
| CRJ-001 | Week 1 | Adopt a complex scoring algorithm for the storage location recommendation | A deterministic rule is easier to test and explain, and the survey did not support prioritising the recommendation feature above the time-critical design work |
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
| v1.6 | Week 2 | Added CR-018, completing CR-014: the sidebar group becomes the shell's single definition, `--color-surface-alt` is removed and the `hi-fi.css` declarations that shadowed those token settings are removed | R1 |
| v1.7 | Week 2 | Added CR-019, fixing the logbook template that three logbooks copied a wrong period and two copied a placeholder date from | R1 |
| v1.8 | Week 2 | Added CR-020, pointing the three remaining issue #75 references at issue #137, which CR-017 recorded as four | R1 |
| v1.9 | Week 3 | Added CR-021, the collection point decision that closes the design half of issue #127: Sprint 2 assumes more than one collection point, `CollectionPoint` is added to `ERD.md`, the contract and UI are aligned, and the record is closed | R1 |
| v1.10 | Week 3 | Added CR-022, the billing scope ruling from the requirements walkthrough: the horizon is eight weeks, payment and settlement stay out of scope, and the overdue fee calculation remains in scope | R1 |
| v1.11 | Week 3 | Added CR-023, CR-024 and CR-025 for the week 3 adjudication: the recommendation panel is demoted, the full-shelf fallback is deferred, and the 50-parcel batch cap is kept without extension | R1 |
| v1.12 | Week 3 | Added CR-026, making `Setting.key` unique within a collection point rather than globally. The schema already did this and `overdue-fee-rules.md` section 8 already asked for it, so the record confirms rather than changes the contract | R1 |
| v1.13 | Week 4 | Recorded CR-027 for the PR #230 tooling correction: replace the obsolete pre-scaffold assumption with additive integration, preserve current CI, and adopt the reviewed formatting rules | R1 |
| v1.14 | Week 4 | Added section 1.3, which says a record number is taken when it appears in the section 2 table rather than when it is mentioned in an issue. Three collisions came from the other interpretation and were resolved by preserving the existing record number | R1 |
| v1.15 | Week 4 | Added CR-028 to define the Sprint 2 full-shelf fallback as a temporary `PENDING` storage location, and corrected the CR-024 wording so its `Item affected` and `After` text reflect the actual shelving path and fallback | R1 |
