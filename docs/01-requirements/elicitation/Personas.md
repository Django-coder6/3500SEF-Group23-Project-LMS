# Personas

## Community Parcel Collection Point Management System

| Item | Detail |
|---|---|
| Task | T-10-04 |
| Owner | R2 |
| Version | v1.2 |
| Status | Complete for Sprint 1 |
| Source | Derived from the four role questionnaires, see [survey results](survey-results.md) |
| Method | Composite personas, identified by role only. Each persona combines the answer patterns that appeared in the corresponding questionnaire group. |

> These personas are composites, not real individuals. **They are identified by
> role and carry no personal name**, because the ethics statement requires that
> no respondent be named in any project document. The age, occupation and living
> situation are illustrative; every goal and pain point below is traceable to at
> least one answer in the questionnaire for that role. Where the questionnaire
> and an earlier draft of this document disagreed, the questionnaire was used.
>
> **Project terminology:** collection point, storage location, shelving,
> handover, pickup code.

## Overview

| Identifier | System role | Survey base | Primary goal |
|---|---|---|---|
| Resident persona | Resident | 11 responses | Know quickly that a parcel has arrived, and where it is |
| Staff persona | Staff | 7 responses | Register a batch fast, and hand over with a reliable record |
| Owner persona | Owner | 7 responses | See overdue parcels and staff activity across all owned points |
| System administrator persona | System administrator | 4 responses | Keep accounts, access control and point configuration under control |

---

## Persona 1: Resident persona

| Field | Detail |
|---|---|
| Identifier | Resident persona |
| Age | 34 |
| Occupation | Office worker in the city |
| Living situation | Apartment block with one collection point at the entrance |
| Tech comfort | High. Uses a phone for almost everything. |
| Receives parcels | A few times a month, not weekly |

> *"I do not mind collecting it. I mind not knowing that it has arrived, and not
> knowing where it is when I get there."*

### What the survey tells us about this persona

| Finding | Responses |
|---|---|
| She uses a phone, not a desktop | 9 of 11 |
| She receives parcels monthly or less often | 6 of 11 |
| Her biggest problem is late or missing notification | 4 of 11 |
| She expects notice within an hour, and ideally within five minutes | 9 of 11, of which 6 wanted five minutes |
| Her most chosen function is in-system notification | 4 of 11 |
| She accepts a collection journey of up to ten minutes | 11 of 11 |

### Daily scenario

The resident persona orders online two or three times a month. On the delivery day she has no
idea whether the parcel has reached the collection point. Sometimes she gets a
message the next day, and it does not say where the parcel is stored. She goes
to the point after work, joins a short queue, and waits while staff look for her
parcel.

### Goals

- Receive a clear notice as soon as the parcel is shelved, ideally within five
  minutes.
- Be able to see, before leaving home, the status of every parcel addressed to
  her.
- Spend no more than three to five minutes at the counter.
- Not have to learn an interface she uses only a few times a month.

### Pain points

- Notices arrive late, or never.
- She does not know where the parcel is stored, so she cannot judge whether it
  is worth walking over.
- Queueing at peak times, although in this sample queueing was one of the least
  chosen problems. Only "collected by the wrong person", with no responses, was
  chosen less often.
- The current interface is not designed for a phone.

### Out of character

The resident persona did **not** ask for a delegated collection code. In the survey nobody
chose it. The team has therefore reduced its priority rather than assume she
needs it.

---

## Persona 2: Staff persona

| Field | Detail |
|---|---|
| Identifier | Staff persona |
| Age | 27 |
| Occupation | Full-time staff member at a community collection point |
| Work situation | Serves a small residential point, a few dozen parcels on a typical day, with occasional large batches |
| Tech comfort | Medium. Comfortable with a phone, intolerant of slow software. |

> *"When a courier drops off a whole trolley at once, I need to get them
> registered and on the shelf without losing one, and without standing at the
> computer for an hour."*

### What the survey tells us about this persona

| Finding | Responses |
|---|---|
| Batch registration is his main source of delay | 4 of 7 |
| Searching the shelves was **not** named as a delay by anyone | 0 of 7 |
| His most important functions are pickup code verification and handover confirmation | 3 and 3 of 7 |
| Location recommendation was chosen by nobody | 0 of 7 |
| He wants to keep manual control of the location, or does not want suggestions | 4 of 7 |
| He accepts up to three minutes per handover | 6 of 7 |
| He already records parcels digitally, using a spreadsheet, existing software or chat | 7 of 7 |

### Daily scenario

The staff persona opens the point in the morning. A courier arrives with a trolley and
The staff persona works through it on the counter, entering each parcel and putting it on
a shelf. In the evening residents come in one after another. He takes a pickup
code, fetches the parcel and completes the handover. When something is disputed,
he has no dependable record of who handled the parcel.

### Goals

- Register a courier batch in one operation rather than one parcel at a time.
- Verify the collector and record the handover accurately and quickly.
- Be able to find the answer to "is my parcel here?" without leaving the
  counter.
- Keep control over where a parcel actually goes, because he knows the shelves.

### Pain points

- Registering a large batch parcel by parcel is the slowest part of the day.
- A disputed handover cannot be resolved because the record is incomplete.
- Being asked to change his way of working by software that does not match the
  shape of the counter.

### Out of character

The staff persona did **not** ask the system to choose the storage location. The
recommendation feature is therefore delivered as a suggestion he can accept or
ignore, never as an automatic assignment.

---

## Persona 3: Owner persona

| Field | Detail |
|---|---|
| Identifier | Owner persona |
| Age | 45 |
| Occupation | Runs a small retail business and operates collection points alongside it |
| Business situation | Manages more than one collection point, and six or more staff across them |
| Tech comfort | Medium. Uses a phone for messaging and payment, prefers short screens with clear numbers. |

> *"I do not need a dashboard full of charts. I need to know what is stuck on my
> shelves and whether my staff are handling things properly."*

### What the survey tells us about this persona

| Finding | Responses |
|---|---|
| She manages more than one collection point | 6 of 7 |
| Her most important report is overdue parcels | 6 of 7 |
| She wants to compare across points | 6 of 7 |
| Her most important permission is viewing reports | 4 of 7 |
| She wants the audit log grouped by parcel or storage location, not by time | 4 of 7 |
| She manages six or more staff | 5 of 7 |

### Daily scenario

The owner persona spends most of the day in her shop and visits each collection point
briefly. She earns a handling fee per parcel, so throughput and complaint rates
matter. At the end of the day she asks staff for the numbers and writes them
down herself. She cannot quickly say how many parcels have been sitting for
several days, or which staff member handled a parcel that a resident is
disputing.

### Goals

- See overdue parcels at a glance, across every point she owns.
- Trace a disputed parcel to the staff member and the time it was handled.
- Compare the activity of her points without collecting numbers by hand.
- Manage staff accounts for her own points.

### Pain points

- Daily figures are assembled by hand and are always a little out of date.
- Overdue parcels occupy shelf space, and nobody follows them up
  systematically.
- A lost-parcel claim cannot be answered with evidence.
- Staff turnover means she has to keep setting up accounts.

### Out of character

The owner persona was not asked for, and did not ask for, control over fee or overdue rules.
Nobody in the survey chose fee configuration. Those settings are therefore kept
as an administrator responsibility, with a per-point default.

---

## Persona 4: System administrator persona

| Field | Detail |
|---|---|
| Identifier | System administrator persona |
| Age | 31 |
| Occupation | Part-time technical support for the collection point operator |
| Situation | Maintains accounts, access control and configuration for a small group of collection points |
| Tech comfort | High |

> *"My job is to keep the accounts and the access rules straight, and to make
> sure that when someone leaves, their access goes with them."*

### What the survey tells us about this persona

The administrator group returned only four responses, so these findings are
indicative and are used only where they agree with decisions already taken.

| Finding | Responses |
|---|---|
| Accounts should be self-registered and then approved | 3 of 4 |
| The most important administration functions are user and collection point management | 2 and 2 of 4 |
| The most important security control is role-based access control | 2 of 4 |
| Data should be separated between points by a site identifier in a shared database | 3 of 4 |
| Fee and overdue rule configuration was chosen by nobody | 0 of 4 |

### Daily scenario

The system administrator persona sets up a new collection point, creates the owner account, and adds the
first staff accounts. When somebody leaves, the system administrator persona disables the account rather
than deleting it, so that historical records keep their author. That persona also
handles enquiries when a resident disputes a handover and the point has to show
what happened.

### Goals

- Create, approve, disable and delete accounts for all roles.
- Set up a new collection point, including its zones and storage locations.
- Keep data for different points separated while running one deployment.
- Rely on role-based access control as the primary defence.

### Pain points

- Account lifecycles are handled informally, so leaving staff keep access.
- Setting up a new point means entering many storage locations by hand.
- Investigation across points is difficult when data is not clearly separated.

### Out of character

The system administrator persona was not asked to design the interface for others, and takes no part in the
day-to-day parcel workflow.

---

## How these personas are used

| Use | Where |
|---|---|
| Deciding which requirements are must-have | SRS Chapter 3, MoSCoW priorities |
| Naming the actors of the use case and sequence diagrams | `docs/02-design/uml/`; the diagrams use these identical role identifiers |
| Writing test scenarios in the language of the user | `docs/04-testing/` |
| Justifying scope reductions in the report | Report Chapter 3 |

The persona that most often decides a requirement is the resident persona,
because the resident group had the largest number of responses. Where a
decision would affect staff time, the staff persona takes precedence, because
staff are the users who will spend the most time in the system.

## Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v1.2 | Week 2 | Current content: composite personas for the four roles, identified by role only. Revisions before this one were made before the file kept a revision history, so they are not listed; `CR-008` and `CR-009` are the change records that named this file | R2 |
