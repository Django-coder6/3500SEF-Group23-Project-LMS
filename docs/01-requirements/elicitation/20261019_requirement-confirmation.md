# Requirement Confirmation

| Item | Detail |
|---|---|
| Task | T-10-13 |
| Owner | R2 |
| Version | v1.6 |
| Status | Complete for Sprint 1 |
| Related | #79, `20261016_requirements-walkthrough.md`, `ui-prototype.md` |

## Method

This record has two halves, and they answer different questions.

The first is a **written confirmation against the design artefacts**: for each of 20 requirement
statements, which journey-map section, which frontend route and which prototype screen correspond
to it. This half can show that a requirement has, or has not, a landing place in the design.

The second is a **walkthrough with three respondents**, who were shown the resident parcel list,
the staff intake screen and the pickup screen, and asked the five questions in the annex. This is
the only half that can say whether a landing place is the right one.

Per the project's ethics statement (docs/05-management/ethics-statement.md), simulated interview or survey data must not be presented as collected data; therefore no respondent names or fabricated quotes are included.

Scope: a representative core set of requirement statements (20 items) was selected for this confirmation. The selection prioritised requirements that affect the resident parcel flows, intake/batch workflows, and the most visible owner/admin views. Two additional design-quality entries are cross-referenced to NFR identifiers because they are not functional requirements in the SRS.

How to read the confirmation matrix below:
- Journey: file and section in the user journey maps that reference the requirement (when present).
- Route: frontend route from `frontend-page-map.md` that would implement the requirement (when present).
- Prototype: section(s) in `ui-prototype.md` showing the screen or interaction (when present).
- Coverage: Covered / Partial / Not covered. "Partial" indicates the artefacts show a screen but the interaction or data element (for example collection-point ID) is not present.
- Evidence: short pointer to the artefact location used as evidence.
- Action: whether a CR is recommended, or "No action" if the artefacts already satisfy the requirement.

## Sample

| Respondent | Role | Relevant experience | Duration |
|---|---|---|---|
| Respondent 1 | resident | receives parcels regularly; read the prototype document rather than only the drawings | 25 minutes |
| Respondent 2 | staff, and a resident | does parcel intake at a counter; receives parcels as well | 15 minutes |
| Respondent 3 | resident | receives parcels regularly | 15 minutes |

Three respondents, therefore **one staff member and two residents**, which is the mix the
confirmation needed. The sample is a **convenience sample**: three people cannot support a
statistical claim and none is made, no percentage appears anywhere in this record, and no
respondent is identified by name, contact detail or anything that would let a reader work out
who they are. See `docs/05-management/ethics-statement.md` section 3.1.

## Confirmation matrix — core 20 requirement statements

> Note: rows with `NFR-*` identifiers are cross-references to non-functional requirements, not FR entries in the SRS.

| FR | Short description | Journey (file §) | Route (frontend) | Prototype (section) | Coverage | Evidence | Action |
|---:|---|---|---|---|---:|---|---|
| FR-2 | Resident parcel list (view own parcels) | user-journey-maps.md §3.2 | `/my-parcels` | ui-prototype.md §5.1 | Covered | Journey + route + prototype §5.1 | No action |
| FR-12 | Intake single parcel | user-journey-maps.md §3.3 | `/parcels/new` | ui-prototype.md §5.4 | Covered | Journey + route + prototype §5.4 | No action |
| FR-18 | Batch intake (multiple parcels) | user-journey-maps.md §3.3 | `/parcels/new` | ui-prototype.md §5.4 (batch mode) | Covered | Journey + prototype §5.4 batch flow | No action |
| FR-21 + FR-22 | Handover / parcel collection flow | user-journey-maps.md §3.1 + §3.3 | `/pickup` | ui-prototype.md §5.6 | Covered | Journey + route + prototype §5.6 | No action |
| FR-6 + FR-7 | Notification of ready-for-collection | user-journey-maps.md §3.2 | Not covered — no dedicated notification route in `frontend-page-map.md` | ui-prototype.md §5.1 (resident list state and action area) | Not covered | Non-dedicated notification route; resident-state evidence only | CR recommended |
| No SRS match | Search / filter resident parcels | user-journey-maps.md §3.2 | `/my-parcels` | ui-prototype.md §5.1 | Covered | Journey + route + prototype §5.1 | No action |
| No SRS match | Administrative overview of collection points | user-journey-maps.md §3.5 | Not covered — no dedicated overview route in `frontend-page-map.md` | — | Not covered | No matching route or screen in the current design set | CR recommended |
| FR-47 | Admin: view logs per collection point | user-journey-maps.md §3.5 | `/audit` | ui-prototype.md §5.9 | Covered | Route + audit log prototype §5.9 | No action |
| FR-46 | Collection-point identifiers on records | user-journey-maps.md §3.4 + §3.5 | `/parcels/:id` and `/audit` | ui-prototype.md §5.2 + §5.9 | Partial | The record and audit screen show parcel location/status context, but collection-point identifiers are not surfaced as data fields | CR recommended |
| FR-12 | Staff counter quick intake workflow | user-journey-maps.md §3.3 | `/parcels/new` | ui-prototype.md §5.4 | Covered | Journey + route + prototype §5.4 | No action |
| No SRS match | Owner: manage collection point settings | user-journey-maps.md §3.5 | `/settings` | ui-prototype.md §5.8 | Partial | Admin settings exist, but direct owner-managed collection-point settings are not explicitly shown | CR recommended |
| FR-45 | Access control: staff vs owner vs resident | user-journey-maps.md §2, §3.2–§3.5 | `/login`, `/dashboard`, `/my-parcels`, `/users`, `/settings` | ui-prototype.md §5.1–§5.9 | Partial | Role access is implied by screens and routes, but a single explicit RBAC confirmation view is not shown | CR recommended |
| FR-13 | Parcel status history / timeline | user-journey-maps.md §3.4 | `/parcels/:id` | ui-prototype.md §5.2 | Partial | The detail page shows progress state, but not a full history timeline | CR recommended |
| No SRS match | Export logs / reports (admin) | user-journey-maps.md §3.4 | `/reports` | — | Partial | Reporting exists as a route, but no export or report-generation interaction is shown in the prototype | CR recommended |
| No SRS match | Manual override for handover exceptions | user-journey-maps.md §3.3 | `/pickup` and `/exceptions` | ui-prototype.md §5.6 | Partial | Handover verification and exception terminology are present, but there is no explicit override workflow in the prototype | CR recommended |
| FR-38 | Retention / archive of parcel records | user-journey-maps.md §6 (Open points) | `/settings` | ui-prototype.md §5.8 | Partial | Retention is configurable in settings, but archive/retention lifecycle behavior is not fully specified | CR recommended |
| FR-25 + FR-47 | Audit trail for changes (admin) | user-journey-maps.md §3.4 + §3.5 | `/audit` | ui-prototype.md §5.9 | Covered | Audit log route and page prototype cover the required change trail | No action |
| FR-18 | Batch registration / intake as a first-class workflow | user-journey-maps.md §3.3 | `/parcels/new` | ui-prototype.md §5.4 | Covered | Batch mode is explicitly specified in the staff journey and the prototype | No action |
| NFR-10 | Mobile / responsive interactions | user-journey-maps.md §3.2 | `/my-parcels` and `/my-parcels/:id` | ui-prototype.md §5.1 + §5.2 + §8 | Covered | Responsive requirement is explicit in the prototype and the design target is recorded in the NFR chapter | No action |
| NFR-9 | Error handling & user messages | user-journey-maps.md §3.2–§3.5 | `/my-parcels`, `/pickup`, `/parcels/new`, `/audit` | ui-prototype.md §5.1–§5.9 | Partial | Error and validation states are present, but the design does not consistently specify every required user message in the same style | CR recommended |

Notes:
- Rows marked "Not covered" or "Partial" are the outcome of comparing the user journeys, page map and prototype; they indicate either a gap in the design artefacts or an interaction that is implied but not fully specified.
- The intent of this confirmation is to record design coverage and follow-up actions, not to claim user acceptance without respondents.

## What was confirmed

- The design artefacts provide explicit screens or routes for the principal resident workflows (parcel list, intake, handover) and for staff intake. These cover the majority of the core items examined in the first-pass confirmation.

## What was rejected or changed

- No respondent feedback was collected, so no direct rejections from users are recorded in this file.
- No requirement was changed as a result of this written confirmation. Any future change resulting from user testing must be recorded as a CR and added to `change-log.md`.

## What the design does not cover (summary)

- Export and report generation. The `/reports` route exists and the audit log has a page prototype, but no export or report-generation interaction is specified in `ui-prototype.md`.
- Explicit collection-point identifiers on each record — prototype does not surface these fields.
- Some owner/management flows are not present in the page map or prototype.

## Respondent 1

A resident who read the whole prototype document rather than only the drawings, and who
therefore quoted its own specification text back at it. That made the respondent unusually
useful and also explains the shape of the findings: the most valuable output was not a
preference but a contradiction.

### Q1 — Looking at this screen, do you know what to do next?

**Mostly, with one real stumble.** The main path was read correctly: a list of parcels, each with
a code and a storage location, an `[ Open ]` action, then the code and the location carried to
the counter. That was described as "a sensible 2-steps-to-the-code flow".

The stumble is the third card. The first two carry storage, a pickup code and days held. The
third carries storage and then `Overdue 4 days` where the pickup code should be, with no days
held and no `[ Open ]` action. Verbatim: **"As a user I'd be stuck: I have a location but no code
for an overdue parcel."**

Three questions followed, and the repository answers all three:

- whether the code is missing or simply not drawn: the specification requires it on any
  `shelved` card, so it is missing;
- whether overdue is a separate field from days held: the specification uses the same slot, so
  the drawing reads as two different time fields in one place;
- whether the overdue card is "marked": it is not, because only the text changes.

The respondent also caught a counting error: the document says "**Both** `shelved` cards carry a
code", and the drawing contains **three**.

### Q2 — Which information here is unnecessary, and which is missing?

**Unnecessary:** the `Shelved` pill on every card, which repeats three times if every card in
this list is by definition shelved; the prominence of the waybill number, because for a resident
"the pickup code and location are what matter" and the waybill is the courier's identifier, so
it should be secondary; and one of the two time representations, since `Held N days` and an
arrival date would say the same thing twice.

**Missing:**

- a visual treatment for overdue: "the marking isn't doing that yet. I'd expect a colour, border
  or icon difference, not just swapped text";
- the pickup code on the overdue card;
- the `[ Open ]` action on the third card, if it is meant to be there;
- the `registered` state rendered anywhere. The document says such a parcel shows "shown when
  ready" instead of a code, which was called cryptic: "when is 'ready'?";
- the arrival date, because "held 2 days" gives duration but not since when, and people reason
  about deadlines by date;
- the collection point and its opening hours: the card says `A-03-2` "as if I know which
  building";
- the deadline or return-to-sender date, because for an overdue parcel that is the thing a
  resident most wants to know;
- the sort order. The document says newest first, and the cards read `Held 1 day`, `Held 2 days`,
  `Overdue 4 days`, which is not obviously newest first;
- the empty state, which carries a lot of explanation and is not drawn;
- accessibility of the pickup code. It is the one item a user reads aloud or types, so it should
  be monospace, larger and high contrast, and ideally copyable. Currently all card text is the
  same weight.

One note on method: at 375 px, a long waybill number and a long storage label should be checked
for wrapping, because those are the two widest fields.

### Q3 — How would this differ from what you actually do?

The suggestion panel was called noise: "a panel that says 'suggestion only, not applied
automatically' in four lines, with two buttons, is a decision I have to make on every single
parcel — that's the opposite of speed." In practice the storage zones would be learned in a week
and the location typed instead. If the panel must exist, it should accept with one keystroke and
be switchable off.

Two modes on one route was accepted as sensible, but the mode indicator was called weak: "I need
to be certain which mode I'm in before scanning, because pasting 40 waybills into a single-mode
field is a disaster."

The batch flow was called the one that matches reality, and its **partial-failure behaviour was
singled out as better than typical**: "in a lot of real systems a single bad row blocks the whole
batch and you re-scan everything."

What staff actually do and the screen does not cover: parcels that do not match the manifest, a
wrong count on the sheet or an extra box; writing on the parcel; a recipient arriving during
intake to ask whether anything has come; and recipient lookup, where the phone is often wrong or
the resident is not registered, so the screen should say "not registered — register as guest?"
rather than leave staff to guess.

Two further points, both confirmed against the repository:

- `Shelve later` only works once a location can be given after creation, and the contract does
  not offer that today. Full shelf plus no shelve-later means holding a parcel with nowhere to
  put it, and "staff will invent a workaround — put it on the floor, note it — regardless of the
  contract."
- Exceptions recorded at intake assume inspection happens first. In reality the damage is often
  discovered after registration, and the screen implies there is no path back.

The batch cap of 50 with a hard `400` was called a wall staff will hit: "a 400 that says 'split
the drop' means I now split by hand. Better: accept up to 50, tell me how many are over, and let
me register the first 50 and leave the rest queued."

### Q4 — How long would this step take? Faster or slower than now?

The target is ten parcels recorded in under three minutes. The respondent's reading:

- **batch mode is plausibly faster**, because of scan or paste, one check and partial-failure
  handling. Ten parcels in under three minutes is realistic if the suggestion is accepted with
  one keystroke and nothing goes wrong;
- **the suggestion panel works against the target in single mode**: reading four lines and
  deciding accept or choose adds seconds per parcel, and "seconds × 50 is minutes";
- **problems cost disproportionately**: one "no free large slot" row means a decision, a possible
  zone override and possibly a shelve-later workaround, so ten parcels with two problems is not
  ten equal steps;
- **duplicate waybills are handled well**, at field level rather than as a page error.

The conclusion was that the risks to the three-minute target are the suggestion panel in single
mode and the full-shelf case, not the batch scan itself, and that the way to settle it is to time
ten parcels three ways: suggestions auto-accepted, panel hidden, and one forced "no free slot"
row.

### Q5 — Is there anything here that should not be built, or a need it misses?

**Should not be built, or should be cut:**

- **`last: 4821 handed over 19:14 by staff1`** on the pickup screen. A handover audit record on
  the fastest screen in the system does nothing for the current handover, and `staff1` is "a
  pseudonym in a screen that otherwise deliberately names nobody".
- **`Resident lost the code? Use identity`** as a permanently visible link. It makes a fallback
  look like a primary action beside `Verify`, competes with the happy path, and, as the
  respondent put it, "as the states admit, the fallback can't be built yet". Drawing it as live
  risks building against a capability that does not exist.
- **`keyboard or scanner`** next to the field. A scanner types exactly like a keyboard, so this
  is a note to the developer and one more word to read at the counter.
- **the empty `Result` panel**, which is a dead region before any result exists.
- **the designed "progress" moment** for `Verify`. Worth building as disable plus spinner to
  prevent double submit, not as a designed state.

**Needs it misses:**

- **a valid code whose parcel is not physically there.** The states cover invalid, expired and
  already used, but not missing. "Real counters hit this constantly — a parcel shelved in the
  wrong bin, or already collected but not marked."
- **the scanner ends with Enter.** If the screen needs a separate `[ Verify ]` click after a
  scan, staff will scan and walk. Either the scan verifies, or the design says Enter does.
- **the identity fallback is thin.** Phone plus surname is guessable and phone numbers are
  shared. The screen should force staff to look at the parcel label, not just type two fields.
- **delegated pickup is underspecified.** How the authorised person obtains a code is not
  stated, and whether the identity fallback applies to them and with what verification is
  unclear.
- **no time-left context on the result row.** Showing held days or overdue there would prompt
  staff to mention it.
- **no guard for a resident who has other parcels waiting**, which is the most common counter
  correction.
- **no explicit prompt to compare the recipient name against the label.** One line would close
  it.

## Respondent 2

A staff member who does parcel intake at a counter and also receives parcels. This is the
respondent that satisfied the staff half of the sample, and the answers are short because the
respondent answered as a worker rather than as a reviewer.

### Q1 — Looking at this screen, do you know what to do next?

Roughly. The parcel would be picked using the pickup code and the storage spot, but the
`[ Open ]` action was unclear: "I'm not sure what the [Open] button does. Does it copy the code?
Or show more details? That isn't clear to me."

### Q2 — Which information here is unnecessary, and which is missing?

**Unnecessary:** the courier prefixes, such as `SF`, `YT` and `JD`.

**Missing:**

- the deadline for an overdue parcel. One card says "Overdue 4 days" with no indication of how
  soon it will be returned;
- clarification of what `[ Open ]` does;
- a reminder of when the parcel was dropped off.

### Q3 — How would this differ from what you actually do?

As staff: "Right now I usually just scan parcels one by one. This batch mode looks new to me.
Also, I don't normally have to pick parcel sizes every time, that feels extra work."

As a resident: the pickup is unaffected, but "I worry if staff are busy typing in all this info,
my parcel might take longer to show up on my list. Also, if a parcel is marked damaged here, I
hope I get a quick notice about it."

Two points worth keeping: batch mode is unfamiliar rather than unwelcome, and size selection is
perceived as work the system should do.

### Q4 — How long would this step take? Faster or slower than now?

Single parcels: "probably about the same speed as now, maybe a little slower because of the size
selection."

Batch: "should be faster. But if many parcels hit issues like no free slot, I think it'll slow
down a lot. Overall, batch drop-offs will be quicker than doing them one by one."

That is close to the other respondent's conclusion reached independently: the scan is fast and
the exceptions are what cost time.

### Q5 — Is there anything here that should not be built, or a need it misses?

**A concern about the identity fallback**, which the respondent raised without being asked for a
security opinion: "the 'Resident lost the code? Use identity' part is good to have, but I'm a
little worried. If someone only gives a phone number and surname, is that safe enough? It feels
easy for someone else to guess. That's a risk I didn't expect."

**Missing:**

- a quick way for staff to leave a note, for example when the resident came to collect but the
  parcel turned out to be damaged;
- a simple overdue alert on the screen, so staff can mention it before handing the parcel over.

**Should not be built now:** the `last handed over` log. "It doesn't help me hand out the parcel
faster. It's probably only useful for checking later, not during pickup."

**A usability note on the error state:** "when the code is wrong, it just says it's not
recognised. That's fine for security, but if the resident keeps mixing up digits, there's no way
for staff to help them figure it out easily."

## Respondent 3

A resident. The answers are the shortest of the three and the most concrete about counter work,
which is why they carry weight on the three-minute target.

### Q1 — Looking at this screen, do you know what to do next?

Partly. The intended path was guessed — tap `Open`, then use the code — but two things were
unclear: what `Open` gives you when the code is already on the list, and where to go and who to
show the code to. Independently of the other respondents: **"The third card has no code, which is
confusing."**

### Q2 — Which information here is unnecessary, and which is missing?

**Unnecessary:** the long waybill number, `Shelved` repeated on every card, and the filter if
there are only a few parcels.

**Missing:**

- the pickup point location and opening hours;
- a line such as "show this code at the counter";
- when the code expires, or the last day to collect;
- a way to enlarge the code or show it as a QR code;
- what to do if it is overdue;
- an explanation of why the third card has no pickup code.

`Open` was called vague: "I don't know if it opens details or opens a locker."

### Q3 — How would this differ from what you actually do?

At the counter the work is grab the scanner, beep the barcode and put the parcel on the shelf. The
intake screen instead asks for a size, a phone number and an `Accept` on a suggested location.
Two criticisms:

- the size can often be judged from the box, or filled from the waybill;
- asking for a phone number on every box is odd, because couriers drop the pile and leave.

What was liked: **batch mode** for a courier dumping a cage of parcels, because pasting a list is
handy, and having the exception types inline so staff do not have to walk away.

### Q4 — How long would this step take? Faster or slower than now?

**Slower.** The estimate is that "a good worker can scan and throw 10 parcels in like 30
seconds", so the typing and the extra clicks will add time at a busy counter. That is the most
concrete challenge to the three-minute target in the record, and it comes with a number: 30
seconds against a three-minute budget.

For the resident side, three to five minutes was judged fine.

### Q5 — Is there anything here that should not be built, or a need it misses?

**Should not be built:**

- **the `ConfirmDialog` popup for handover.** "handing over a parcel isn't like deleting an
  account"; three interactions for one handover. "If I make a mistake, let a manager fix it in
  the backend."
- **the history line** `last: 4821 handed over...`, which takes up screen space.
- **the identity fallback, for now**, "because the notes literally say the backend doesn't even
  support it yet. Don't build a button that won't work."

**Needs it misses:**

- **multiple parcels for one resident.** Three boxes currently means scanning and confirming
  three times; a "collect all for this resident" path was suggested.
- **a loud visual and audio confirmation.** A beep and a large green tick, so staff know it
  worked without staring at the screen.
- **a much larger shelf location.** `Zone A · A-03-2` should be readable from a few feet away
  when walking to the shelf.
- **a contradiction between two statements**: one line says no person is named on any screen,
  another refers to the authorised person or a surname for the fallback. "Pick one."

## Findings verified against the repository

The respondents\' observations were checked against the files rather than accepted as written.
Fourteen held. Findings 1 to 10 have been addressed in the prototype or the mock-backed Sprint 2
pages. Finding 11 remains an R6 contract decision, and findings 12 to 14 were resolved by CR-023,
CR-024 and CR-025. A contradiction is listed as a defect rather than a preference, because a
document that disagrees with itself is wrong regardless of who noticed.

### Contradictions inside the prototype

| # | Finding | Where | Raised by | Outcome |
|:--:|---|---|---|---|
| 1 | The third card in the resident list is `shelved` but carries **no pickup code, no days held and no open action**, while the prototype requires all six elements on every card | `ui-prototype.md` §5.1 | 1, 2, 3 | Fixed by R5 in v1.2 |
| 2 | `last: 4821 handed over 19:14 by staff1` puts a pseudonymous staff identifier on a screen that claims "No person is named on any screen" | `ui-prototype.md` §5.6 | 1, 2, 3 | Fixed by R4 in v1.1 |
| 3 | `Resident lost the code? Use identity` is drawn as a live action, while the fallback needs a contract decision (`CD-2`) before it can be built | `ui-prototype.md` §5.6 | 1, 2 | Fixed by R4 in v1.1; fallback marked unavailable |
| 4 | The `delegated code` state is described as available, while `FR-9` and `FR-10` are future work under `CR-005` | `ui-prototype.md` §5.6 | 1, 2, 3 | Fixed by R4 in v1.1; removed from the current state list |
| 5 | The overdue card is described as "marked", but only the text changes: no colour, border or icon is specified | `ui-prototype.md` §5.1 | 1, 3 | Fixed by R5 in v1.2 |
| 6 | `keyboard or scanner` is a note to the developer rendered inside the interface | `ui-prototype.md` §5.6 | 1 | Fixed by R4 in v1.1 |
| 7 | The `Result` block is drawn in the idle state with nothing under it | `ui-prototype.md` §5.6 | 1 | Fixed by R4 in v1.1 |

### Gaps against requirements that already exist

| # | Finding | Where | Raised by | Outcome |
|:--:|---|---|---|---|
| 8 | The resident list shows no collection point, opening hours or return-to-sender date. `FR-38` holds the retention period and `FR-39` the overdue calculation, so the data exists and is not surfaced | `ui-prototype.md` §5.1 | 1, 2, 3 | Fixed by R5 in v1.2 |
| 9 | No state covers a valid code whose parcel is not physically on the shelf — wrong bin, or collected and not marked. The states cover invalid, expired and already-used only | `ui-prototype.md` §5.6 | 1 | Implemented in the mock-backed `T-42-05`; API representation remains with R6/R2 |
| 10 | A scanner ends with Enter, but the flow requires a separate `Verify` click, which staff will skip | `ui-prototype.md` §5.6 | 1 | Implemented in `T-42-05` |
| 11 | No path for a resident collecting several parcels: each needs its own verify and confirm | `openapi.yaml` pickup paths | 1, 2, 3 | Open — R6 contract decision |

### Open questions about scope

| # | Question | Where | Raised by | Outcome |
|:--:|---|---|---|---|
| 12 | The suggestion panel conflicts with its own survey evidence (S4 recorded **0 of 7** staff asking for a recommendation) and, per two respondents, adds latency in single mode. Should `FR-19` and `FR-20` be reduced further? | §5.4 vs `CR-004` | 1, 2, 3 | Resolved by CR-023 |
| 13 | A full shelf with no `shelve later` leaves no honest option. Same gap as `#98` | §5.4, contract | 1 | Resolved by CR-024 |
| 14 | A batch over 50 returns `400` with no way to register the valid 50 and queue the rest | §5.4 | 1 | Resolved by CR-025 |

**The three-minute handover target was challenged with a number**, not an opinion: one respondent
estimated a competent worker at about 30 seconds for ten parcels, against a three-minute budget.
That is `T-42-*` performance work rather than a prototype defect, and it is worth testing rather
than arguing: time ten parcels with suggestions auto-accepted, with the panel hidden, and with
one forced "no free slot" row.

## Effect on the requirements

**No requirement is added, removed or renumbered by the written half**, and where the matrix
marks "CR recommended" or "Design needed", a CR should be opened and logged in `change-log.md`.

The walkthrough adds findings rather than requirements. They split three ways:

- **seven contradictions** (findings 1 to 7) are defects in `ui-prototype.md` against its own
  text, and are corrected in the prototype rather than in the requirements;
- **four gaps** (findings 8 to 11) are defects against requirements that already exist, and are
  carried into Sprint 2 as `T-42-*` work;
- **three scope questions** (findings 12 to 14) need a ruling before anything is built, and each
  should become a change record. Finding 12 is the most consequential: if `FR-19` and `FR-20`
  should be reduced further, `CR-004` is superseded rather than amended.

## Limitations

- Three respondents is a convenience sample. It is enough to surface defects and not enough to
  claim the design suits the user population.
- The respondents were shown a wireframe, not a running system. Reactions to a drawing are not the
  same as reactions to a working screen, and the three-minute target cannot be judged from a
  drawing at all.
- The interviews were summarised rather than transcribed, following the rule in
  `docs/05-management/ethics-statement.md` section 3.1: the repository holds the analysed
  summary, never the raw responses.
- The core set reviewed is a selected sample of 20 rows: 13 references to SRS functional requirements, two cross-references to NFRs, and five descriptions that have no SRS counterpart. It covers the most impactful user journeys and is not an exhaustive review of all 47 functional requirements.

## Effect on RSK-03

`RSK-03` records that the requirements evidence base rests on one convenience sample of 29
questionnaire responses, so the quantitative targets are not statistically supported.

**This confirmation does not widen that sample, so it does not reduce `RSK-03`.** What it
can show is that each requirement has a place in the design; it cannot show that the targets
are right. The risk therefore stays where it is.

Its mitigation is already in place and is now recorded in a third document: the limitation is
stated in the SRS preface and in `survey-results.md` section 1.2, and every target is
described as a design target rather than as a population value. This record is the third
place that says so.

**Status is left at `Occurred, being handled` deliberately.** The trigger, a reviewer asking
where a target value came from, is answered by those three documents. The likelihood cannot be
reduced inside the project, because collecting a second sample is not in scope. Marking the
risk closed would imply the sample had been strengthened, and it has not.

## Next steps (recommended)

1. For rows marked "Not covered" or "Partial", raise CRs to specify the missing screens/data elements and link them back to the affected requirement IDs.
2. If the team prefers to meet the original acceptance criterion requiring three respondents, run the short interview script (five questions) with three appropriate respondents and update this file to reflect any confirmed changes.
3. Once CRs are raised and implemented, re-run this confirmation against the updated artefacts and update the revision history.

## Annex: interview script for the three-respondent route

The `Next steps` item 2 above offers the interview route. These are the five questions, kept
here so that the option is something the team can actually run rather than a reference to a
script that exists nowhere.

Show one screen at a time from `ui-prototype.md`, then ask:

| # | Question | What it tests |
|:--:|---|---|
| 1 | Looking at this screen, do you know what to do next? | Usability |
| 2 | Which information here is unnecessary, and which is missing? | Completeness |
| 3 | If you worked at this counter, or received parcels here, how would this differ from what you actually do? | Realism |
| 4 | How long would this step take? Faster or slower than now? | Whether the target values are plausible |
| 5 | Is there anything here that should not be built, or a need it misses? | Validity and scope |

**Recording rules, from `docs/05-management/ethics-statement.md` section 3.1:**

- Report replies as **counts only**, for example "three of three answered yes to question 1".
  Do not use percentages; the sample is too small and a percentage would overstate precision.
- Identify respondents by **role and relevant experience**, never by name: "Respondent 1,
  resident, receives parcels two or three times a month". Nothing that would let a reader
  work out who they are.
- Record **no** name, contact detail, student number or address.
- Suggest three respondents with **at least one staff or owner and one resident**.
- If a reply rejects a requirement, open a `CR` and log it. The change-log rule applies: a
  change that is not in the log did not happen.

## Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v1.0 | Week 3 | Initial requirement confirmation (written confirmation against artefacts) | R2 |
| v1.1 | Week 3 | Wording refined; added confirmation matrix and next steps | R2 |
| v1.2 | Week 3 | Added the "Effect on RSK-03" section and an annex holding the five interview questions, so that the option in Next steps is runnable. Corrected the sample count in Limitations, which read as 22 while the matrix has 20 rows | R1 |
| v1.3 | Week 3 | Added the three respondent interviews and the findings table, and split the findings into seven prototype contradictions, four gaps against existing requirements and three scope questions. Corrected the Method and Limitations sections, which still described the record as having no respondents | R1 |
| v1.4 | Week 3 | Status changed from draft to complete for Sprint 1 | R1 |
| v1.5 | Week 3 | Added an outcome column and recorded the R4 corrections from `#170`; R5 and R6/R2 items remain explicitly open | R4 |
| v1.6 | Week 3 | Recorded the R5 fixes, the mock-backed T-42-05 implementation and the CR-023 to CR-025 resolutions; finding 11 remains with R6 | R4 |
