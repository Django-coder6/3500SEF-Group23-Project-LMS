https://github.com/Django-coder6/3500SEF-Group23-Project-LMS

# Community Parcel Collection Point Management System
**COMP 3500SEF Group 23 | November 2026**

---

# 1. The Problem and the Context

Community collection points receive bulk parcels for local residents. Staff already use digital tools: among seven staff respondents, three use spreadsheets, two use existing software, and two use chat histories. Records are fragmented, so staff cannot reliably answer where a parcel is or who collected it.

Four role-specific questionnaires (29 responses) put different problems first: late or missing notification for 4 of 11 residents, and batch registration for 4 of 7 staff. The project builds a web system that records parcel locations, issues pickup codes, and audits every handover.

# 2. Process

*[Insert Figure 1 here: Process / Sprint Cadence Table from PLAN.md]*

**Figure 1: Process (Shows the hybrid process and sprint cadence)**

The project spans eight weeks across five sprints: Sprint 0 and Sprint 1 one week each, then three two-week sprints. Every task has an identifier, owner and folder in `PLAN.md`; completion requires its artefact.

We used a hybrid incremental and plan-driven model: two-week sprints, a product backlog and retrospectives inside a fixed SRS/architecture baseline and change log. This fitted a fixed assessment calendar with moving requirements; waterfall would have frozen the wrong scope, while pure Scrum would have under-produced the artefact trail the rubric assesses.

The cadence is in `team-charter.md` and the sprint minutes: a Wednesday checkpoint, a Sunday logbook deadline, non-author review before merge, and protected branches. Review evidence is the PR thread, not always a formal approval. A change not in `change-log.md` did not happen.

Design held to schedule; requirements did not. Documents were still changing in week 3, after coding began; `RSK-01` predicted that eight weeks leaves no buffer, so Sprint 1 slippage spills into build.

# 3. Requirements Engineering

Requirements were elicited through four role-specific questionnaires (29 responses). The data shaped scope: location recommendation was demoted from must-have to should-have because 0 of 7 staff requested it (`CR-004`), while delegated collection was excluded because 0 of 11 residents chose it (`CR-005`).

Composite personas and user journey maps grounded the findings in scenarios. The SRS defines 47 functional and 14 non-functional requirements. The RTM maps every must-have to a design artefact and a reserved test identifier; implementation status is recorded separately rather than implied by the mapping.

# 4. System Modeling

System modeling is anchored by the parcel lifecycle state machine. The model defines three states: `registered → shelved → collected`, with strict transitions enforced by the service layer (`CR-006`). The original seven-state enumeration was reduced because `ready_for_pickup` and `out_for_delivery` described courier logistics stages that the SRS places outside the system boundary.

Exceptions are not modeled as parcel states. Instead, a separate `ParcelException` flow (`open → processing → resolved`) records damage, loss, or wrong-recipient cases without altering the parcel's physical status. Illegal transitions are rejected with a 422 error.

# 5. Architectural and Database Design

*[Insert Figure 2 here: 3-Tier Architecture Diagram]*

**Figure 2: Architecture (Shows the 3-tier architecture and P0 fix)**

Four ADRs record the architecture and rejected options. `ADR-002` fixes a layered monolith: Vue 3 SPA, REST contract, Spring Boot controller/service/repository, PostgreSQL, Flyway and an audit aspect. We rejected microservices because four developers had eight weeks, and because handover needs one transaction across parcel, location and audit.

`ADR-003` fixes the frontend, `ADR-005` selects Spring Boot, and `ADR-004` records the deferred recommendation rule. The schema is one Flyway baseline of nine tables and eighteen foreign keys. The parcel model reduced seven statuses to three, because the others described courier stages outside the SRS boundary (`CR-006`).

# 6. Design and Implementation

Design and implementation share a contract-first boundary. The OpenAPI contract was frozen before frontend feature work, so Vue pages and Spring services could be built in parallel; **six operations** were explicitly marked specified and not built rather than allowed to drift.

The frontend uses one Axios wrapper and a tokenised status language: `tokens.css` defines semantic roles once, and `design-system.md` maps parcel, location, pickup and exception states to them. The backend keeps state changes in services, uses Flyway for every schema change, and writes audit rows through one aspect.

# 7. Testing and Validation

*[Insert Figure 3 here: RTM Traceability Matrix Table]*

**Figure 3: Traceability (Shows the RTM linking requirements to tests)**

Continuous verification runs on every pull request: ESLint, **68 frontend tests**, and **90 backend tests** (including **35 Testcontainers** tests across 7 classes). The test suite achieves **88.78% line coverage** and **64.64% branch coverage**, confirming that core workflows are adequately validated. The database design incorporates **18 foreign keys** and **36 change records (CRs)** have been logged to date.

The most important defect came from tracing the model, not from a green test: handover tests inserted `shelved` directly, so they passed while the application could not reach that state. The missing path is now a recorded P0 defect and is fixed.

# 8. Software Evolution

Change is recorded rather than remembered: thirty-six change records carry every decision. `CR-013` added the resident read paths to the contract, exposing that they were absent from the implementation; they remain specified and not built.

`CR-010/011` corrected the exception types to five values, because `CR-006` governed only parcel status. `CR-012` single-sourced an exception list seven documents restated. `CR-029` corrected `CR-028`, which invented a pending storage slot the schema cannot represent. `CR-033` made handover idempotent, matching `NFR-4` against a task description that claimed the opposite.

**CR-034–036 record the reachability fixes. The risk register closes a risk only with evidence.**

# 9. Conclusion and Reflection

Two lessons would change the order of work. Requirements would be frozen before design, because revision in week 3 forced the contract to move under working code.

The traceability matrix would be written alongside the requirements rather than reconstructed afterwards, since a matrix built in retrospect proves what was remembered rather than what was decided.
