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
| 2. Change analysis and cost | The effect on requirements, design, code, tests and the schedule is worked out. The cost is stated in hours and in risk. | R2 for requirement and scope effect, R6 on API contract changes, R3 on content changes and R7 on any storage-path implementation cost |
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
| CR-001 | Week 1 | Design | Backend framework and database, ADR-002 and ADR-005 | The first design baseline specified Node.js with Express 4 and MySQL 8, with Sequelize for migrations | Three reasons: the project had already agreed on a Java/Spring stack, the course guidance for the backend was the Java ecosystem, and the project’s later commit history and CI configuration already assumed Java. | The design baseline is Java with Spring Boot, PostgreSQL and Flyway. The earlier framework and database choices are retired and no longer used in the repository baseline. | Medium. The backend stack, database choice and implementation plan all change. | R1 | Sprint 1 | Implemented |
| CR-002 | Week 1 | Scope | Elicitation method, plan entry T-10-02 and T-10-03 | The plan included three stakeholder interviews and a competitor analysis of three platforms | The workshop plan under-specified the actual fieldwork and did not match the team’s real workload. The scope decision was needed before the project could be staffed realistically. | The project keeps its stakeholder interviews and adds the targeted elicitation work already executed in week 1. The competitor analysis is dropped and not carried forward. | Low. Only the plan entry and the project scope record change. | R2 | Sprint 1 | Implemented |
| CR-003 | Week 1 | Requirement | FR-18 batch registration, backlog priority | Batch registration of up to 50 parcels was listed as a should-have | Staff questionnaire S3: 4 of 7 respondents named batch registration as the biggest operational pain point, so the requirement must be moved earlier and treated as a must-have. | Batch intake is a must-have and remains in Sprint 2. The requirement still uses the 50-parcel limit and the per-entry validation rules already confirmed in the backlog. | Medium. The backlog priority, the implementation task and the testing plan all move with the requirement. | R2 | Sprint 1 | Implemented |
| CR-004 | Week 1 | Requirement | FR-19 and FR-20, storage location recommendation | The recommendation feature was listed as a must-have and was described in early material as a headline feature | The survey and design review did not support treating recommendation as a Sprint 2 must-have. The evidence showed that manual control was preferred and that a recommendation panel could add latency. | FR-19 and FR-20 are reduced to a future optional capability, and the recommendation panel is deferred. The implementation is not part of Sprint 2. | Medium. The requirement row, prototype content and design decisions all change. | R2 | Sprint 1 | Implemented |
| CR-005 | Week 1 | Requirement | FR-9 and FR-10, delegated collection | Delegated collection was listed in scope as IS-8 | Resident questionnaire R5: 0 of 11 chose delegated collection, so the feature has no evidence behind it. It is not a valid Sprint 2 requirement. | `FR-9` and `FR-10` remain as future-work requirements only, and the other resident workflow stays in scope. | Low. The resident requirements row and future-work notes are corrected. | R2 | Sprint 1 | Implemented |
| CR-006 | Week 1 | Design | Parcel status enumeration, ERD.md | The status enumeration was `registered, in_storage, ready_for_pickup, out_for_delivery, picked_up, returned, exception` | The draft statuses were inconsistent with the actual lifecycle and with the later contract, and several values were too close to the operation-not-state distinction. | The parcel lifecycle is reduced to `registered`, `shelved` and `collected`, with exceptions handled as a separate flow. The design and the backend state model now match the same vocabulary. | Medium. The ERD, state diagrams and API wording all change. | R2 | Sprint 1 | Implemented |
| CR-007 | Week 1 | Design | Pickup confirmation endpoint, openapi.yaml | The `/pickup/confirm` request body required `operatorId` to be supplied by the client | The operator must be taken from the authenticated session, not the request body, so the audit trail remains reliable and secure. | The handover operator is read from the authenticated session and stored with the handover record. The API contract and the audit requirement are aligned. | Medium. The API contract and audit behaviour change, as do the relevant test cases. | R2 | Sprint 1 | Implemented |
| CR-008 | Week 2 | Requirement | SRS chapters 1 and 2: background, problem statement, scope, functions, user roles, references, assumptions and open issues | Chapters 1 and 2 were written before the team settled on the terminology and final scope. They did not yet match the final product definition. | The baseline specification needed to be aligned to the elicitation evidence and the final project scope before Sprint 1 could close. | Chapters 1 and 2 are corrected to match the final role model, the system boundary and the agreed constraints. | Medium. The requirements baseline and the document review are updated. | R2 | Sprint 1 | Implemented |
| CR-009 | Week 2 | Requirement | Persona identifiers, `Personas.md` and SRS section 2.3 | The four personas carried invented personal names and were not aligned with the agreed ethically neutral format. | The course requirements prohibit naming respondents and the project set a rule to use role-based identifiers only. | The persona names are removed and the SRS consistently uses the four role identifiers instead. | Low. The persona descriptions and the SRS role section are corrected. | R2 | Sprint 1 | Implemented |
| CR-010 | Week 2 | Design | Exception type enumeration: `ERD.md`, `class-parcel-and-location.puml` and `ui-prototype.md` | The exception types were not defined consistently. `ERD.md` and the class diagram used different terms and the prototype also differed. | The exception model had to be fixed before it could become a stable contract and before the SRS could state the accepted set. | The approved exception types are fixed to `overdue`, `damaged`, `wrong_recipient`, `refused` and `lost`. | Medium. Several design files and the API vocabulary change at once. | R2 | Sprint 1 | Implemented |
| CR-011 | Week 2 | Design | Exception type list in `prototype/README.md`, `PLAN.md` and `glossary.md` | The exception types were written down in seven places with four different wordings. The repository had drifted away from one source of truth. | The domain vocabulary had to be unified before design and implementation could proceed without conflicting names. | The glossary becomes the authoritative list and the other documents are edited to match it. | Low. Documentation vocabulary is made consistent. | R2 | Sprint 1 | Implemented |
| CR-012 | Week 2 | Process | `docs/glossary.md` as the single source for domain vocabulary; the retired-term rule | Domain terms were repeated in full in the SRS, the ERD, the class diagram, the prototype and the plan, and they were not always consistent. | A single glossary reduces drift and keeps the implementation and the requirements aligned. | `glossary.md` is the canonical list and older names are marked retired if they must not be used. | Low. Process and documentation discipline improve. | R2 | Sprint 1 | Implemented |
| CR-013 | Week 2 | Scope | Sprint 2 contract: the resident read path. `frontend-page-map.md`, `user-journey-maps.md`, `frontend-api-dependencies.md`, `openapi.yaml`, `product-backlog.md` | Frontend stories for the resident read path were either missing or inconsistent with the actual API contract. | The resident read path is a dependency for the public-facing release and needed a scope ruling before implementation. | Sprint 2 includes the resident read path as a defined contract area with the associated API and page map. | Medium. Contract and UI scope are fixed together. | R2 | Sprint 2 | Implemented |
| CR-014 | Week 2 | Design | Design system palette: `design-system.md` 5.1 and 5.2, `frontend/src/styles/tokens.css`, and the high fidelity pages | The palette was fixed to a teal and pale-slate set even though the prototype already needed a clearer, more neutral interface. | The design system needed a controlled correction before the UI could be built and reviewed consistently. | The design system is updated to the revised palette and the affected UI pages are brought into alignment. | Medium. Styling and the high-fidelity screens change together. | R4 | Sprint 2 | Implemented |
| CR-015 | Week 2 | Design | Design baselines delivered in week 2 with no issue of their own: `docs/02-design/location-recommender-rules.md` and `docs/02-design/overdue-fee-rules.md` | Two design baselines were delivered without a matching entry in the log, so they were not traceable to a change record. | The change log must record evidence and design decisions so that implementation and testing remain linked to their source. | The design baseline files are recorded and traceably linked to the relevant delivery. | Low. Documentation traceability is improved. | R1 | Sprint 2 | Implemented |
| CR-016 | Week 2 | Design | Contract and plan decisions raised by R6 while reviewing `T-20-15`: `openapi.yaml`, `PLAN.md` T-42-02, `ADR-004-location-recommendation.md` and the unique partial constraint set | The earlier design materials did not yet lock down the recommendation API assumptions or the partial-consistency constraints. | The recommendation feature and its contract assumptions needed a stable decision before implementation. | The contract and plan record the actual recommendation constraints; uncertain or contradictory assumptions are removed. | Medium. API contracts, the design note and the plan change in parallel. | R6 | Sprint 2 | Implemented |
| CR-017 | Week 2 | Scope | The week 2 scope of issue #73 and four stale issue references: `README.md`, `backend/Dockerfile` and `docker-compose.yml` | Issue #73 asked for "four jobs and all are given to the team", but the actual sprint work had to be narrowed and several issue references were stale. | The sprint scope had to be fixed to the actual work that could be delivered in week 2, and the stale links had to be corrected so readers reach the right issue. | The week 2 scope is narrowed to the tasks that are actually feasible, and the references are corrected to the right live issue numbers. | Medium. Process, documentation and issue linking all change. | R1 | Sprint 1 | Implemented |
| CR-018 | Week 2 | Design | Completing CR-014: the sidebar token group and `--color-surface-alt` in `frontend/src/styles/tokens.css`, `docs/02-design/design-system.md` 5.2, `docs/02-design/high-fidelity.md` and the related pages | The design system palette correction was incomplete in the high-fidelity screens and the sidebar token group still used the replaced values. | Follow-through on CR-014 was required so the design and implementation remained consistent. | The remaining token references are corrected and the design system is uniform across the app. | Medium. Styling and documentation remain aligned. | R4 | Sprint 2 | Implemented |
| CR-019 | Week 2 | Process | `docs/06-logbook/README.md`, the template every personal logbook was copied from | Three logbooks carry a wrong period and two carry a placeholder date, and both come from the template itself. | The project needs a consistent logbook period and a valid date for every entry. | The template is corrected and the copied logbooks are updated to match it. | Low. Documentation hygiene and traceability improve. | R1 | Sprint 2 | Implemented |
| CR-020 | Week 2 | Process | Three remaining references to issue #75 in `CHANGELOG.md` and `docs/07-report/chapters/00-outline.md` | The Spring Boot skeleton is delivered by `T-30-02`, and the four references that named it pointed at issue #75, the closed design-baseline issue. | A repo-wide search showed several references were still stale and readers would be sent to the wrong issue. | All live references are corrected to the actual implementation issue and the report outline is consistent. | Medium. Documentation and traceability are fixed. | R1 | Sprint 2 | Implemented |
| CR-021 | Week 3 | Design | `docs/02-design/database/ERD.md`, `docs/02-design/adr/ADR-002-backend-architecture.md` | `ERD.md` held eight entities and none carried a collection point identifier, there was no `CollectionPoint` entity, and `ADR-002` said nothing about whether one installation serves one point or many. | FR-46 requires every collection-point-scoped record to carry a collection point identifier, and the owner requirement set clearly assumes more than one point. | Sprint 2 assumes more than one collection point. `CollectionPoint` is added to `ERD.md` and `collectionPointId` is added to the relevant scoped entities. | Medium. Schema, design and requirement traceability are all updated. | R6 | Sprint 2 | Implemented |
| CR-022 | Week 3 | Scope | `docs/01-requirements/specification/SRS.md` `OS-3`; `PLAN.md` `T-44-01`; finding A of `docs/05-management/minutes/20261016_requirements-walkthrough.md` | The walkthrough had a reading of the billing boundary that left payment and settlement in an unclear state. | The team needed a clear scope decision before Sprint 2 operations and the owner-facing fee rules could be implemented. | Payment and settlement remain out of scope; overdue fee calculation remains in scope under `T-44-01`. | Low. The billing boundary and the owner report scope are clarified. | R2 | Sprint 2 | Implemented |
| CR-023 | Week 3 | Requirement | FR-19 and FR-20, `docs/02-design/location-recommender.md`, `ui-prototype.md`, `T-42-07` | `CR-004` kept storage-location recommendation as a should-have optional feature, but the prototype and sprint plan still talked as if the panel would be built in Sprint 2. | The recommendation feature had no requirement-backed Sprint 2 delivery and was creating contradictory expectations for design and implementation. | `FR-19` and `FR-20` are reduced to a future optional capability, and the recommendation panel stays out of Sprint 2. | Medium. Requirement, UI prototype and task scope are realigned. | R2 | Sprint 2 | Implemented |
| CR-024 | Week 3 | Scope | `T-42-01`/`T-42-03` shelving path and the `shelve later` fallback | The contract had no explicit continuation when a storage location was unavailable, so the flow depended on an undefined path rather than a defined fallback. | The project needed a scope ruling before implementation so that Sprint 2 would not promise a workflow it had not decided. | The `shelve later` workflow is deferred and stays out of Sprint 2. The default fallback when no suitable location is available is a temporary `PENDING` storage location, so the system keeps a defined operational path without creating a new Sprint 2 feature. | Medium. The shelving task, the SRS wording and the tests for a full shelf all change together. | R2 | Sprint 2 | Implemented |
| CR-025 | Week 3 | Requirement | FR-18 batch limit, `openapi.yaml`, `ui-prototype.md`, `T-42-01` | The batch requirement was defined as up to 50 parcels in one operation, but the contract still did not say what happened when a batch was larger than 50. | The full-batch overflow case had to be nailed down before implementation or the API contract would remain contradictory. | A batch over 50 is rejected with `400` before any entry is processed; the staff member must split the batch manually. | Medium. Contract, UI and batch-creation testing all change. | R2 | Sprint 2 | Implemented |
| CR-026 | Week 3 | Design | `docs/02-design/database/ERD.md` (`Setting`) and `docs/02-design/overdue-fee-rules.md` section 8 | `ERD.md` described `Setting.key` as `unique`, which reads as unique across the whole system and conflicts with the actual collection-point-scoped design. | The data model needed to match the real multi-point design and the overdue-fee rules already in place. | `Setting.key` is treated as unique within a collection point rather than globally. | Low. The database schema and the associated rules document align. | R1 | Sprint 2 | Implemented |
| CR-027 | Week 4 | Process | T-30-05 quality tooling and PR #230 | Issue #177 assumes the backend does not exist, but develop now contains the Spring Boot application, Maven wrapper, Java tests and project tooling that need a corrected description. | The team needed a governing record for the tooling correction so that the project’s assumptions and CI quality gates remain consistent. | The tooling record is updated to match the current repository state and the additive integration approach. | Low. CI and process documentation are updated without changing the product scope. | R10 | Sprint 2 | Implemented |
| CR-028 | Week 4 | Scope | `T-42-01`/`T-42-03` shelving path, and `CR-024`'s record of it | `SRS.md` 3.5 says no defined fallback exists when a storage location is unavailable, and `CR-024`'s `After` column claims "the normal full-shelf failure path" — a path that has never been specified. `#98` has carried the gap since week 3. | Option A: when a shelf is full, place the parcel in a temporary `PENDING` storage location until a real slot becomes available. This preserves a defined operational fallback, keeps `shelve later` out of Sprint 2, and avoids a 409 refusal that would block normal intake. | The default full-shelf behavior is a temporary `PENDING` storage location, with a later reassignment to a real shelf. `FR-14` and `FR-15` remain unchanged, and the SRS wording no longer says there is no defined fallback. | Medium. `T-42-01`/`T-42-03`, the SRS wording and the full-shelf test path are all affected. | R2 | Sprint 2 | Proposed |

## 3. Requests that were considered and rejected

Recording a rejected request is as useful as recording an accepted one, because
it shows the option was weighed.

| ID | Date | Request | Reason for rejection |
|---|---|---|---|
| CRJ-001 | Week 1 | Adopt a complex scoring algorithm for the storage location recommendation | A deterministic rule is easier to test and explain, and the survey did not support prioritising the recommendation feature above the time-critical design work. |
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
| v1.6 | Week 2 | Added CR-018, completing CR-014: the sidebar group becomes the shell's single definition, `--color-surface-alt` is removed and the `hi-fi.css` declarations that shadowed those tokens are deleted | R4 |
| v1.7 | Week 2 | Added CR-019, fixing the logbook template that three logbooks copied a wrong period and two copied a placeholder date from | R1 |
| v1.8 | Week 2 | Added CR-020, pointing the three remaining issue #75 references at issue #137, which CR-017 recorded as four | R1 |
| v1.9 | Week 3 | Added CR-021, the collection point decision that closes the design half of issue #127: Sprint 2 assumes more than one collection point, `CollectionPoint` is added to `ERD.md`, the five directly scoped entities carry `collectionPointId`, and `ADR-002` records the deployment model | R1 |
| v1.10 | Week 3 | Added CR-022, the billing scope ruling from the requirements walkthrough: the horizon is eight weeks, payment and settlement stay out of scope, and the overdue fee calculation stays in under `T-44-01`. `OS-3` already drew that line, so the recorded contradiction was a misreading and no SRS edit was needed | R1 |
| v1.11 | Week 3 | Added CR-023, CR-024 and CR-025 for the week 3 adjudication: the recommendation panel is demoted, the full-shelf fallback is deferred, and the 50-parcel batch cap is kept without a queue | R2 |
| v1.12 | Week 3 | Added CR-026, making `Setting.key` unique within a collection point rather than globally. The schema already did this and `overdue-fee-rules.md` section 8 already asked for it, so `ERD.md` moves to v1.4 and `overdue-fee-rules.md` to v1.1 for the same decision, and `V1__baseline.sql` is untouched | R1 |
| v1.13 | Week 4 | Recorded CR-027 for the PR #230 tooling correction: replace the obsolete pre-scaffold assumption with additive integration, preserve current CI, and adopt the reviewed formatting and commitlint configuration | R10 |
| v1.14 | Week 4 | Added section 1.3, which says a record number is taken when it appears in the section 2 table rather than when it is mentioned in an issue. Three collisions came from the other reading — `CR-018`, `CR-026` and `CR-027` — and `CR-027` is the one still live: `#98` reserved it for the full-shelf answer while `#230` wrote it for the tooling, so the full-shelf record moves to `CR-028` | R1 |
| v1.15 | Week 4 | Added CR-028 to define the Sprint 2 full-shelf fallback as a temporary `PENDING` storage location, and corrected the CR-024 wording so its `Item affected` and `After` text reflect the actual shelving path and the defined fallback | R2 |
