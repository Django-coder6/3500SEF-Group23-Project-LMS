# Project Report - outline and word budget

| Item | Detail |
|---|---|
| Task | T-20-14 |
| Owner | R1 |
| Status | Outline trimmed; chapters 1 and 2 drafted |
| Version | v0.4 |
| Deadline | 2026-12-08 |
| Limit | Not more than 1000 words, source code excluded |
| Submitted by | the leader only, as a PDF through the OLE assignment link |

## First line of the report
The course requires the GitHub link on the first line of the report:
https://github.com/Django-coder6/3500SEF-Group23-Project-LMS

It must also carry a guide to install and run the project. `README.md` holds the complete setup
and run guide. The backend, Maven wrapper, Flyway migrations, seeded data and full Docker Compose
stack are now available through `T-30-02`, `T-30-03` and the related Sprint 2 work.

## How the report is marked
Course rubric, Project Report (15 per cent):

| Part | Weight | What it asks for |
|---|---|---|
| Quality of project management | 12% | Correct use of software engineering concepts: software processes, requirement engineering, system modeling, architectural design, design and implementation, software testing, and software evolution. |
| Quality of report | 3% | Organization, design and analysis, writing |

The 12 per cent evaluates whether the team used software engineering deliberately. Every section names the artefact that evidences it, so the writing points at something rather than asserts.

## Word budget
1000 words is not much. This split leaves 60 words for the conclusion, and the
nine section figures below add to 980 rather than the 1130 they added to before this
revision, because the earlier table was 150 words over its own limit.

| Section | Words | Rubric concept | Evidence to cite |
|---|---|---|---|
| Title block and repository link | 20 | - | README.md |
| 1. The problem and the context | 130 | requirement engineering | SRS 1.2, 1.3; survey-results.md |
| 2. Process | 150 | software processes | PLAN.md; team-charter.md; the sprint minutes |
| 3. Requirements engineering | 115 | requirement engineering | SRS; survey-results.md; personas; user-journey-maps.md; change-log.md CR-002 |
| 4. System modeling | 90 | system modeling | the twelve .puml sources; uml/README.md |
| 5. Architectural and database design | 75 | architectural design | ADR-002 to ADR-005; ERD.md; backend-state-machines.md |
| 6. Design and implementation | 100 | design and implementation | design-system.md; tokens.css; openapi.yaml; the frontend skeleton |
| 7. Testing and validation | 95 | software testing | test-plan.md; 29 backend and 68 frontend tests; RTM.md |
| 8. Software evolution | 165 | software evolution | change-log.md; risk-register.md; the cases below |
| 9. Conclusion and reflection | 60 | - | - |

## Section notes

### 2. Process
Five sprints in eight weeks, with Sprint 0 and Sprint 1 one week each. Execution included a Wednesday checkpoint, a Sunday logbook deadline, one approval before a merge, and protected main/develop branches. The process held for design but slipped for requirements, as the risk register records.

### 3. Requirements engineering
The elicitation method changed mid-project. Initial interviews provided weak evidence and were replaced by two questionnaires (29 responses) and four composite personas. Chapters 1 and 2 were then corrected to align with this new basis (CR-002, CR-008, CR-009). The limitation is a single convenience sample, meaning targets are design targets rather than statistically supported (RSK-03).

### 4. System modeling
Twelve PlantUML sources are kept as text for reviewable changes. Tracing these models against the contract revealed two real defects in the contract (one fixed, one open), proving the models' value.

### 5. Architectural and database design
Four ADRs detail the chosen alternatives and rejected options. The parcel status model reduced seven statuses to three, as the others described courier stages outside the SRS system boundary (CR-006).

### 6. Design and implementation
The frontend uses Vue 3, with the design system expressed as CSS custom properties to prevent raw colors or sizes in components. The backend uses Java 17 and Spring Boot 3.3. The contract was frozen before any code, so the frontend could be built against it in parallel; by week 3 the backend implements login, JWT, four-role access control, the audit log and the Flyway baseline.

### 7. Testing and validation
The test plan prioritizes handover idempotency under concurrency, single location per parcel, duplicate waybill rejection, and single-use pickup codes. Continuous verification runs on every pull request: ESLint, 68 frontend tests, 29 backend tests, Flyway and a Compose smoke test that signs in as a seeded account.

### 8. Software evolution
Five cases, each with a decision behind it:
- **Scope ruling:** Resident pages are P0 but lack contract endpoints; the product owner ruled this belongs to Sprint 2 (CR-013).
- **Design change:** Exception types changed to five values. The premise was corrected as CR-006 only covered parcel status (CR-010, CR-011).
- **Single-sourcing:** A list of exception types in seven documents was unified by placing it in the glossary (CR-012).
- **Model correction:** Handover models originally showed a 409 on repeated confirmation; they were updated to match the idempotent contract.
- **Regression:** Case-sensitive link issues between `personas.md` and `Personas.md` were fixed by rewriting the link check.
- **Risk register:** Reviewed every sprint; risks are closed with evidence, and blocked risks are explicitly recorded.

### 9. Conclusion
The team would freeze requirements before design and write the traceability matrix concurrently with the requirements, rather than retrospectively.

## Writing rules
- English, per team-charter.md section 5.3.
- Cite the artefact, not the memory: "change-log.md CR-010", not "we discussed".
- No personal names for respondents. The personas are composites and are named by role (CR-009).
- Count the words before submitting. 1000 is a hard limit.
- The PDF is submitted by the leader only. Put the repository link on line one.

## Current drafting status

| Section | Status | Source |
|---|---|---|
| 1. The problem and context | Drafted; needs reduction to its 130-word allocation | `chapters/01-introduction.md` |
| 2. Process | Drafted; needs reduction to its 150-word allocation | `chapters/02-process.md` |
| 3. Requirements engineering | Drafted; within budget | `chapters/03-requirements.md` ([PR #257](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/257)) |
| 4. System modeling | Drafted; within budget | `chapters/04-modeling.md` ([PR #258](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/258)) |
| 5-9 | Outline and section notes only | `00-outline.md` and the cited project artefacts |

## Revision history
| Version | Week | Change | Author |
|---|---|---|---|
| v0.1 | Week 2 | First version recorded. Skeleton, over limit at 1123 words. | R1 |
| v0.2 | Week 3 | Trimmed redundant text to bring the outline body under the 1000-word limit while preserving section structure. Clarified that the limit applies to the submitted report, not metadata. | R9 |
| v0.3 | Week 3 | The word budget table added to **1150 against a limit of 1000**, and the sentence above it promised "about 100 words for the conclusion" while the table gave the conclusion 80. Chapter 1 is raised from 80 to 130 because it now cites the resident and staff pain points with their counts, which is what the requirement engineering criterion asks for, and cutting it back would turn evidence into assertion. The 150 words come from chapters 3 to 9, and chapter 8 keeps the largest single share because this outline itself calls it the section that distinguishes the report. Chapter 2 keeps 150, so its drafted 176 words need a 26-word trim rather than a budget change | R1 |
| v0.4 | Week 3 | Corrected the stale runnable-state note now that the backend and full Compose stack are delivered. Replaced the obsolete missing-artefact table, whose issues are closed, with the current drafting status; found that both drafted chapters still need trimming to their budget | R4 |
