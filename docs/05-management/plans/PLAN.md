# Project plan

| Item | Detail |
|---|---|
| Task | T-00-07 |
| Owner | R1 |
| Version | v1.4 |
| Status | Active, reviewed at the end of every sprint |


Eight weeks, five sprints. Every task has an ID, an owner and a folder.
Task IDs are used in commit messages so that any commit can be traced back
to the task it belongs to.

## Sprint overview

| Sprint | Week | Goal | Tag |
|---|---|---|---|
| Sprint 0 | 1 | repository, research, competency review | v0.0.1 |
| Sprint 1 | 2 | requirements and design baseline | v0.1.0 |
| Sprint 2 | 3-4 | intake, shelving and pickup working end to end | v0.2.0 |
| Sprint 3 | 5-6 | notifications, exceptions, stocktake, billing, progress review | v0.3.0 |
| Sprint 4 | 7-8 | full test pass, report, presentation, logbooks | v1.0.0 |

Sprint 0 and Sprint 1 are one week each rather than the usual two to four.
This is a deliberate compression during the startup phase, where the work is
documentation and setup rather than development. Sprints 2 to 4 follow the
usual two week length.

## Module ownership

| Module | Name | Owner |
|---|---|---|
| M1 | users and permissions | R6 |
| M2 | parcel intake | R7 |
| M3 | storage locations and the recommender | R6 |
| M4 | pickup and handover | R6 |
| M5 | delegated collection | R7 |
| M6 | billing engine | R7 |
| M7 | notifications | R7 |
| M8 | exception parcels | R7 |
| M9 | stocktake and reconciliation | R7 |
| M10 | reporting, audit and admin | R6 |

Frontend foundation is owned by R4: application layout, routing, Pinia setup, Axios wrapper and shared components. Feature pages are implemented by R5 under R4's standards and reviewed by R4. Page-level ownership follows docs/02-design/frontend-page-map.md

## Sprint 0, week 1

The setup week. `PLAN.md` named it in the sprint overview from the first draft, and this section
is its task list: without it Sprint 0 could only be audited by guessing which files it should have
produced.

| ID | Task | Owner | Folder |
|---|---|---|---|
| T-00-01 | Team charter and the ethics and privacy statement | R1, R9 | `docs/05-management` |
| T-00-02 | Repository scaffold: `README.md`, `CONTRIBUTING.md`, the issue and pull request templates, `.gitignore` | R1 | repository root |
| T-00-03 | Logbook template and the first entries, so every member has a place to record work from day one | R1 | `docs/06-logbook` |
| T-00-05 | Ten week 1 logbooks, each with a date, a time range, a task ID and an evidence link | All | `docs/06-logbook` |
| T-00-06 | Weekly stand-up, risk register, change log and the contributor screenshot | R1 | `docs/05-management` |
| T-00-07 | Project plan, release plan and glossary | R1 | `docs/05-management/plans`, `docs` |
| T-00-08 | Consistency checklist used before each pull request | R1 | `docs/05-management` |
| T-10-01 | Four role questionnaires, the response summary and the composite personas | R1, R2 | `docs/01-requirements/elicitation` |
| T-10-04 | User journey maps for the four roles, referenced by the SRS | R4 | `docs/01-requirements/elicitation` |
| T-30-01 | Docker Compose skeleton, started early so Sprint 2 can build on it | R10 | repository root |

Deliverables that must exist at the end of the week:

1. `README.md` and `CONTRIBUTING.md` describing the project, the team and the working agreement.
2. `docs/05-management/team-charter.md` and `ethics-statement.md`.
3. `docs/01-requirements/elicitation/` holding the questionnaires, the survey results and the personas.
4. `docs/05-management/risk-register.md`, `change-log.md` and the first contributor screenshot.
5. Ten logbooks in `docs/06-logbook/`, one per member.
6. Release tag `v0.0.1`.

> **Two notes on identifiers.** `T-00-06` is the recurring management task and appears again in
> Sprint 1 and every sprint after it; it is not a duplicate. `T-00-04` is unused: the numbering
> skips it because the task it was reserved for was folded into `T-00-03`. Recording the hole is
> cheaper than renumbering every reference to it.

> **On the competency review.** The sprint overview describes Sprint 0 as "repository, research,
> competency review". The first two produced the artefacts above. The third produced no document of
> its own: the gap analysis lives in `RSK-11`, which records that Java and Spring Boot are new to
> part of the team. A separate competency review would restate that risk, so it was not written.

## Sprint 1, week 2

The goal is a frozen requirements and design baseline. At the end of this week
the SRS is complete, every requirement has a test mapping, and the design
baseline is committed so that Sprint 2 can start building without further
decisions.

| ID | Task | Owner | Folder |
|---|---|---|---|
| T-10-05 | SRS chapters 1 and 2: background, problem, goals, scope, overall description | R3 | docs/01-requirements/specification |
| T-10-05a | Design baseline consistency sweep: ERD, OpenAPI, ADR statuses and retired terminology | R6 | docs/02-design |
| T-10-06 | Functional requirements, 47 items grouped by role, each with rationale and MoSCoW priority | R3, R2 | docs/01-requirements/specification |
| T-10-07 | Non-functional requirements, 14 items, each with a measurable target and a verification method | R3 | docs/01-requirements/specification |
| T-10-08 | Product backlog with acceptance criteria and MoSCoW order | R2 | docs/01-requirements/specification |
| T-10-09 | Requirements traceability matrix mapping each requirement to its design, code and test cases | R3, R8 | docs/01-requirements |
| T-10-10 | Requirements validation: internal walkthrough, findings recorded and closed | R2 | docs/05-management/minutes |
| T-10-11 | Requirements change process and change log | R2 | docs/05-management |
| T-10-12 | Priority review meeting, resolving conflicts between resident convenience and staff effort | R2 | docs/05-management/minutes |
| T-10-13 | Prototype-driven requirement confirmation with three respondents | R2 | docs/01-requirements/elicitation |
| T-20-01 | Architecture design: layered structure, module boundaries, system context | R6 | docs/02-design |
| T-20-02 | Database design: entity relationship diagram, tables, unique constraints, indexes | R6 | docs/02-design/database |
| T-20-03 | Interface design: OpenAPI 3 specification covering the endpoints Sprint 2 needs | R6, R10 | docs/03-api |
| T-20-04 | State machine design for the parcel, storage location and exception ticket, as PlantUML sources | R7 | docs/02-design/uml |
| T-20-05 | UI prototype for the resident, staff and admin pages | R5 | docs/02-design |
| T-20-06 | Design system: colour, type, spacing and component rules | R4 | docs/02-design |
| T-20-07 | UML baseline: use case diagrams, sequence diagrams and the class diagram, as PlantUML sources | R4 | docs/02-design/uml |
| T-20-08 | Design review meeting covering architecture, database, API and UI, findings recorded | R6 | docs/05-management/minutes |
| T-20-09 | Frontend engineering conventions: folder structure, naming, component split, state management | R5 | docs/02-design |
| T-20-10 | Usability review of the prototype against the ten heuristics, findings recorded | R9 | docs/02-design |
| T-20-11 | Activity models for the intake and handover business processes | R4 | docs/02-design/uml |
| T-20-12 | High fidelity visual designs for the pickup, intake and dashboard pages | R4 | docs/02-design |
| T-20-13 | Architecture decision records, including the database choice | R6 | docs/02-design/adr |
| T-20-14 | Report chapters 4 and 5: figure captions and explanatory text | R1 | docs/07-report/chapters |
| T-20-15 | Storage location recommender design: zone rules, scoring, hot and cold zones | R6, R7 | docs/02-design |
| T-20-16 | Overdue fee engine design: rule table, pure calculation function, cap and waiver | R7 | docs/02-design |
| T-00-06 | Weekly stand-up, risk register, change log and weekly report | R1 | docs/05-management |
| T-50-19 | Requirement walkthrough support and the first draft of the test plan | R8 | docs/04-testing |
| T-30-01 | Docker Compose for PostgreSQL, Redis, backend and frontend, started early so Sprint 2 can build on it | R10 | repository root |

Deliverables that must exist at the end of the week:

1. `SRS.md` complete, chapters 1 to 4: introduction, overall description,
   functional requirements and non-functional requirements.
2. `RTM.md` with every must-have requirement mapped to at least one test case.
3. `docs/02-design/uml/` containing the use case, sequence, class, state and
   activity sources, all in PlantUML.
4. `openapi.yaml` covering every endpoint Sprint 2 will call.
5. `ERD.md` consistent with the parcel status model in the SRS.
6. `docs/05-management/risk-register.md` and `change-log.md`.
7. Release tag `v0.1.0`.


> **Task id note.** `T-10-05` covers SRS chapters 1 and 2 only. Chapter 3 is
> always `T-10-06`. The consistency sweep is `T-10-05a`. Do not reuse `T-10-05`
> for chapter 3.

## Sprint 2, weeks 3 to 4

The goal is that a staff member can record a parcel, the system suggests a
storage location, the resident receives a pickup code, and the staff member
hands the parcel over. Everything else waits.

| ID | Task | Owner | Folder |
|---|---|---|---|
| T-30-01 | Docker Compose with PostgreSQL, Redis, backend and frontend | R10 | repository root |
| T-30-02 | Spring Boot skeleton: controller / service / repository / entity / dto, unified error handling | R6 | backend/src/main/java/com/group23/common |
| T-30-03 | Flyway migrations and seed data | R7 | backend/src/main/resources/db/migration |
| T-30-04 | Frontend skeleton: Vite, router, Pinia, Axios wrapper, shared layout, /login and /dashboard placeholders | R4 | frontend/src |
| T-30-05 | Checkstyle, Spotless, commitlint, Git hooks | R10 | repository root |
| T-30-06 | CI runs `./mvnw verify` and frontend tests | R6 | .github/workflows |
| T-41-01 | Registration, login, JWT, four-role RBAC | R6 | backend/src/main/java/com/group23/auth |
| T-41-02 | Login page; connect to the auth store and route guards | R5 | frontend/src/views |
| T-41-03 | Audit log interceptor | R7 | backend/src/main/java/com/group23/common |
| T-41-04 | Unit tests for auth and RBAC | R8 | backend/src/test/java/com/group23 |
| T-42-01 | Staff records a parcel, carrier recognised from the waybill prefix, duplicate waybills rejected | R7 | backend/src/main/java/com/group23/parcel |
| T-42-02 | Storage location recommender: size filter, zone priority, the deterministic ordering from `location-recommender-rules.md`, safe under concurrency | R6 | backend/src/main/java/com/group23/location |
| T-42-03 | Storage location management: zones, states, manual override | R7 | backend/src/main/java/com/group23/location |
| T-42-04 | Pickup code issue and handover, one-time use, transaction and unique constraint | R6 | backend/src/main/java/com/group23/pickup |
| T-42-05 | Pickup verification and handover page | R4 | frontend/src/views |
| T-42-06 | Location board component with zone filter and full-shelf warning | R5 | frontend/src/components |
| T-42-07 | Staff workbench: intake form, suggested location, handover screen | R5 | frontend/src/views |
| T-42-08 | Unit and integration tests for the intake to handover path | R8 | backend/src/test/java/com/group23 |
| T-42-09 | Front end and back end integration, defect fixes | R5 | frontend and backend |

## Sprint 3, weeks 5 to 6

| ID | Task | Owner | Folder |
|---|---|---|---|
| T-43-01 | Notifications: in-app message plus a pluggable provider interface | R7 | backend/src/main/java/com/group23/notify |
| T-43-03 | Delegated collection codes: one-time, expiry, revoke | R6 | backend/src/main/java/com/group23/pickup |
| T-43-04 | Exception parcel tickets: the five types maintained in the glossary | R7 | backend/src/main/java/com/group23/ticket |
| T-43-05 | Admin pages: collection points, locations, staff, fee rules | R5 | frontend/src/views |
| T-43-12 | Stocktake: task generation, difference recording, reason categories | R7 | backend/src/main/java/com/group23/stocktake |
| T-43-07 | Second integration test round, clear P0 and P1 defects | R10 | docs/04-testing |
| T-43-08 | Progress review pack: demo script, slides, contributor screenshot, RTM | R1 | docs/05-management |
| T-44-01 | Billing engine: rule table, pure calculation function, cap and waiver | R7 | backend/src/main/java/com/group23/billing |
| T-44-03 | Operations dashboard: location utilisation, daily intake and pickup volume | R5 | frontend/src/views |
| T-44-04 | Security hardening: token refresh, phone masking, rate limiting | R6 | backend/src/main/java/com/group23/config |

## Sprint 4, weeks 7 to 8

| ID | Task | Owner | Folder |
|---|---|---|---|
| T-44-05 | Reports view: parcel volume, overdue and exception summary | R4 | frontend/src/views |
| T-44-06 | End to end tests with Playwright covering the must-have flows | R8 | frontend/tests |
| T-50-01 | System testing across the five test scope dimensions | R8 | docs/04-testing |
| T-50-02 | Performance and concurrency testing | R8 | docs/04-testing |
| T-50-03 | Security testing, including pickup code replay | R8 | docs/04-testing |
| T-50-05 | Test report with testing insight and recommendation entries | R8 | docs/04-testing/reports |
| T-50-09 | Deploy to the cloud, keep a local backup for the demonstration | R10 | docs/05-management |
| T-50-11 | Report chapters | R9 | docs/07-report/chapters |
| T-50-13 | Every member finalises their personal logbook | everyone | docs/06-logbook |
| T-50-14 | Retrospective and lessons learned | R1 | docs/05-management |

## Assessment milestones

These dates come from the course brief, not from the team. The eight-week
project plan above has to fit around them.

| Date | Item | Owner |
|---|---|---|
| 2026-09-30 | Logbook interim submission 1 | every member |
| 2026-10-15 | Logbook interim submission 2 | every member |
| 2026-10-30 | Logbook interim submission 3 | every member |
| 2026-11-03 to 2026-11-23 | In-class progress review, weeks 10 to 12 (10 per cent) | R1, whole team |
| 2026-11-15 | Logbook interim submission 4 | every member |
| 2026-11-24 or 2026-11-30 | Written test (20 per cent, individual) | every member |
| 2026-12-08 | Project report (15 per cent) and final logbook (15 per cent) | R1 submits the report |

### How the two calendars fit together

The project is planned to finish at the end of week 8, which is around
2026-11-22. The in-class progress review falls in weeks 10 to 12, which overlaps
the end of the project. The consequence is deliberate:

- the Sprint 3 increment is what gets demonstrated at the progress review,
  not the final build, so the demonstration material has to be ready by the
  end of week 6;
- Sprint 4 is used for testing, the report, the recorded presentation and the
  logbooks, with the 2026-12-08 deadline as the hard stop;
- the written test falls in the same period as Sprint 4, so Sprint 4 carries
  the lightest development load.

## Definition of done

A task is done when:

1. the code or document is committed on a branch that names the task
2. a pull request is open with the task ID in the title
3. one other person has approved it and CI is green
4. the deliverable sits in the folder listed above
5. the person who did it has written a logbook entry with a link

## Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v1.0 | Week 1 | Initial eight-week, five-sprint plan with a task ID, an owner and a folder for every task | R1 |
| v1.1 | Week 2 | `T-42-02` corrected to drop the turnover score, which `CRJ-001` had rejected (`CR-016`), and the week 2 scope of issue #73 narrowed to the two jobs that could run (`CR-017`) | R1 |
| v1.2 | Week 3 | Added the Sprint 0 section, which the sprint overview named but the body never described. The task identifiers are the ones the files and commits already use, not new ones | R1 |
| v1.3 | Week 3 | Moved `T-30-06` from R10 to R6. `#137`'s fourth acceptance criterion already assigns enabling the backend CI job to R6, and one deliverable should not carry two owners; `PLAN.md` was the odd one out. R10 keeps `T-30-01` (delivered) and `T-30-05`, and wrote the commented-out `backend` block that R6 now activates | R1 |
| v1.4 | Week 3 | Corrected the Sprint 1 deliverable from "chapters 1 to 7" to "chapters 1 to 4". The line had said 1 to 7 since it was written on 12 October, while `SRS.md` has had four chapters throughout, its table of contents lists four, and no other document mentions seven; `SRS.md` 1.7 cites IEEE 830 / ISO 29148 as its structural basis and four chapters are those standards' core sections. Also replaced two tab characters that had taken the place of the cell delimiter in the Sprint 2 and Sprint 4 task tables. `#175` took `v1.3` for the `T-30-06` ownership change while this was open, so this is `v1.4` rather than a second `v1.3` | R1 |
