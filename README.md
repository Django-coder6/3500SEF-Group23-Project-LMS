# - Community Parcel Collection Point Management System

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
| backend | Express + Sequelize service | R6, R7 |
| frontend | Vue 3 + Vite client | R4, R5 |

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

## Getting started

This section is filled in during Sprint 2 (week 7) once the backend and
frontend skeletons exist.

```bash
git clone https://github.com/<user>/<repo>.git
cd <repo>
cd backend && npm install && cd ..
cd frontend && npm install && cd ..
cp backend/.env.example backend/.env
docker compose up -d
```

## Sprint status

| Sprint | Weeks | Goal | Status |
|---|---|---|---|
| Sprint 0 | 1-2 | topic, repository, user research, competitor review | in progress |
| Sprint 1 | 3-6 | requirements and design baseline | not started |
| Sprint 2 | 7-9 | intake, shelving and pickup working end to end | not started |
| Sprint 3 | 10-12 | notifications, exceptions, stocktake, admin, progress review | not started |
| Sprint 4 | 13-14 | billing, reports, audit, full test pass | not started |
