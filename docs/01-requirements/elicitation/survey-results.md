# Survey Results

## Community Parcel Collection Point Management System

| Item | Detail |
|---|---|
| Task | T-10-01 |
| Owner | R1 (fieldwork), R2 (analysis) |
| Version | v1.0 |
| Status | Complete for Sprint 1 |
| Method | Four online questionnaires, one per user role |
| Collected | Week 1 to Week 2 |

## 1. Method and Response Summary

Four questionnaires were designed, one for each role in the system. They were
distributed to people known to the team: classmates, neighbours, and staff and
owners of local collection points. Responses were collected over four days.

| Role | Questionnaire | Responses collected |
|---|---|---:|
| Resident | Resident Parcel Experience Survey | 11 |
| Staff | Staff Parcel Handling Survey | 7 |
| Owner | Collection Point Owner Survey | 7 |
| System administrator | System Administration Survey | 4 |
| **Total** | | **29** |

### 1.1 How to read these results

Three limits apply, and they are stated here so that no reader, including the
course instructor, mistakes the numbers for a representative study.

1. **Convenience sampling.** Respondents were people the team already knew or
   could approach easily. This is not a random or stratified sample. The
   results describe the people who answered, not the population of parcel
   collection point users.
2. **Small sub-samples.** The staff, owner and administrator groups each have
   fewer than ten responses. In a group of seven, moving one respondent from
   one answer to another shifts the proportion by roughly 14 percentage points.
   For this reason the findings below are stated as "most", "about half" or "a
   minority" rather than as precise percentages, and percentages are given only
   as counts of the responses actually received.
3. **The administrator group is directional only.** Four responses cannot
   support a conclusion on their own. The administrator findings are labelled
   as indicative and are used only to support decisions already taken on other
   grounds.

### 1.2 What may and may not be claimed

- **May be claimed:** "among the 11 residents who responded, most use a phone",
  "the staff respondents ranked batch registration as their main source of delay".
- **May not be claimed:** "82 per cent of residents use mobile", "the survey
  shows that residents want notification within five minutes" as though the
  sample represented the wider population.

The report and the SRS use the first form throughout.

## 2. Resident Findings (n = 11)

| Question | Responses | What it tells us |
|---|---|---|
| R1. How often do you receive parcels? | 0 per week; a few times a month 5; monthly 3; rarely 2; never 1 | Occasional rather than frequent use. The interface has to be self-explanatory for someone who uses it a few times a month. |
| R2. Main device used | Phone 9; tablet 1; shared household device 1; desktop 0 | Mobile first is a clear finding. A desktop layout only has to remain usable. |
| R3. Biggest problem | Notification arrives late or not at all 4; parcel hard to find 3; do not know where it is stored 2; queues 2; collected by the wrong person 0 | Notification and finding the parcel are the two problems that matter. "Hard to find" and "do not know where it is" are two sides of one problem and are treated as location visibility below. |
| R4. How quickly should the arrival notice arrive? | Within 5 minutes 6; within 1 hour 3; same day 2; not important 0 | 9 of 11 expect the notice within an hour, and 6 of those within five minutes. No respondent said it does not matter. |
| R5. Most important function | In-system notification 4; parcel status 3; storage location 2; pickup code 2; delegated collection code 0 | Notification and status together account for 7 of 11. No respondent chose the delegated collection code. |
| R6. Acceptable time to collect a parcel | 3 to 5 minutes 5; 5 to 10 minutes 4; 1 to 2 minutes 2; more than 10 minutes 0 | All 11 accept a collection that takes up to ten minutes, and about half expect three to five minutes. This sets the target for the pickup journey. |

**Reading of the resident data.** The two dominant needs are being told that a
parcel has arrived, quickly, and being able to see where it is stored. The
delegated collection code, which the current SRS lists in scope, was chosen by
nobody and should be reduced in priority.

## 3. Staff Findings (n = 7)

| Question | Responses | What it tells us |
|---|---|---|
| S1. Parcels handled on a typical day | 20 to 50: 4; under 20: 1; 100 to 200: 1; over 200: 1 | Most of the points represented are small. The system does not need to be designed for a warehouse. |
| S2. How parcels are recorded now | Spreadsheet 3; existing software 2; chat history 2; paper 0 | None of the respondents still rely on paper alone. They already use digital tools, so moving to this system is not a large change of habit. |
| S3. Main source of delay | **Batch registration 4**; notification 1; verifying the collector 1; damaged or wrong parcels 1; **searching the shelves 0** | Batch registration is the single largest delay. Searching the shelves, which the SRS currently treats as a major problem, was chosen by nobody. |
| S4. Most important function | Pickup code verification 3; handover confirmation 3; batch registration 1; **location recommendation 0** | Verification and a reliable handover record are what staff want. Location recommendation was chosen by nobody. |
| S5. How should locations be suggested? | Always suggest 2; suggest but let me confirm 1; manual only 2; do not suggest 2 | Opinion is split, and more than half prefer to keep control or avoid suggestions entirely. A suggestion may be offered but must never be applied automatically. |
| S6. Acceptable time for one handover | Up to 1 minute 2; 1 to 2 minutes 2; 2 to 3 minutes 2; more than 3 minutes 1 | Most accept a handover of up to three minutes. This sets the operational target. |

**Reading of the staff data.** Batch registration is the real bottleneck, not
shelf searching. The features staff value are pickup code verification and a
reliable handover record. Location recommendation is not a priority and must
be delivered as an optional suggestion the staff member can override.

## 4. Owner Findings (n = 7)

| Question | Responses | What it tells us |
|---|---|---|
| O1. How many collection points do you manage? | 2 to 3: 3; 4 to 5: 2; 1: 1; 6 to 10: 1 | 6 of 7 manage more than one point. Multi-point support is justified by the data, not assumed. |
| O2. How many staff do you manage? | 6 to 10: 2; 11 to 20: 2; remaining answers 1 each | There is a real need to manage staff across more than one point. |
| O3. Most important report | **Overdue parcels 6**; collected parcels 1; others 0 | Overdue parcels is the dominant requirement, chosen by 6 of 7. |
| O4. Compare across points | Must have 4; nice to have 2; not sure 1; not needed 0 | Cross-point comparison is wanted by most, but it can follow the single-point view. |
| O5. Most important permission | View reports 4; manage staff 2; view audit log 1 | The owner role is about seeing figures and managing people, not about configuring the system. |
| O6. How should the audit log be presented? | **Grouped by parcel or storage location 4**; by who acted 2; by time 1 | The audit log should be searchable by the object involved, not presented as a flat chronological list. |

**Reading of the owner data.** The overdue parcel report is the top
requirement by a wide margin. Multi-point operation is supported. The owner
role should not be given configuration duties that belong to the
administrator.

## 5. Administrator Findings (n = 4, indicative only)

| Question | Responses |
|---|---|
| A1. Expected number of collection points | 6 to 10: 2; 2 to 5: 1; more than 50: 1 |
| A2. How should accounts be created? | **Self registration with approval 3**; mixed 1 |
| A3. Most important administration function | User management 2; collection point management 2 |
| A4. Most important security control | **RBAC 2**; JWT session 1; phone number masking 1 |
| A5. How should data be separated between points? | **Shared database with a site identifier 3**; separate schema 1 |
| A6. Which settings should be configurable per point? | Retention period 2; notification rules 1; location layout 1; **fee and overdue rules 0** |

**Reading of the administrator data.** With four responses these results cannot
stand alone. A5 is nonetheless useful: it agrees with the architecture already
recorded in ADR-002, so the existing decision is not contradicted by the people
who would operate it. A6 shows that fee configuration was chosen by nobody,
which supports moving billing out of the must-have scope.

## 6. Conclusions Carried Into the Requirements

| # | Conclusion | Evidence | Effect on the SRS |
|---|---|---|---|
| 1 | Keep four login roles: resident, staff, owner, administrator. A courier does not need an account. | No respondent in any group described a courier logging in; couriers deliver to the counter. | Confirm Section 2.3 and the Out of Scope entry OS-2. |
| 2 | Multi-point operation is required. | Owners: 6 of 7 manage more than one point; 6 of 7 want cross-point comparison. Administrators: 3 of 4 chose a shared database with a site identifier. | Keep the multi-point data model. Single-point views are must-have, cross-point comparison is should-have. |
| 3 | Batch registration is the highest priority staff function. | Staff S3: 4 of 7 named batch registration as the main delay. | Raise batch registration from should-have to must-have, and make it the first staff feature built. |
| 4 | Pickup code verification and a reliable handover record are the next priority. | Staff S4: 3 and 3 of 7 respectively. Owners O6: the audit log must be traceable by parcel. | Keep verification and handover as must-have. Change the audit log presentation to be grouped by parcel or storage location. |
| 5 | Timely notification is the highest priority resident function. | Resident R3: 4 of 11 named late or missing notification as the biggest problem. R4: 9 of 11 expect notice within an hour. R5: notification was the most chosen function. | Keep notification as must-have. Set the notification delay target from R4. |
| 6 | Parcel status and storage location visibility matter almost as much as notification. | Resident R5: status 3, location 2 of 11. R3: combined location complaints 5 of 11. | Keep status and location visibility as must-have. |
| 7 | Location recommendation is not a priority and must not be automatic. | Staff S4: 0 of 7 chose it. S5: 4 of 7 preferred manual control or no suggestion. | Reduce to could-have or should-have. Keep the manual override. Do not present it as a headline feature. |
| 8 | Delegated collection is not wanted by residents. | Resident R5: 0 of 11 chose the delegated collection code. | Move from must-have in scope to future work, or keep as a low priority. |
| 9 | Overdue fee calculation is not wanted by administrators. | Administrator A6: 0 of 4 chose fee configuration. | Move billing out of the must-have scope, or reduce to a display of days held. |
| 10 | The system must work on a phone first. | Resident R2: 9 of 11 use a phone. | Add a mobile-first requirement. Desktop only has to remain usable. |
| 11 | The interface must suit occasional users. | Resident R1: most respondents receive parcels monthly or less often. | Reinforce the usability requirement: no training, few steps. |

## 7. Resulting Non-Functional Targets

These replace the earlier assumption-based targets.

| Item | Target | Source |
|---|---|---|
| Arrival notification delay | Within 5 minutes as the design target; within 1 hour as the acceptable limit | Resident R4: 6 of 11 wanted 5 minutes, 9 of 11 wanted within an hour |
| Collection journey, resident side | 3 to 5 minutes as the target; 10 minutes as the acceptable limit | Resident R6 |
| Single handover, staff side | Up to 3 minutes | Staff S6: 6 of 7 accepted up to 3 minutes |
| Primary security control | RBAC, supported by JWT session handling and phone number masking | Administrator A4 |
| Data separation between collection points | Shared database with a site identifier | Administrator A5, and consistent with ADR-002 |
| Mobile support | Usable from 375 px width; primary design target is a phone screen | Resident R2: 9 of 11 |

## 8. Questions the Survey Did Not Answer

The survey was not designed to answer everything. The following remain open
and are recorded in the SRS as open issues.

| Item | Why it is still open | Proposed way to close it |
|---|---|---|
| Retention period in days before a parcel is treated as overdue | No question asked for a number, and the administrator group did not choose fee configuration. | Adopt a configurable default of three days and state it as an assumption. |
| Whether owners in small points also act as staff | Two owners reported managing only one point, where the roles are likely to overlap. | Treat the roles as combinable and record it as a decision. |
| The exact identification fallback when a resident has lost the pickup code | Residents were not asked, and staff answers were split between code and identity checks. | Define a documented fallback: phone number plus surname, recorded as an alternative verification method. |

## Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v1.0 | Week 1 to Week 2 | Initial results: method and response summary, the four role sections, the conclusions carried into the requirements, the derived targets in section 7 and the limitations in section 1.2. `CR-008` is the change record that named this file | R1, R2 |
