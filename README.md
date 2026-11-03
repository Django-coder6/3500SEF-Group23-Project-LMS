# Community Parcel Collection Point Management System

COMP 3500SEF Software Engineering, group project, 10 members.

The system manages the daily work of a neighbourhood parcel collection point: a
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

| Role ID | Role | Name | Student ID | GitHub |
| :---: | --- | --- | --- | --- |
| R1 | Project Lead / Team Leader | Liu JiaHao | 13662451 | [Django-coder6](https://github.com/Django-coder6) |
| R2 | Product Owner | Chan Yin Cho | 14489395 | [ohcyc246](https://github.com/ohcyc246) |
| R3 | Requirements Documentation Officer / Business Analyst | Li Cheuk Fung | 14320368 | [Kennethli13](https://github.com/Kennethli13) |
| R4 | Frontend Lead + Product Architect A | Yuan Chong Jun | 13705584| [RookieVENENO](https://github.com/RookieVENENO/) |
| R5 | Frontend Developer | Tan Yuan Ting | 13374348 | [Christine049](https://github.com/Christine049) |
| R6 | Backend Lead + Product Architect B | Hu Qing Kai | 13661454 | [123dvfj123](https://github.com/123dvfj123?tab=repositories)|
| R7 | Backend Developer | Huang Wei Jia | 13686319 | [Allen06914](https://github.com/Allen06914)|
| R8 | QA Lead | Wu Kehao | 14612273 | [fannaodawang](https://github.com/fannaodawang) |
| R9 | Documentation & Quality | Guo Yuhui | 14294174 | [RavenG7](https://github.com/RavenG7) |
| R10 | Infrastructure & Integration DevOps | Chan Sung Ming | 14480597 | [smcheese9731-sys](https://github.com/smcheese9731-sys) |


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

```bash
git clone https://github.com/Django-coder6/3500SEF-Group23-Project-LMS.git
cd 3500SEF-Group23-Project-LMS

# backend (Java 17 and the Maven wrapper, delivered by T-30-02 / issue #137)
cd backend
./mvnw clean install        # on Windows use: mvnw.cmd clean install
cd ..

# frontend
cd frontend
npm install
cd ..

# build environment variable
cp .env.example .env

# Start the database, the cache and the frontend.
# The backend image is built from backend/ (T-30-02, issue #137) and sits behind
# the "full" profile because building it takes a few minutes.
docker compose up -d

# Add the backend image when you need the whole stack:
docker compose --profile full up -d
```


| Service | URL |
|---|---|
| Frontend | http://localhost:80 |
| Backend health | http://localhost:8080/api/health |
| PostgreSQL | localhost:5432 |
| Redis | localhost:6379 |


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
