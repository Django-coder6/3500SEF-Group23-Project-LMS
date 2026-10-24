# Project Report - outline and word budget

| Item | Detail |
|---|---|
| Task | T-20-14 |
| Owner | R1 |
| Status | Skeleton, to be written |
| Version | v0.2 |
| Deadline | 2026-12-08 |
| Limit | Not more than 1000 words, source code excluded |
| Submitted by | the leader only, as a PDF through the OLE assignment link |

## First line of the report
The course requires the GitHub link on the first line of the report:
https://github.com/Django-coder6/3500SEF-Group23-Project-LMS
It must also carry a guide to install and run the project. `README.md` holds the guide; it is not yet runnable, because `backend/pom.xml` and the Maven wrapper arrive with issue #137.

## How the report is marked
Course rubric, Project Report (15 per cent):

| Part | Weight | What it asks for |
|---|---|---|
| Quality of project management | 12% | Correct use of software engineering concepts (processes, requirements, modeling, design, testing, evolution). Show understanding. |
| Quality of report | 3% | Organization, design and analysis, writing. |

Every section names the artefact that evidences it, so the writing points at something rather than asserts.

## Word budget
1000 words is not much. This split leaves about 100 words for the conclusion.

| Section | Words | Rubric concept | Evidence to cite |
|---|---|---|---|
| Title block and repository link | 20 | - | README.md |
| 1. The problem and the context | 80 | requirement engineering | SRS 1.2, 1.3; survey-results.md |
| 2. Process | 150 | software processes | PLAN.md; team-charter.md; sprint minutes |
| 3. Requirements engineering | 150 | requirement engineering | SRS; survey-results.md; personas; user-journey-maps.md; CR-002 |
| 4. System modeling | 120 | system modeling | twelve .puml sources; uml/README.md |
| 5. Architectural and database design | 100 | architectural design | ADR-002 to ADR-005; ERD.md; backend-state-machines.md |
| 6. Design and implementation | 150 | design and implementation | design-system.md; tokens.css; openapi.yaml; frontend skeleton |
| 7. Testing and validation | 120 | software testing | test-plan.md; eleven unit tests; RTM.md |
| 8. Software evolution | 180 | software evolution | change-log.md; risk-register.md; the cases below |
| 9. Conclusion and reflection | 80 | - | - |

## Section notes

**2. Process**
Five sprints in eight weeks. Actual run: Wednesday checkpoint, Sunday logbook deadline, one approval before merge, protected main/develop. The process held for design but slipped for requirements (recorded in risk register).

**3. Requirements engineering**
Pivoted from weak interviews to two questionnaires (29 responses) plus four composite personas. Corrected chapters 1 and 2 accordingly (CR-002, CR-008, CR-009). Limitation: one convenience sample, targets are design targets (RSK-03).

**4. System modeling**
Twelve PlantUML sources (kept as text for reviewable diffs). Tracing models against the contract found two real defects in the contract (one fixed, one open).

**5. Architectural and database design**
Four ADRs (each with rejected alternative). Parcel status model reduced from seven to three statuses (CR-006).

**6. Design and implementation**
Frontend: Vue 3 with design system as CSS custom properties. Backend: contract frozen before code. Honest note: backend holds no code yet.

**7. Testing and validation**
Key cases: handover idempotency under concurrency, one location holding one parcel, duplicate waybill rejection, pickup code single use. Running verification: ESLint, eleven unit tests, production build on push.

**8. Software evolution (longest section)**
Five cases with decisions:
- **Scope ruling:** Resident pages P0 but missing contract endpoints. Routed to PO, ruled for Sprint 2. Record: CR-013.
- **Design change:** Exception types changed to five values. Corrected premise: CR-006 was about parcel status only. Records: CR-010, CR-011.
- **Single-sourcing fix:** Same exception list appeared in seven documents. Fix: glossary holds the list, others refer to it (CR-012).
- **Model correction:** Handover models showed 409 on repeated confirmation. Contract was already idempotent; models changed to match contract.
- **Regression:** Two SRS links pointed at `personas.md` vs `Personas.md`. Windows resolves silently, GitHub does not. Link check rewritten to be case-exact.
- **Risk register:** Reviewed every sprint, risks closed with evidence, one recorded as blocked on another team member's deliverable (RSK-03).

**9. Conclusion**
What the team would do differently: freeze requirements before design, and write the traceability matrix as requirements are written.

## Writing rules
- English, per `team-charter.md` section 5.3.
- Cite the artefact: "change-log.md CR-010", not "we discussed".
- No personal names for respondents. Personas are composites named by role (CR-009).
- Count the words before submitting. 1000 is a hard limit.
- PDF submitted by leader only. Repository link on line one.

## What is still missing before this can be written

| Section | Waiting on | Issue |
|---|---|---|
| 3, 7, 9 | SRS chapters 3 and 4, and the traceability matrix | #76, #77 |
| 3 | the product backlog and acceptance criteria | #78 |
| 6 | the backend skeleton | #137 |
| 7 | the test cases and the defect log | #78 depends on matrix |

## Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v0.1 | Week 2 | First version. Skeleton, over word limit at 1123 words. | R1 |
| v0.2 | Week 3 | Trimmed to under 1000 words using bullet points. Clarified limit applies to submitted report, not metadata. | R9 |
