# UML Sources

- Status: Draft
- Owners: R4 Frontend Lead and Product Architect A, R6 Backend Lead
- Task: T-10-06, to be confirmed with R1, see the pull request
- Scope: Sprint 1 design baseline

Diagram sources are kept as text so that a change to a diagram shows up as a
readable diff in a pull request, as CONTRIBUTING.md requires. Nothing in this
folder is an exported image.

| File | Diagram title | Scope |
|---|---|---|
| `usecase-counter-work.puml` | Use Case Diagram(Collection Point Counter Work) | Staff and resident daily flows, plus the courier as an external actor |
| `usecase-oversight-and-administration.puml` | Use Case Diagram(Oversight And Administration) | Owner oversight and administrator functions, plus registration |
| `class-parcel-and-location.puml` | Class Diagram(Parcel and Storage Location) | Domain classes and enums, matching `docs/02-design/database/ERD.md` |
| `sequence-staff-intake.puml` | Sequence Diagram(Staff Intake And Shelving) | `POST /parcels`, `POST /locations/recommend`, `PATCH /parcels/{id}/status` |
| `sequence-resident-pickup.puml` | Sequence Diagram(Resident Pickup Handover) | `POST /pickup/verify`, `POST /pickup/confirm` |
| `sequence-login-role-guard.puml` | Sequence Diagram(Login And Role Guard) | `POST /auth/login`, then the frontend route guard |

Every endpoint named above exists in `docs/03-api/openapi.yaml`. No endpoint was
invented for a diagram.

## Why the use case model is two diagrams

A single use case diagram carrying five actors and twenty use cases rendered as
a very tall picture with lines crossing the whole width, which is hard to read
on screen and impossible to read when printed. It is split along the line that
already separates the two kinds of user:

- **counter work**, meaning the people standing at the collection point while a
  parcel arrives or is collected;
- **oversight and administration**, meaning the people who are not at the
  counter while the daily work happens.

"Log in" appears in both, which is normal and deliberate: each diagram has to
stand on its own.

## Not repeated here

Two diagrams that a design baseline usually carries already have a home
elsewhere, and are not copied into this folder so that the two copies cannot
drift apart:

| Diagram | Where it lives | Owner |
|---|---|---|
| ER Diagram(Collection Point Database) | `docs/02-design/database/ERD.md` | R6, R7 |
| State Diagram(Parcel Lifecycle), plus the Location, PickupCode and Exception state machines | `docs/02-design/backend-state-machines.md` | R6 |

If the team later wants a rendered state diagram as a `.puml` source, it should
be generated from `backend-state-machines.md`, and that file should stay the
single source of truth for the transitions.

## How to render

Any of these works; all of them read the same `.puml` files.

1. **VS Code, PlantUML extension.** Open a `.puml` file and press `Alt` + `D`.
   This is the fastest route while working.
2. **Command line**, when a rendered file is needed for the report:

   ```bash
   java -jar plantuml.jar -tsvg docs/02-design/uml/*.puml
   ```

3. **PlantUML web server**, by pasting the contents of a file into
   plantuml.com.

Exported images used in the report go to `docs/07-report/figures/`, and each one
is numbered by chapter and referred to at least once in the body text.

## Conventions applied

- Diagram titles state the business scope in brackets and carry no sequence
  number, as CONTRIBUTING.md requires.
- File names are lower case with hyphens.
- Actors follow the SRS role names: Resident, Staff, Owner, System
  Administrator. The courier is drawn outside the system boundary because it is
  an external actor with no account.