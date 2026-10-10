# LinYi - Community Parcel Collection Point Management System

COMP 3500SEF Software Engineering, group project, 10 members.

LinYi manages the daily work of a neighbourhood parcel collection point: a
courier drops parcels at the counter, staff record them and put them on a
shelf, residents come and collect them, and the site owner gets usage and
revenue figures at the end of the month.

The system is a web application only. It does not control any physical
equipment. Receiving, shelving, handing over and reporting are all recorded
by staff through the web interface.

## Repository layout

| Folder | Contents | Owner |
|---|---|---|
| docs/01-requirements | survey and interview notes, SRS, backlog, RTM | R2, R3 |
| docs/02-design | architecture, UML sources, ER diagram, ADRs | R4, R6 |
| docs/03-api | OpenAPI specification | R6 |
| docs/04-testing | test plan, test cases, defect log, test report | R8 |
| docs/05-management | meeting minutes, risk register, change log, weekly screenshots | R1 |
| docs/06-logbook | one personal logbook per member | everyone |
| docs/07-report | report chapters and figures | R9, R1 |
| backend | Java 17 and Spring Boot service, Maven layout | R6, R7 |
| frontend | Vue 3 and Vite client | R4, R5 |

## Team

| ID | Role | Name | GitHub | Module |
|---|---|---|---|---|
| R1 | Project manager | | @ | schedule, reviews, report process chapters |
| R2 | Product owner | | @ | backlog, priorities, billing rules |
| R3 | Requirements analyst | | @ | SRS, RTM, elicitation |
| R4 | Frontend lead | | @ | frontend architecture, design system, UML |
| R5 | Frontend developer | | @ | resident, staff and admin pages |
| R6 | Backend lead | | @ | architecture, data model, API, recommender |
| R7 | Backend developer | | @ | location, pickup, billing, stocktake |
| R8 | QA lead | | @ | test plan, cases, defects, acceptance |
| R9 | Documentation | | @ | report editing, user manual, glossary |
| R10 | DevOps and integration | | @ | CI, Docker, fake data service, deployment |

## Technology stack

| Layer | Choice | Reason |
|---|---|---|
| Frontend | Vue 3, Vite, Pinia, Element Plus | Component based, quick to build, easy for ten people to work on in parallel |
| Backend | Java 17, Spring Boot 3.3 | Mature layered structure, and real transaction support for the handover requirement |
| Build | Maven (use the Maven wrapper `mvnw`) | Same build on every machine, no local Maven install needed |
| Persistence | Spring Data JPA / Hibernate | Entities map directly onto the ER design |
| Migrations | Flyway | Schema versions are files in the repository, so every change is reviewable |
| Database | PostgreSQL 16 | Storage location, parcel and handover data are strongly relational, and handover needs row level locking |
| Cache | Spring Data Redis | Pickup code checks and hot storage location lookups |
| Security | Spring Security with JWT | Four roles with clear permission boundaries |
| API documentation | SpringDoc OpenAPI | Generated from the controllers, so it cannot drift from the code |
| Backend tests | JUnit 5, Mockito, MockMvc | Unit tests plus slice tests for the controllers |
| Frontend tests | Vitest, Playwright | Unit tests plus end to end tests |
| CI | GitHub Actions | Lint, tests and build run on every push |
| Deployment | Docker Compose, Nginx | One command reproduces the environment |

## Getting started

This section is filled in during Sprint 2 (week 3) once the backend and
frontend skeletons exist.

```bash
git clone https://github.com/Django-coder6/3500SEF-Group23-Project-LMS.git
cd 3500SEF-Group23-Project-LMS

# backend (Java 17 and the Maven wrapper)
cd backend
./mvnw clean install        # on Windows use: mvnw.cmd clean install
cd ..

# frontend
cd frontend
npm install
cd ..

docker compose up -d
```

## Sprint status

The project runs for eight weeks.

| Sprint | Week | Goal | Status |
|---|---|---|---|
| Sprint 0 | 1 | topic, repository, user research, competency review | done |
| Sprint 1 | 2 | requirements and design baseline | in progress |
| Sprint 2 | 3-4 | intake, shelving and pickup working end to end | not started |
| Sprint 3 | 5-6 | notifications, exceptions, stocktake, billing, progress review | not started |
| Sprint 4 | 7-8 | full test pass, report, presentation, logbooks | not started |

See `docs/05-management/plans/PLAN.md` for the task breakdown and
`CONTRIBUTING.md` for the branch and commit rules.
