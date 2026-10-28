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
| 2. Change analysis and cost | The effect on requirements, design, code, tests and the schedule is worked out. The cost is stated in hours and in risk. | R2 for requirement and scope effect, R6 on API and contract impact, R7 on implementation burden |
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
| CR-001 | Week 1 | Design | Backend framework and database, ADR-002 and ADR-005 | The first design baseline specified Node.js with Express 4 and MySQL 8, with Sequelize for migrations | Three reasons: the team already had a Java backend in progress, the course material expects a clear framework choice, and the design baseline needed to match the actual working stack |
| CR-002 | Week 1 | Scope | Elicitation method, plan entry T-10-02 and T-10-03 | The plan included three stakeholder interviews and a competitor analysis of three platforms | With a ten-member team, the workload and schedule did not support that breadth; the team needed a lighter but still traceable elicitation plan | The plan was narrowed to the required stakeholder and requirement inputs that support the Sprint 1 baseline | Medium — planning and elicitation artefacts | R2 | Sprint 1 | Approved |
| CR-003 | Week 1 | Requirement | FR-18 batch registration, backlog priority | Batch registration of up to 50 parcels was listed as a should-have | Staff questionnaire S3: 4 of 7 respondents named batch handling as important to their work, and it was central to Sprint 1 operation | Batch registration becomes a must-have requirement in the backlog and is carried into the Sprint 2 contract scope | High — requirement and backlog traceability | R2 | Sprint 1 | Approved |
| CR-004 | Week 1 | Requirement | FR-19 and FR-20, storage location recommendation | The recommendation feature was listed as a must-have and was described in early material as a headline feature | Evaluation later showed the recommendation was optional and lower priority; the team lacked evidence to treat it as a must-have | The feature is reduced to an optional, non-binding recommendation and is not part of the Sprint 2 core flow | Medium — functional requirements and prototype flow | R2 | Sprint 1 | Approved |
| CR-005 | Week 1 | Requirement | FR-9 and FR-10, delegated collection | Delegated collection was listed in scope as IS-8 | Resident questionnaire R5: 0 of 11 chose delegated collection, so the requirement was not supported by the sample data | The requirement is deferred as future work and removed from the Sprint 1 scope | Medium — SRS and backlog scope | R2 | Sprint 1 | Approved |
| CR-006 | Week 1 | Design | Parcel status enumeration, ERD.md | The status enumeration was `registered, in_storage, ready_for_pickup, out_for_delivery, picked_up, returned, exception` | `out_for_delivery` is a courier concept and not a valid parcel state for the collection-point system; the team needed a consistent lifecycle aligned to this product | The status set is narrowed to the in-system lifecycle used by the project and it is reflected in the data model and prototypes | High — data model and workflow definitions | R3 | Sprint 1 | Approved |
| CR-007 | Week 1 | Design | Pickup confirmation endpoint, openapi.yaml | The `/pickup/confirm` request body required `operatorId` to be supplied by the client | The operator must be taken from the authenticated session so the operation log is reliable and not spoofed by a client | The endpoint uses the session principal as the operator source and the client payload no longer carries it | High — API contract and audit integrity | R6 | Sprint 1 | Approved |
| CR-008 | Week 2 | Requirement | SRS chapters 1 and 2: background, problem statement, scope, functions, user roles, references, assumptions and open issues | Chapters 1 and 2 were written before the full elicitation and baseline review, and some statements needed rewording to match the agreed scope | The review identified mismatches between the draft and the agreed product boundary and the inclusion of out-of-scope items | The SRS chapters are rewritten to reflect the final problem statement, scope and assumptions used by the team | High — requirements baseline quality | R2 | Sprint 1 | Approved |
| CR-009 | Week 2 | Requirement | Persona identifiers, `Personas.md` and SRS section 2.3 | The four personas carried invented personal names (Emily Chen, Daniel Wu, Linda Zhang, Alex Ho) and were not consistent with the ethics-based naming rule | The ethics statement forbids naming real or implied individuals in the project documents, so the persona naming had to be normalized | The personas use role labels rather than personal names, and the same names are used throughout the documents | Medium — requirements and ethics consistency | R2 | Sprint 1 | Approved |
| CR-010 | Week 2 | Design | Exception type enumeration: `ERD.md`, `class-parcel-and-location.puml` and `ui-prototype.md` | The exception types were not defined consistently. `ERD.md` and the class diagram used different labels and the UI prototype did not align | The project needs a single, traceable exception vocabulary for the parcel lifecycle and the status model | A common set of exception types is used across the domain model, diagrams and prototype | High — domain model consistency | R3 | Sprint 1 | Approved |
| CR-011 | Week 2 | Design | Exception type list in `prototype/README.md`, `PLAN.md` and `glossary.md` | The exception types were written down in seven places with four different wordings. `prototype/README.md` and the plan treated them as free text | The team needed a canonical set to keep validation and testing coherent | The exception names are normalized, duplicated terms are retired, and the glossary is the single source for the vocabulary | High — cross-document consistency | R3 | Sprint 1 | Approved |
| CR-012 | Week 2 | Process | `docs/glossary.md` as the single source for domain vocabulary; the retired-term rule | Domain terms were repeated in full in the SRS, the ERD, the class diagram, the prototype and elsewhere; there was no single approved source | A single source of truth makes requirement traceability and acceptance testing far easier, and it prevents variant spellings from reappearing | The glossary becomes the authoritative vocabulary, with retired terms recorded only as historical notes | Medium — documentation governance | R2 | Sprint 1 | Approved |
| CR-013 | Week 2 | Scope | Sprint 2 contract: the resident read path. `frontend-page-map.md`, `user-journey-maps.md`, `frontend-api-dependencies.md`, `openapi.yaml`, `product-backlog.md` | Frontend and backend planning had drifted because the resident read path had not yet been concretely tied to the API contract | The team needed a single agreed scope boundary before design and implementation could start in Sprint 2 | The resident read path is narrowed and explicitly tied to the API and page map used by the sprint plan | High — contract and implementation alignment | R6 | Sprint 2 | Approved |
| CR-014 | Week 2 | Design | Design system palette: `design-system.md` 5.1 and 5.2, `frontend/src/styles/tokens.css`, and the high fidelity pages | The palette was the teal and pale-slate set fixed from the first prototype, but the actual implementation drifted from the agreed design tokens | The UI needed a stable source of truth so all pages and tokens aligned with one color system | The palette is standardized around the approved token set and applied consistently across pages | Medium — frontend implementation consistency | R4 | Sprint 1 | Approved |
| CR-015 | Week 2 | Design | Design baselines delivered in week 2 with no issue of their own: `docs/02-design/location-recommender-rules.md` and `docs/02-design/overdue-fee-rules.md` | Two design baselines were delivered without being formally recorded, so the team lacked a traceable reference for later review | The baseline documents needed to be linked to their owning approval record before implementation depended on them | The design files are recorded as approved baselines and used as the basis for future change records | Medium — design traceability | R1 | Sprint 1 | Approved |
| CR-016 | Week 2 | Design | Contract and plan decisions raised by R6 while reviewing `T-20-15`: `openapi.yaml`, `PLAN.md` T-42-02, `ADR-004-location-recommendation.md` and the unique partial constraint | The contract and plan had a hidden assumption about how the recommender should behave under the unique partial constraint | The analysis needed to make the dependency explicit so implementation and testing would not diverge | The contract record states the explicit constraint and the plan is kept aligned to it | High — contract and scheduling alignment | R6 | Sprint 2 | Approved |
| CR-017 | Week 2 | Scope | The week 2 scope of issue #73 and four stale issue references: `README.md`, `backend/Dockerfile` and `docker-compose.yml` | Issue #73 asked for "four jobs and all are going to run in week 2" but the repository had already moved past that baseline | The team needed a precise statement of which jobs were in scope for week 2 so the issue references would no longer mislead contributors | The scope is narrowed to the two jobs that can actually run in the sprint and the stale issue references are corrected | Medium — project coordination | R1 | Sprint 1 | Approved |
| CR-018 | Week 2 | Design | Completing CR-014: the sidebar token group and `--color-surface-alt` in `frontend/src/styles/tokens.css`, `docs/02-design/design-system.md` 5.2, `docs/02-design/high-fidelity-prototype.md` | The token set had duplicated values and the sidebar styling override was drifting from the agreed design system | The design had to be corrected to a single token source and consistent shell styling before implementation continued | The sidebar group becomes the shell's single definition and the duplicate shadow declarations are removed | Medium — frontend styling consistency | R4 | Sprint 1 | Approved |
| CR-019 | Week 2 | Process | `docs/06-logbook/README.md`, the template every personal logbook was copied from | Three logbooks carry a wrong period and two carry a placeholder date, and both come from the same template | The team needed a consistent reporting period and a clean template before personal records were reviewed | The template is corrected so every logbook carries the correct period and no placeholder date remains | Low — process hygiene | R1 | Sprint 1 | Approved |
| CR-020 | Week 2 | Process | Three remaining references to issue #75 in `CHANGELOG.md` and `docs/07-report/chapters/00-outline.md` | The Spring Boot skeleton is delivered by `T-30-02`, and the remaining references still pointed to the old issue number | Stale references are confusing and can mislead later tracing work | The issue references are corrected to the active issue and the project record is internally consistent | Low — documentation integrity | R1 | Sprint 1 | Approved |
| CR-021 | Week 3 | Design | `docs/02-design/database/ERD.md`, `docs/02-design/adr/ADR-002-backend-architecture.md` | `ERD.md` held eight entities and none carried a collection point identifier, so the design did not support multi-point data separation | The project design needed to reflect the agreed multi-collection-point model before the API and schema work matured | `CollectionPoint` is added to the schema, all scoped records are tied to it, and the design baseline is updated | High — data model and multi-tenant integrity | R3 | Sprint 2 | Approved |
| CR-022 | Week 3 | Scope | `docs/01-requirements/specification/SRS.md` `OS-3`; `PLAN.md` `T-44-01`; finding A of `docs/05-management/minutes/20261016_requirements-walkthrough.md` | The walkthrough clarified the billing boundary, but the SRS and the plan were still using the earlier assumption that payment should appear in scope | The project scope had to be made explicit to keep billing and fee logic separate from the platform itself | Payment and settlement remain out of scope while the overdue fee calculation is preserved within the product boundary | Medium — scope alignment | R2 | Sprint 2 | Approved |
| CR-023 | Week 3 | Requirement | FR-19 and FR-20, `docs/02-design/location-recommender.md`, `ui-prototype.md`, `T-42-07` | `CR-004` kept storage-location recommendation as a should-have optional feature, but the implementation documents still described it as a committed part of the flow | The design and contract still implied a stronger commitment than the team had approved | The recommendation panel is demoted to optional, non-binding, and explicitly ignored by the Sprint 2 core workflow | Medium — requirement and UX scope | R2 | Sprint 2 | Approved |
| CR-024 | Week 3 | Scope | `T-42-01`/`T-42-03` shelving path and the `shelve later` fallback | The contract had no explicit continuation when a storage location was unavailable, so the flow depended on an unspoken assumption that was never specified | The work needed a defined, reviewable rule for Sprint 2 rather than a hidden gap that would be interpreted differently by different teammates | The full-shelf path now defaults to a `PENDING` storage location when no suitable shelf is available, and the workflow is recorded as a defined fallback instead of a missing path | High — affects `SRS.md` 3.5, `T-42-01`/`T-42-03`, the shelving contract and the Sprint 2 acceptance tests | R2 | Sprint 2 | Approved |
| CR-025 | Week 3 | Requirement | FR-18 batch limit, `openapi.yaml`, `ui-prototype.md`, `T-42-01` | The batch requirement was defined as up to 50 parcels in one operation, but the contract still described the request as unlimited in some places | The contract and requirement text had to line up or the UI and API would disagree on the valid request size | The batch limit remains 50 and the API and prototype are updated to reject larger batches without partial registration | High — API contract, UI behavior and acceptance criteria | R2 | Sprint 2 | Approved |
| CR-026 | Week 3 | Design | `docs/02-design/database/ERD.md` (`Setting`) and `docs/02-design/overdue-fee-rules.md` section 8 | `ERD.md` described `Setting.key` as `unique`, which reads as unique across the whole platform rather than within a collection point | The design needed to match the actual scope and the fee rules, which are collection-point scoped | `Setting.key` is constrained to be unique within a collection point, not globally, and the design is consistent with the project model | Medium — schema and config integrity | R3 | Sprint 2 | Approved |
| CR-027 | Week 4 | Process | T-30-05 quality tooling and PR #230 | Issue #177 assumes the backend does not exist, but develop now contains the Spring Boot application, Maven wrapper, Java tests and the CI scaffolding | The team needed to replace the stale assumption with the actual delivery path so tooling and quality checks would not be built around a non-existent backend | The process record now reflects the additive integration path: keep the current CI, preserve the working project, and add the reviewed tooling as required | Medium — build and CI process integrity | R1 | Sprint 2 | Approved |
| CR-028 | Week 4 | Scope | `T-42-01`/`T-42-03` shelving path, and `CR-024`'s record of it | `SRS.md` 3.5 says no defined fallback exists when a storage location is unavailable, and `CR-024`'s `After` column claims "the normal full-shelf failure path" — a path that has never been specified. `#98` has carried the gap since week 3 | The system needs a defined fallback for Sprint 2 rather than an undefined gap. The accepted behavior is to place the parcel in a `PENDING` storage location when no suitable shelf is available; this keeps the workflow usable, preserves traceability, and avoids the unacceptable operational denial already identified in `#170` finding 13 | High — affects `SRS.md` 3.5, `T-42-01`/`T-42-03`, the shelving contract and the Sprint 2 back-end and UI test expectations | R2 | Sprint 2 | Proposed |

## 3. Requests that were considered and rejected

Recording a rejected request is as useful as recording an accepted one, because
it shows the option was weighed.

| ID | Date | Request | Reason for rejection |
|---|---|---|---|
| CRJ-001 | Week 1 | Adopt a complex scoring algorithm for the storage location recommendation | A deterministic rule is easier to test and explain, and the survey did not support prioritising the complexity |
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
| v1.9 | Week 3 | Added CR-021, the collection point decision that closes the design half of issue #127: Sprint 2 assumes more than one collection point, `CollectionPoint` is added to `ERD.md`, the model and the database are kept aligned, and the design baseline is updated | R1 |
| v1.10 | Week 3 | Added CR-022, the billing scope ruling from the requirements walkthrough: the horizon is eight weeks, payment and settlement stay out of scope, and the overdue fee calculation remains in scope | R1 |
| v1.11 | Week 3 | Added CR-023, CR-024 and CR-025 for the week 3 adjudication: the recommendation panel is demoted, the full-shelf fallback is deferred, and the 50-parcel batch cap is kept without changing the requirement title | R1 |
| v1.12 | Week 3 | Added CR-026, making `Setting.key` unique within a collection point rather than globally. The schema already did this and `overdue-fee-rules.md` section 8 already asked for it, so the design and the requirement are aligned | R1 |
| v1.13 | Week 4 | Recorded CR-027 for the PR #230 tooling correction: replace the obsolete pre-scaffold assumption with additive integration, preserve current CI, and adopt the reviewed formatting and lint rules | R1 |
| v1.14 | Week 4 | Added section 1.3, which says a record number is taken when it appears in the section 2 table rather than when it is mentioned in an issue. Three collisions came from the other interpretation and are described there | R1 |
| v1.15 | Week 4 | Added CR-028 to define the Sprint 2 full-shelf fallback as a `PENDING` storage location, and corrected the CR-024 wording so its `Item affected` and `After` text no longer claim a non-existent failure path | R2 |
