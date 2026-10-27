# Sprint 1 review and retrospective

| Field | Detail |
|---|---|
| Task | `T-00-06`, closing the Sprint 1 review |
| Version | v1.0 |
| Date of record | 2026-10-22 |
| Form | **Asynchronous review. No meeting was convened and no recording exists** |
| Chair | R1 (Liu JiaHao) |
| Record taken by | R1, from the repository as it stands on 2026-10-22 |
| Sprint | Sprint 1, week 2, 2026-10-12 to 2026-10-19 |
| Related | [PLAN.md](../plans/PLAN.md), [change-log.md](../change-log.md), [20261019_requirement-confirmation.md](../../01-requirements/elicitation/20261019_requirement-confirmation.md), [v0.1.1](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/releases/tag/v0.1.1) |

## Why this record exists and why it is not a meeting

`docs/05-management/minutes/README.md` requires a sprint review on the last day of each sprint, and
says the record must contain **what the increment demonstrated, what the stakeholders said, and what
the team agreed to change**.

**No meeting was convened.** Sprint 1 ended on 19 October and every member had other commitments in
the days that followed, so the review was conducted against the repository and the sprint's own
artefacts. The Sprint 2 kick-off was delivered as an individual message to each member rather than
as a meeting, for the same reason.

So this document follows the form `20261015_design-review.md` already set for this repository: a
review of artefacts rather than of a conversation. **No attendee list is claimed, no member's opinion
is attributed unless it is already written in the repository, and the third section lists only
changes that were actually made and recorded.**

---

## 1. What the increment demonstrated

Sprint 1's goal was "a frozen requirements and design baseline … so that Sprint 2 can start building
without further decisions". **There is no running software in this increment**, and that is not a
shortfall: the sprint's definition of done was the baseline itself.

### Delivered and verifiable

| Artefact | Size | Where |
|---|---|---|
| Functional requirements | **47**, each with rationale, source, MoSCoW and a verification method | `SRS.md` chapter 3 |
| Non-functional requirements | **14**, each with a measurable target and a verification method | `SRS.md` chapter 4 |
| Requirement traceability matrix | **61 rows**, every requirement mapped to design, code module and a reserved test case | `RTM.md` |
| Product backlog | **10 items** with Given/When/Then acceptance criteria and MoSCoW order | `product-backlog.md` |
| Test case register | **61 reserved identifiers**, level column complete | `test-cases/README.md` |
| Interface contract | **12 paths, 16 schemas** | `openapi.yaml` |
| Data model | **9 entities**, 10 relationships | `ERD.md` |
| UML sources | **12 files**: 2 use case, 3 sequence, 1 class, 4 state machine, 2 activity | `docs/02-design/uml/` |
| UI prototype | **9 page prototypes**, 766 lines | `ui-prototype.md` |
| High fidelity pages | **3 rendered pages** plus sources | `docs/02-design/high-fidelity/` |
| Architecture decision records | **4** | `docs/02-design/adr/` |
| Frontend skeleton | **23 source files, 14 routes** with placeholders | `frontend/` |
| Continuous integration | **2 live jobs** plus 2 repository-wide consistency checks | `.github/workflows/ci.yml` |
| Docker Compose | PostgreSQL 16, Redis, frontend; backend behind a profile until it can build | `docker-compose.yml` |

**72 pull requests were merged into `develop` during the sprint.** Every one required a non-author
approval and a green pipeline, so each was read by at least two people.

### Released

`v0.1.0` was tagged on **14 October**, and `v0.1.1` on **22 October**. **The second tag exists because
the first was five days early**: four of the sprint's own deliverables — the RTM, the product backlog,
the test-case register and the requirement confirmation record — landed after `v0.1.0`. `v0.1.1`
covers them and the three close-out corrections that followed.

**A sprint whose release tag has to be re-cut is a sprint whose definition of done was applied at the
wrong moment.** That is the first of the observations in section 4.

### Not delivered

- **`SRS.md` has chapters 1 to 4, not the 1 to 7 that `PLAN.md` asked for.** The requirement content
  is complete; the chapters on external interfaces, data requirements and constraints are not
  written, and their content currently lives in `openapi.yaml`, `ERD.md` and `glossary.md`. The plan
  has since been corrected to match the document rather than the document padded to match the plan.
- **`T-20-14`, "report chapters 4 and 5: figure captions and explanatory text", produced nothing.**
  The report is limited to 1000 words, so a five-chapter structure was never going to survive; the
  task is obsolete rather than overdue.
- **`T-10-12`, the priority review, produced no minutes at the time.** The two decisions it existed
  to make — `CR-003` and `CR-004` — were made in week 1 and recorded in the change log.
  `20261021_priority-review.md` now records them and states plainly that no meeting was held.

---

## 2. What the stakeholders said

### The only recorded external feedback is the interview round

The course's progress review falls between 2026-11-03 and 2026-11-23, so **no teaching-team feedback
exists for this sprint yet**. What exists is the requirement confirmation record: three respondents
walked the resident list, the staff intake screen and the pickup screen, and were asked five
questions.

| Respondent | Role | Duration |
|---|---|---|
| Respondent 1 | resident; read the prototype document rather than only the drawings | 25 minutes |
| Respondent 2 | staff who does parcel intake at a counter, and a resident | 15 minutes |
| Respondent 3 | resident | 15 minutes |

**Their observations were checked against the files rather than accepted as written, and fourteen
held.** Seven are places where `ui-prototype.md` contradicts its own specification text.

### The three findings that matter most

**a. A shelved parcel with no pickup code.** The third card in the resident list is drawn as
`shelved` but carries no pickup code, no days held and no open action — while the same document
requires a code on any `shelved` card, requires all six elements on every card, and counts the
shelved cards as two when the drawing shows three. **All three respondents raised it independently.**
A resident with an overdue parcel can see the shelf and cannot see the code, which is the opposite of
what an overdue parcel needs.

**b. A staff identifier on a screen that says nobody is named.** `last: 4821 handed over 19:14 by
staff1` sits on the pickup screen, eight lines above the sentence "No person is named on any screen".

**c. An action drawn as live that the contract cannot support.** `Resident lost the code? Use
identity` is drawn beside `Verify`, while the states section of the same document says the fallback
needs a contract decision — recorded as `#98` — before it can be built.

### The finding nobody wrote down, because it was not a defect

One respondent estimated **ten parcels in about thirty seconds** for a competent worker, against the
three-minute budget the requirement sets. That is not a contradiction in a document; it is a challenge
to a target that came from a survey rather than from measurement. It is recorded here because it will
not appear in any of the fourteen findings, and it is the kind of thing only a person who has done the
job can say.

---

## 3. What was changed as a result

**Only changes that were made and recorded are listed.** Each has a change record, a pull request or
an issue behind it.

| # | Change | Record | Why |
|:--:|---|---|---|
| 1 | `FR-18` gained acceptance criteria and a partial-failure rule, including the Given/When/Then form | SRS section 3.7, `#168` | The walkthrough's finding 1: an endpoint that returns `201` for a partly rejected batch needs its failure behaviour written down |
| 2 | The billing scope ruled as **eight weeks**, payment out, overdue fee calculation in | `CR-022` | The walkthrough recorded an apparent contradiction between `OS-3` and `PLAN.md`; `OS-3` already drew the line, so the finding was a misreading of it |
| 3 | The requirement confirmation record gained the interview annex and the `RSK-03` review | `#167`, then `#169` | Criterion 5 of `#79` had no evidence, and the "next step" it offered named a script that existed nowhere |
| 4 | Twelve design and API documents gained a version header and a revision history | `#175` | Three of them are the documents Sprint 2 changes most, and a change to any of them left no trace |
| 5 | The Sprint 1 deliverable corrected from "chapters 1 to 7" to "chapters 1 to 4" | `PLAN.md` v1.4, `#174` | The line had said 1 to 7 since 12 October while the document had four chapters throughout |
| 6 | `PLAN.md` gained the Sprint 0 section its overview had always named but its body never described | `#173` | Sprint 0 could otherwise only be audited by guessing which files it should have produced |
| 7 | `T-30-06`, the backend CI job, moved from R10 to R6 | `PLAN.md` v1.3 | `#137`'s fourth acceptance criterion already assigned it to R6, and one deliverable should not carry two owners |
| 8 | Five documents moved off `Draft`; `SRS.md` to `v0.7` and out of draft in its version string | `#173` | A sprint that has ended cannot have its central document still describing itself as a draft |
| 9 | `v0.1.1` tagged, and `v0.1.0`'s earliness recorded in `CHANGELOG.md` | `v0.1.1` | A release tag that misses four of its sprint's deliverables is not a release |

**One thing deliberately not changed:** the fourteen findings are recorded but not fixed. Seven of
them are prototype contradictions that `#170` now tracks, and saying "recorded" while meaning "fixed"
is the failure mode this sprint spent its last three days removing.

---

## 4. Retrospective: what the sprint taught

### Observation 1 — the checks cover format; the defects live in meaning

`scripts/check_doc_versions.py` compares a document's declared version against its own revision
history. `scripts/check_table_columns.py` compares a table row's cell count against its header.

**Between them they passed every one of the following:**

| Instance | What the check saw | What was actually wrong |
|---|---|---|
| `#166` round 1 | green | the revision rows were in descending order, so "last row" was the oldest |
| `#166` round 2 | green | twenty invented requirement identifiers, none of which existed |
| `#168` | green | a whole section added with the version left at `v0.5` |
| `#155` | green | a stale section that duplicated a heading, and a sentence promising "forty-nine" rows when there were 47 |
| `#173` | green | five documents still called themselves draft |
| `#174` | green, then a conflict | the same version number claimed by two open pull requests |
| `#178`'s body | not applicable | a three-backtick YAML block inside a three-backtick fence, which broke the issue when rendered |

**Seven instances, one sprint.** The same defect each time: **a check that compares two things cannot
notice that a third thing is missing.**

**What was done about it:** `check_doc_versions.py` gained a `REQUIRED` list, so a document that
ought to have a header and does not is now a **problem** rather than a silent skip. It gained a YAML
branch, so the contract is checked at all. The list grew from 18 documents to 32.

### Observation 2 — a green tick is not evidence

`#79` was closed on 20 October **three seconds** after its first deliverable merged, with two of its
five acceptance criteria unmet and **no comment**. It was reopened the same day, and closing it
properly took three more pull requests, three rounds of review on one of them, and a set of
interviews.

**The lesson is not "review more carefully".** It is that a checkbox records an intention. What
records a fact is a file, a line number, or a quoted command output. Every acceptance criterion in
that issue was eventually satisfied with a pointer to one of those, and none of them could have been
satisfied by a tick alone.

### Observation 3 — the plan and the documents drifted apart in silence

`PLAN.md` asked for a seven-chapter SRS from 12 October to 22 October, while `SRS.md` had four
chapters and said so in its own version string. **Nobody noticed for nine days**, because no check
compares a plan against the documents it plans.

The same shape appears in `T-20-14`: a task to write report chapters 4 and 5 for a report limited to
1000 words. **A plan is a claim about the future, and it decays unless something re-reads it.**

### Observation 4 — the scope of the sprint was right, and the scope of the releases was not

Sprint 1 delivered what it set out to deliver. What went wrong was the moment of release: `v0.1.0`
was cut on 14 October, five days before the sprint ended, so four deliverables missed their own tag.

**The fix is not more discipline about tagging.** It is that the definition of done has to name the
moment, not the work.

### Observation 5 — the logbook is the last thing written and the first thing graded

Ten logbooks exist. In week 2, three members recorded fewer than four rows each (R3 one row, R5 three,
R10 three). **Two days into week 3, nine of the ten logbooks had no week-3 rows at all.**

The logbook is worth **15% of the module**, and it is the only artefact that is graded individually
rather than as a team. **It is also the only one that gets written at 20:55 on a Sunday.**

---

## 5. What has not been decided

**This section exists because a sprint review that claims agreements it cannot evidence is worse than
one that names the questions.** Each item below is open, and each has somewhere to live.

| # | Open question | Blocking what | Where it lives |
|:--:|---|---|---|
| 1 | Should `FR-19` and `FR-20`, the storage location suggestion, be reduced further than `CR-004` took them? Two of three respondents called the panel noise, and the survey recorded 0 of 7 staff asking for it | R5's staff workbench, which contains the panel | `#170` finding 12; the ruling is carried by a new task issue for R2, listed in `#185` |
| 2 | Does a full shelf need a fallback before `shelve later` exists? | the location board, and `T-42-03` in week 4 | `#170` finding 13, `#98` |
| 3 | Should the batch cap of 50 queue the overflow instead of returning `400`? | `T-42-01` in week 4 | `#170` finding 14, `#98` |
| 4 | Is "one resident collecting several parcels at once" a requirement? Two respondents raised it | possibly a new `FR-48` | a new task issue for R3, listed in `#185` |
| 5 | `#98`'s three contract gaps: assigning a location to an existing parcel, the lost-code verification path, and a location claimed between the recommendation and the save | `T-42-02`, `T-42-03`, `T-42-04` in week 4 | `#98`, still open |
| 6 | Is `RSK-11`, "Java and Spring Boot are new to part of the team", still `Being monitored`? | nothing directly, but it is the recorded explanation for a slow backend start | `risk-register.md` |
| 7 | Five cross-references in `SRS.md` still describe a state that has passed: section 4.6 says "confirmation before this chapter is frozen", `2.8` `OI-1` and `OI-3` are resolved, section 3.5 still says "checks before approval", and `NFR-8` still calls `#127` open | nothing; they are stale but not wrong in substance | deferred by agreement to the end of Sprint 2 |
| 8 | What replaces `T-20-14`, which asked for report chapters that a 1000-word limit cannot accommodate? | the report | a new task issue for R9, listed in `#185` |

---

## 6. Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v1.0 | Week 3 | First record of the Sprint 1 review, conducted asynchronously from the repository because no meeting was convened. Records what the increment demonstrated, what the three interview respondents said, the nine changes made in response, five retrospective observations and eight open questions | R1 |
