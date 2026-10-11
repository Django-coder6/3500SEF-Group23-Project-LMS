# R3 — Synthetic Interview Synthesis for SRS and Glossary


## Assumed stakeholder contexts

| Stakeholder | Scenario | Illustrative needs |
|---|---|---|
| Resident | 3–5 parcels per week; evening pickup | Accurate ready status, fast retrieval, secure delegated pickup |
| Collection point staff | About 150–200 daily parcels, peak-hour pressure | Barcode intake, correct storage location, quick lookup, exception tracking |
| Collection point owner | Three-person shop; adoption cost sensitivity | Low-cost browser-based operation, accountability, basic reports |

## Proposed candidate requirements for team review

| ID (provisional) | Candidate requirement | Priority suggestion | Scenario source | Validation |
|---|---|---|---|---|
| CR-01 | Staff can register parcels on arrival via identifier/barcode | Must | Staff/owner synthetic interviews | Unvalidated |
| CR-02 | Each stored parcel has a searchable storage location | Must | All three synthetic interviews | Unvalidated |
| CR-03 | Staff can find parcels using a pickup code or tracked identifier | Must | Resident/staff synthetic interviews | Unvalidated |
| CR-04 | The system records handover confirmation including timestamp and responsible account | Must | Staff/owner synthetic interviews | Unvalidated |
| CR-05 | Parcel status supports intake, shelving, ready, handover and exception states | Must | Staff/owner synthetic interviews | Unvalidated |
| CR-06 | Proxy pickup could use one-time time-limited authorization | Could | Resident/staff synthetic interviews | Requires privacy/security review |
| CR-07 | Staff can log and resolve missing/misplaced/damaged parcel exceptions | Should | Staff/owner synthetic interviews | Unvalidated |
| CR-08 | UI works on existing desktop/mobile browsers without proprietary locker hardware | Should | Owner synthetic interview | Unvalidated |
| CR-09 | Basic volume/operations reporting | Could | Owner synthetic interview | Unvalidated |

Priorities above are **suggestions**, not approved commitments. Reconcile against the group's existing SRS/backlog before adding IDs or changing scope.

## Glossary suggestions

| English term | Working definition |
|---|---|
| Collection point | Staffed location receiving, storing and handing over parcels |
| Storage location | A shelf or designated physical position within a collection point |
| Parcel intake | Registering an arriving parcel into the system |
| Shelving | Placing a registered parcel into its assigned storage location |
| Parcel lookup | Retrieving the record and location of a parcel by an allowed identifier |
| Handover confirmation | Recording that the parcel was given to an authorized collector |
| Proxy pickup authorization | A permission allowing another person to collect a parcel for the recipient |
| Exception parcel | Parcel requiring special handling due to mismatch, damage, loss or another irregularity |
| Operation log / audit trail | Time-ordered record of significant actions and the responsible user |
| Parcel lifecycle | The status transitions from intake through storage to handover or exception |

**Terminology constraint:** Use **collection point**, **storage location**, **shelving** and **handover confirmation**, not smart-locker compartments for this project's staffed-station model.

## Follow-up validation

The project brief separately requires one genuine resident interview, one genuine staff interview and one genuine owner interview (about 15 minutes each), plus two questionnaires with at least 20 real responses each. These fictional transcripts and synthetic survey CSVs **do not satisfy that evidence requirement**.
