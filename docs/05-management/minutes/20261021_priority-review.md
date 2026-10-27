# Priority review: resident convenience against staff effort

| Field | Detail |
|---|---|
| Task | T-10-12 |
| Version | v1.0 |
| Date of record | 2026-10-21 |
| Form | Reconstructed record. No meeting was convened for this task, and no recording exists |
| Chair | R2 (Chan Yin Cho), Product Owner |
| Record taken by | R1, from `change-log.md`, `survey-results.md` and the repository as it stands |
| Decisions recorded | `CR-003`, `CR-004` |
| Sprint | Sprint 1, week 2 |
| Related | [change-log.md](../change-log.md), [survey-results.md](../../01-requirements/elicitation/survey-results.md), [product-backlog.md](../../01-requirements/specification/product-backlog.md) |

## Why this record exists and why it is dated week 3

`PLAN.md` schedules `T-10-12` as a priority review meeting in Sprint 1, week 2, "resolving
conflicts between resident convenience and staff effort", with the findings recorded. **No minutes
were written for it, and no meeting appears in the minutes index.**

The two decisions it was meant to produce, however, exist and are dated **week 1**:

| Change | Decision | Date in the change log |
|---|---|---|
| `CR-003` | batch registration raised from should to must, and made the first staff feature in Sprint 2 | Week 1 |
| `CR-004` | storage location recommendation reduced from must to should, delivered as an override-able suggestion only | Week 1 |

So the decisions came **before** the week the plan schedules the meeting in. Either the meeting ran
early, or the decisions were taken in the group chat and written up afterwards. **This record does
not claim which.** What it does is collect the two decisions, the evidence behind them, and the
reasoning, so that `T-10-12` has a record instead of a gap.

## Agenda

The conflict the task names is real and appears in the questionnaire data:

| Role group | What they wanted | What it costs someone else |
|---|---|---|
| Residents | notification when a parcel arrives, and a short walk to collect it | every extra field a staff member types lengthens the queue they stand in |
| Staff | fewer steps per parcel, and batch intake | batch intake is the least popular thing to build and the most wanted at the counter |
| Owners | overdue visibility and per-point figures | each report is a report nobody has time to read |

## Discussion

### Batch registration: the promotion

`survey-results.md` records that **4 of 7 staff respondents named batch registration as their main
source of delay**, and it is the largest delay any role group identified. Nothing else in the four
questionnaires came close.

The counter argument was effort: a batch screen is more work than a single-parcel form, and Sprint 2
is already carrying the authentication foundation. The decision went the other way because the delay
is measured in minutes per delivery and the alternative is staff working around the system, which
`#98` and `#170` both show they already do when a screen does not fit the job.

**`CR-003`:** batch registration becomes a must-have and the first staff feature built in Sprint 2.

### Storage location recommendation: the demotion

The feature had been described as a headline feature. The survey says otherwise:
**S4 recorded 0 of 7 staff choosing location recommendation**, and **S5 recorded 4 of 7 preferring
manual control or no suggestion at all.**

So the feature the team had planned as a differentiator was the one nobody asked for. The decision
kept it, because a deterministic suggestion costs nothing to compute and helps a new member learn
the zones, but reduced its weight and wrote down the rule that matters: **it is a suggestion and is
never applied automatically.**

**`CR-004`:** the feature becomes a should-have, delivered as an override-able suggestion.

### The conflict this meeting was supposed to resolve

Naming it plainly: **resident convenience and staff effort were not actually in conflict in either
decision.** Both were settled by the same evidence, which is that staff time at the counter is the
scarce resource and residents experience that time as the queue. The plan framed the task as a
trade-off; the data did not present one.

That is worth recording because it will come up again. `#170` finding 12 asks whether `FR-19` and
`FR-20` should be reduced further, and two of the three interview respondents called the suggestion
panel noise. The same evidence points the same way.

## Decisions

| # | Decision | Change record | Owner |
|:--:|---|---|:--:|
| 1 | Batch registration is a must-have and the first staff feature in Sprint 2 | `CR-003` | R2 |
| 2 | Storage location recommendation is a should-have, a suggestion only, never applied automatically | `CR-004` | R2 |

## Actions

| # | Action | Owner | Status |
|:--:|---|---|---|
| 1 | Reflect both changes in the product backlog and the SRS | R2, R3 | done — `BL-001` and `BL-006`, `FR-18` to `FR-20` |
| 2 | Sequence Sprint 2 so batch intake comes first among the staff features | R1 | done — `T-42-01` and `T-42-02` ordering in `PLAN.md` |
| 3 | Revisit the recommendation panel if user evidence contradicts the demotion | R2 | open — `#170` finding 12 |

## What this record does not claim

- **No meeting was held for `T-10-12`**, and no attendee list is asserted.
- **The two decisions were not made here.** They were made in week 1 and recorded in the change log;
  this document assembles them.
- **No new decision is made.** Any change from here needs its own change record.

## Revision history

| Version | Date | Change | Author |
|---|---|---|---|
| v1.0 | 2026-10-21 | Reconstructed record of `T-10-12`: the two priority decisions, the evidence behind them, and an explicit statement that no meeting was held | R1 |
