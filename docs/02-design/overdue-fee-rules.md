# Overdue Fee Rules

| Item | Detail |
|---|---|
| Status | Complete for Sprint 1 |
| Version | v1.1 |
| Date | 2026-10-15 |
| Task | T-20-16 |
| Owner | R7 Backend Developer |
| Reviewers | R2 for rule values and scope, R6 for persistence and API impact, R8 for the 20 test cases |
| Related | SRS FR-39, FR-38 and FR-44; SRS OS-3; `docs/glossary.md`; `docs/04-testing/test-plan.md`; `docs/02-design/database/ERD.md` |

## 1. Purpose

This document fixes how the overdue fee engine calculates an amount for one
parcel. The engine is a deterministic, side-effect-free calculation. It reads
no clock and performs no database write, notification or payment.

The requirement is FR-39:

> The system shall calculate an overdue fee for an eligible parcel using the
> configured retention period and fee rule without taking payment.

`FR-39` is a should-have proposal in SRS chapter 3. The values in section 4 are
a proposed baseline for R2 to approve. Changing a monetary value does not
change the calculation rules in this document.

### 1.1 Requirement trace

| FR-39 phrase | How this design satisfies it |
|---|---|
| calculate an overdue fee | One pure function returns an amount and does not mutate data |
| for an eligible parcel | Eligibility starts when the parcel has a persisted `shelvedAt`; a `registered` parcel is excluded |
| using the configured retention period | `retentionDays` is a per-collection-point rule; the glossary default is three days |
| and fee rule | The rule contains the daily rate, cap, waiver decision and boundary rules |
| without taking payment | Payment, settlement and payment state are out of scope and are not added to the data model |

The cap and waiver are design details of the "fee rule" part of FR-39 and the
T-20-16 task, not new functional requirements. The 20-case comparison is a
test-plan requirement. `NFR-12` is still reserved for SRS chapter 4, so this
document does not claim that its final wording has already been defined.

## 2. Scope boundary

The following are in scope:

- calculating whether a shelved parcel is overdue;
- calculating the number of fee-bearing overdue days;
- applying a configured daily rate and cap;
- applying an explicit waiver decision;
- returning an exact amount in minor currency units.

The following are out of scope:

- collecting, refunding or settling money;
- recording that a resident paid;
- sending a demand or changing a parcel status;
- creating an overdue exception ticket;
- storing the current time inside the calculation function.

SRS OS-3 and FR-39 both require this boundary. This document does not add a
screen or an endpoint. Any owning feature that displays the result must show it
as a calculated amount, not as a payment.

## 3. Pure calculation contract

The calculation is one pure function in the billing module:

```java
OverdueFeeResult calculate(
        Instant shelvedAt,
        Instant evaluatedAt,
        OverdueFeeRule rule,
        Optional<FeeWaiver> waiver);
```

The caller loads `rule` and `waiver`. The function has no Spring annotation, no
repository call, no `Clock`, no HTTP type and no mutation.

Suggested value objects:

```text
OverdueFeeRule
  retentionDays: int
  dailyFeeMinorUnits: long
  capMinorUnits: long
  currency: ISO 4217 code

FeeWaiver
  reason: system_error | site_closure | goodwill
  authorisedBy: user id
  recordedAt: instant

OverdueFeeResult
  overdue: boolean
  overdueDays: long
  amountMinorUnits: long
  currency: string
  status: NOT_OVERDUE | CALCULATED | WAIVED
  capApplied: boolean
```

`amountMinorUnits` is the only amount returned by the engine. For example,
`200` means `HKD 2.00` when `currency` is `HKD`. No floating-point value is
used.

The caller decides which timestamp to pass:

| Caller context | `evaluatedAt` |
|---|---|
| Active shelved parcel | The current time read once by the caller |
| Handover calculation | `collectedAt` of the handover |
| Historical display | The stored calculation time, not a new current time |

A `registered` parcel has no shelving time and is not passed to the fee engine.
An overdue exception ticket does not alter the calculation. The fee engine uses
the parcel's shelving time, not the ticket creation time.

## 4. Rule table

| Key | Type | Proposed baseline | Rule |
|---|---|---|---|
| `retentionDays` | integer, `>= 0` | `3` | Free retention period. It is configurable per collection point. |
| `dailyFeeMinorUnits` | integer, `>= 0` | `200` | Calculated for each started overdue day. The baseline is HKD 2.00. |
| `capMinorUnits` | integer, `>= 0` | `2000` | Maximum calculated fee for one parcel. The baseline is HKD 20.00. |
| `currency` | ISO 4217 string | `HKD` | Currency of both configured amounts and the result. |
| `waiver` | explicit record or absent | absent | A valid waiver sets the amount to zero but does not change the overdue marker or day count. |

Rules:

1. Negative retention days, fees or caps are invalid input.
2. A zero daily fee or zero cap is valid and produces a zero amount.
3. `capMinorUnits` applies after the daily amount is calculated.
4. A change to a collection point's rule affects later calculations. A
   historical amount that must not change has to persist either the calculated
   result or the rule values used for that result.
5. The baseline is HKD because the project uses Hong Kong terminology and
   context. R2 must confirm this before the values are called final.
6. FR-39 does not fix a maximum retention period, so this design does not add
   one. An operational maximum would need separate approval.

## 5. Day and boundary rules

The retention period is a duration from `shelvedAt`, not a calendar-day count
based on midnight.

```text
retentionEndsAt = shelvedAt + retentionDays * 24 hours
```

The parcel is not overdue at `retentionEndsAt`. It becomes overdue at the first
instant after that timestamp.

After the retention period:

- a partial overdue period counts as one started day;
- exactly one completed 24-hour period counts as one day;
- a second period starts at the first instant after 24 hours.

This gives the following deterministic rules:

```text
if evaluatedAt <= retentionEndsAt:
    overdueDays = 0
else:
    overdueDays = number of started 24-hour periods after retentionEndsAt
```

Using a duration avoids daylight-saving changes and makes the calculation agree
with an independent recalculation. A service running in Hong Kong may use
`Asia/Hong_Kong` for display, but the stored values are instants and the pure
function does not apply a time zone.

## 6. Amount calculation

The calculation order is fixed:

```text
grossMinorUnits = dailyFeeMinorUnits * overdueDays
cappedMinorUnits = min(grossMinorUnits, capMinorUnits)
amountMinorUnits = waiver present ? 0 : cappedMinorUnits
```

The multiplication must be overflow-safe. A configured cap lets the engine
avoid calculating an unbounded product. If the product would overflow before
the cap is applied, the result is the cap, not an error, because the cap is the
upper bound of the business result.

No arithmetic rounding is required. All money is stored and calculated in
minor units. A configuration form may accept `2.00`, but the persistence layer
must convert it exactly to `200` and reject a value with more than two decimal
places. The frontend may format the result for display, but formatting never
changes the stored amount. This is the rounding policy for the test-plan cases:
the amount is exact in minor units, and invalid sub-minor-unit configuration is
rejected rather than rounded.

## 7. Waiver rules

A waiver is an explicit, auditable decision. The pure function does not infer a
waiver from free text or from the parcel status.

| Reason | Meaning | Example |
|---|---|---|
| `system_error` | A system failure prevented the normal notification or collection process | Notification delivery failed because of a system fault |
| `site_closure` | The collection point could not be used for a period | Unexpected closure for repair |
| `goodwill` | An authorised person grants a waiver for a recorded business reason | Goodwill after a documented service problem |

Rules:

- A waiver requires an authorised user, a reason and a timestamp.
- "The resident did not collect the parcel" is not a waiver.
- A waiver sets the amount to zero but leaves `overdue` and `overdueDays`
  unchanged, so reports can still show that the parcel was overdue.
- Without a waiver record, the engine never applies a waiver.

The persistence model for waivers is a dependency on R6. It must preserve the
reason and actor in the audit trail if the team wants the result to be
explainable later.

## 8. Configuration and persistence dependency

`CR-021` added the collection point identifier to `Setting` and created the `CollectionPoint`
entity this section said `T-30-03` would need, and `CR-026` records that `key` is unique
**within** a collection point rather than globally. Both preconditions this section set are
therefore met.

R6 must resolve this before T-30-03 creates the schema. The preferred shape is
a typed `OverdueFeeRule` table because the values have different types and
constraints:

```text
overdue_fee_rules
  id
  collection_point_id, unique
  retention_days
  daily_fee_minor_units
  cap_minor_units
  currency
  version
  updated_by_id
  updated_at
```

The generic `Setting` entity can be extended instead, but then the unique key
must become unique per collection point and the value remains an untyped
string. Either choice is acceptable only if it supports a per-point rule and
returns the same inputs to the pure function.

**The second shape was chosen and built in `T-30-03`.** A typed `OverdueFeeRule` table would
have covered the four overdue values and left the recommender's two thresholds with nowhere to
live, because `location_recommender.cold_zone_max_ratio` and
`location_recommender.hot_zone_min_ratio` are per-collection-point configuration too. Extending
`Setting` is the shape that serves both feature areas from one rule, which is why it was taken
over the shape this section preferred at the time.

A dedicated waiver table or an append-only audit event needs at least:

```text
parcel_id
reason
authorised_by_id
recorded_at
```

The overdue fee design does not add a payment table or a payment state.

## 9. Required verification cases

These are proposed component cases. R8 may use them directly or assign test
case IDs. The default row uses `retentionDays = 3`,
`dailyFeeMinorUnits = 200`, `capMinorUnits = 2000` and `currency = HKD`.
`Elapsed` is measured from `shelvedAt`.

| # | Elapsed from shelving | Expected overdue days | Expected amount | Boundary or rule |
|---:|---:|---:|---:|---|
| 1 | 0 hours | 0 | 0 | Not shelved long enough |
| 2 | 71 hours 59 minutes 59 seconds | 0 | 0 | Last instant inside retention |
| 3 | 72 hours exactly | 0 | 0 | Retention end is inclusive |
| 4 | 72 hours plus 1 second | 1 | 200 | First started overdue day |
| 5 | 73 hours | 1 | 200 | Partial first day |
| 6 | 95 hours 59 minutes 59 seconds | 1 | 200 | Last instant of first day |
| 7 | 96 hours exactly | 1 | 200 | Exactly one completed overdue day |
| 8 | 96 hours plus 1 second | 2 | 400 | Second day starts |
| 9 | 119 hours 59 minutes 59 seconds | 2 | 400 | Partial second day |
| 10 | 120 hours exactly | 2 | 400 | Exactly two completed days |
| 11 | 120 hours plus 1 second | 3 | 600 | Third day starts |
| 12 | 168 hours exactly | 4 | 800 | Exactly four completed days |
| 13 | 168 hours plus 1 second | 5 | 1000 | Fifth day starts |
| 14 | 240 hours exactly | 7 | 1400 | Seven completed days |
| 15 | 288 hours exactly | 9 | 1800 | Below cap |
| 16 | 360 hours exactly | 12 | 2000 | Cap applies |
| 17 | 720 hours exactly | 27 | 2000 | Long overdue parcel remains capped |
| 18 | 120 hours, daily fee 0 | 2 | 0 | Zero rate is valid |
| 19 | 96 hours plus 1 second, cap 100 | 2 | 100 | Cap below the gross amount |
| 20 | 120 hours, valid waiver | 2 | 0 | Waiver changes amount only |

Additional acceptance checks:

1. Run the same 20 cases through an independent implementation or spreadsheet
   and require zero differences.
2. Assert that the calculation performs no write and starts no payment.
3. Assert that a rule change does not alter a persisted historical result.
4. Assert that the calculation uses `shelvedAt`, not `registeredAt`, ticket
   creation time or the current wall clock.

## 10. Completion checklist for T-20-16

- [ ] R2 confirms the proposed baseline values, especially HKD 2.00 per day
      and the HKD 20.00 cap.
- [ ] R6 confirms where the per-point rule and waiver records are persisted.
- [ ] R8 accepts or adapts the 20 calculation cases.
- [ ] The pure function and its unit tests are added under `backend/` when
      T-44-01 is implemented.
- [ ] Any screen that displays the result labels it as a calculated amount and
      offers no payment action.



## Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v1.0 | Week 2 | First version recorded in this file. The rule table `T-44-01` implements. The twenty test cases `NFR-12` needs are Sprint 3 work and do not exist yet. Any earlier edit is not listed: this file had no revision history until week 3, so records of those edits do not exist here. | R1 |
| v1.1 | Week 3 | Section 8 records that both preconditions it set were met (`CR-021`, `CR-026`) and which of the two proposed shapes was built, with the reason it was preferred over the typed rule table | R1 |
