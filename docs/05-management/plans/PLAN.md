# Project plan

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

Frontend pages are split by role: resident pages (R5), staff pages (R5),
admin pages (R5), all reviewed by R4.

## Sprint 2, weeks 3 to 4

The goal is that a staff member can record a parcel, the system suggests a
storage location, the resident receives a pickup code, and the staff member
hands the parcel over. Everything else waits.

| ID | Task | Owner | Folder |
|---|---|---|---|
| T-30-01 | Docker Compose with PostgreSQL, Redis, backend and frontend | R10 | repository root |
| T-30-02 | Spring Boot skeleton: controller / service / repository / entity / dto, unified error handling | R6 | backend/src/main/java/com/linyi/common |
| T-30-03 | Flyway migrations and seed data | R7 | backend/src/main/resources/db/migration |
| T-30-04 | Frontend skeleton: Vite, router, Pinia, axios wrapper | R4 | frontend/src |
| T-30-05 | Checkstyle, Spotless, commitlint, Git hooks | R10 | repository root |
| T-30-06 | CI runs `./mvnw verify` and frontend tests | R10 | .github/workflows |
| T-41-01 | Registration, login, JWT, four-role RBAC | R6 | backend/src/main/java/com/linyi/auth |
| T-41-02 | Login and registration pages, route guards | R5 | frontend/src/views |
| T-41-03 | Audit log interceptor | R7 | backend/src/main/java/com/linyi/common |
| T-41-04 | Unit tests for auth and RBAC | R8 | backend/src/test/java/com/linyi |
| T-42-01 | Staff records a parcel, carrier recognised from the waybill prefix, duplicate waybills rejected | R7 | backend/src/main/java/com/linyi/parcel |
| T-42-02 | Storage location recommender: size filter, zone priority, turnover score, safe under concurrency | R6 | backend/src/main/java/com/linyi/location |
| T-42-03 | Storage location management: zones, states, manual override | R7 | backend/src/main/java/com/linyi/location |
| T-42-04 | Pickup code issue and handover, one-time use, transaction and unique constraint | R6 | backend/src/main/java/com/linyi/pickup |
| T-42-06 | Location board component with zone filter and full-shelf warning | R5 | frontend/src/components |
| T-42-07 | Staff workbench: intake form, suggested location, handover screen | R5 | frontend/src/views |
| T-42-08 | Unit and integration tests for the intake to handover path | R8 | backend/src/test/java/com/linyi |
| T-42-09 | Front end and back end integration, defect fixes | R5 | frontend and backend |

## Sprint 3, weeks 5 to 6

| ID | Task | Owner | Folder |
|---|---|---|---|
| T-43-01 | Notifications: in-app message plus a pluggable provider interface | R7 | backend/src/main/java/com/linyi/notify |
| T-43-03 | Delegated collection codes: one-time, expiry, revoke | R6 | backend/src/main/java/com/linyi/pickup |
| T-43-04 | Exception parcel tickets: damaged, wrong item, refused, lost | R7 | backend/src/main/java/com/linyi/ticket |
| T-43-05 | Admin pages: collection points, locations, staff, fee rules | R5 | frontend/src/views |
| T-43-12 | Stocktake: task generation, difference recording, reason categories | R7 | backend/src/main/java/com/linyi/stocktake |
| T-43-07 | Second integration test round, clear P0 and P1 defects | R10 | docs/04-testing |
| T-43-08 | Progress review pack: demo script, slides, contributor screenshot, RTM | R1 | docs/05-management |
| T-44-01 | Billing engine: rule table, pure calculation function, cap and waiver | R7 | backend/src/main/java/com/linyi/billing |
| T-44-03 | Operations dashboard: location utilisation, daily intake and pickup volume | R5 | frontend/src/views |
| T-44-04 | Security hardening: token refresh, phone masking, rate limiting | R6 | backend/src/main/java/com/linyi/config |

## Sprint 4, weeks 7 to 8

| ID | Task | Owner | Folder |
|---|---|---|---|
| T-44-06 | End to end tests with Playwright covering the must-have flows | R8 | frontend/tests |
| T-50-01 | System testing across the five test scope dimensions | R8 | docs/04-testing |
| T-50-02 | Performance and concurrency testing | R8 | docs/04-testing |
| T-50-03 | Security testing, including pickup code replay | R8 | docs/04-testing |
| T-50-05 | Test report with testing insight and recommendation entries | R8 | docs/04-testing/reports |
| T-50-09 | Deploy to the cloud, keep a local backup for the demonstration | R10 | docs/05-management |
| T-50-11 | Report chapters | R9 | docs/07-report/chapters |
| T-50-13 | Every member finalises their personal logbook | everyone | docs/06-logbook |
| T-50-14 | Retrospective and lessons learned | R1 | docs/05-management |

## Definition of done

A task is done when:

1. the code or document is committed on a branch that names the task
2. a pull request is open with the task ID in the title
3. one other person has approved it and CI is green
4. the deliverable sits in the folder listed above
5. the person who did it has written a logbook entry with a link
