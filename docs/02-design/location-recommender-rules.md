# Storage Location Recommender Rules

| Item | Detail |
|---|---|
| Status | Accepted |
| Version | v1.0 |
| Date | 2026-10-15 |
| Accepted | 2026-10-15, by R6 as the reviewer and the ADR owner of T-20-15. This table is the order T-42-02 implements and the normative home of the rule |
| Design record | `docs/02-design/location-recommender.md` holds the ruling, the evidence and the reasoning; this file holds the rule |
| Task | T-20-15 |
| Owners | R6 Backend Lead, R7 Backend Developer |
| Reviewer | R6 Backend Lead |
| Related | ADR-004, SRS FR-11, FR-14, FR-15, FR-16, FR-19 and FR-20; CR-004; CRJ-001; `docs/03-api/openapi.yaml`; `docs/02-design/database/ERD.md`; `docs/02-design/ui-prototype.md` |

## 1. Purpose

This document turns ADR-004 into an implementable rule table. It covers the
zone rules, the deterministic ordering and the hot/cold-zone interpretation
requested by T-20-15 without introducing a weighted scoring model or a
predictive algorithm.

The result is advice only. It never changes a parcel or a storage location
record. The staff member keeps the final choice.

This is a pair task under D18. R7 prepared this first draft, and R6 is the only
reviewer for the task. R6 owns the final backend, data-model and ADR decision
before the rules are implemented in T-42-02, and accepted the table on
2026-10-15.

### 1.1 Requirement and ADR trace

| Source | What it requires | Where this design responds |
|---|---|---|
| FR-19 | Show an optional location suggestion and never apply it automatically | Sections 3, 6 and 9: the service is read-only and does not assign a location |
| FR-20 | Let staff ignore the suggestion and select a location manually | Sections 2, 7 and 9: override remains the normal save path |
| ADR-004 | Use a deterministic rule, prefer the requested zone and then the least recently updated available location | Sections 4 and 6 |
| CR-004 | The suggestion is should-have, optional and overridable | Sections 2 and 11 |
| CRJ-001 | Do not adopt a complex scoring algorithm | Section 4 uses a lexicographic order with no weights |
| OS-7 | The suggestion remains deterministic and is never automatic | Sections 2, 6 and 9 |

## 2. Scope and non-goals

In scope:

- filtering storage locations by collection point, status and parcel size;
- preferring an explicitly requested zone;
- preferring an exact size fit;
- preferring a cold zone when the staff member did not name a zone;
- using the longest-available location as the final practical tie-breaker;
- returning `recommendedLocation = null` and
  `reason = no_location_available` when nothing fits.

Out of scope:

- machine learning, forecasting or a multi-factor weighted score;
- reserving a location for the caller;
- applying the suggestion automatically;
- assigning a parcel to a location;
- optimizing walking distance, staff shifts or courier routes;
- changing the existing success response shape of `/locations/recommend`.

## 3. Inputs

The service receives:

| Input | Source | Rule |
|---|---|---|
| Collection point | Authenticated staff session | The client does not choose the site in the request body |
| Parcel size | Request body, `small`, `medium` or `large` | Required |
| Zone hint | Optional request field | A preference, never a hard filter |
| Candidate locations | Location repository | Same point only |

Each candidate has:

- `id`;
- `code`;
- `zone`;
- `size`;
- `status`;
- `updatedAt`.

Only `available` locations are eligible. `reserved`, `occupied`, `disabled`
and locations at another collection point are excluded.

## 4. Rule table

The order below is a lexicographic order, not a weighted calculation. An
earlier rule always wins over a later rule. There are no tunable weights.

| Order | Rule | Deterministic result | Source |
|---:|---|---|---|
| 1 | Capacity filter | Keep a location only when its size rank is greater than or equal to the parcel size rank. | ADR-004 |
| 2 | Zone priority | When a zone hint is supplied and at least one eligible location is in that zone, discard eligible locations in other zones. If the named zone has no eligible location, fall back to all zones. | ADR-004, OpenAPI |
| 3 | Exact-fit priority | Prefer `rank(location.size) - rank(parcel.size) == 0`, then the smallest positive difference. | Proposed for R6 approval |
| 4 | Cold-zone rotation | When no zone hint was decisive, prefer `cold`, then `normal`, then `hot`. | Proposed for R6 approval |
| 5 | Longest-available priority | Among equal candidates, prefer the oldest `updatedAt`. | ADR-004 |
| 6 | Stable tie-breaker | Prefer the lexicographically smaller location `code`. | Proposed for R6 approval |

Size rank is:

```text
small = 1
medium = 2
large = 3
```

The rule is deliberately not "always put a small parcel in the first small
slot". If no exact-fit slot exists, a larger slot remains a valid fallback.

Rules 3, 4 and 6 refine the accepted ADR rather than merely restating it.
ADR-004 originally ordered same-zone candidates by the least recently updated
location. R6 owns that decision, accepted these refinements on 2026-10-15, and
amended ADR-004 to the same ordering before T-42-02 implements it. The
acceptance is recorded in `docs/02-design/location-recommender.md` section 5;
the amendment is recorded in ADR-004's Amended line.

ADR-004 is amended in the ADR rather than in this file, so that the accepted
architecture decision stays in the ADR and this document stays the rule table.

## 5. Zone rules and hot/cold zones

`zone` remains the string already present in `Location`. This design does not
add a new persisted zone state.

For each zone at the collection point, calculate occupancy from active
locations only:

```text
active = available + reserved + occupied
occupancyRatio = (reserved + occupied) / active
```

`disabled` locations are excluded from the denominator. If a zone has no active
locations, it has no candidate and no hot/cold label.

| Zone class | Occupancy ratio | Rotation rank |
|---|---:|---:|
| cold | `0.00` to `0.20` inclusive | 0 |
| normal | greater than `0.20` and less than `0.80` | 1 |
| hot | `0.80` to `1.00` inclusive | 2 |

The 20 percent and 80 percent thresholds were accepted by R6 on 2026-10-15. The
task calls for hot and cold zones, but it fixes no cut-off, so the values are
written down here to be testable.

The labels are derived from current occupancy each time the recommendation is
requested. They are not stored. "Hot" therefore means heavily used or nearly
full, and "cold" means lightly used. The rule prefers cold zones only when the
staff member did not name a zone. An explicit zone hint wins even when that
zone is hot.

This is the simple interpretation required by CRJ-001. It does not use a
learned model, a weighted score or a hidden priority number.

## 6. Algorithm

```text
eligible = candidates where
    status == available
    and sizeRank(candidate.size) >= sizeRank(request.size)

if eligible is empty:
    return null, "no_location_available"

if request.zone is supplied:
    exactZone = eligible where zone == request.zone
    if exactZone is not empty:
        pool = exactZone
        zoneWasDecisive = true
    else:
        pool = eligible
        zoneWasDecisive = false
else:
    pool = eligible
    zoneWasDecisive = false

sort pool by:
    1. size excess, ascending
    2. zone class rank, ascending
    3. updatedAt, ascending
    4. code, ascending

return first location
```

When `zoneWasDecisive` is true, the zone class is only a tie-breaker inside the
named zone. It cannot move the result back to another zone.

## 7. Reason values

The current contract has one free-form `reason` string. This design proposes a
small vocabulary for it:

| Reason | When it is returned |
|---|---|
| `requested_zone` | The result is in the explicitly requested zone |
| `exact_size` | The result is the exact size and no zone hint was decisive |
| `cold_zone_rotation` | The result is in a cold zone as part of rotation |
| `least_recently_available` | The result was selected by the oldest `updatedAt` tie-breaker |
| `no_location_available` | No eligible location exists; the location value is null |

The endpoint still returns HTTP 200 for the null case, as ADR-004 and the
OpenAPI contract require. No additional response field is needed for hot/cold
classification.

## 8. Worked examples

Assume all locations are at the same collection point. The request is for a
`small` parcel.

| Location | Zone | Size | Status | Zone class | Result |
|---|---|---|---|---|---|
| `A-01` | A | small | available | hot | Not chosen when a cold exact fit exists |
| `A-02` | A | medium | available | hot | Valid fallback, but not exact |
| `B-01` | B | small | available | cold | Chosen when no zone hint is supplied |
| `B-02` | B | small | available | cold | Tie with `B-01`; older `updatedAt` wins |
| `C-01` | C | small | occupied | cold | Filtered out because it is not available |

If the request supplies zone `A`, `A-01` is chosen before `B-01` because the
explicit zone is the first decisive rule. If no zone is supplied, the exact-fit
cold candidates in B are considered before the hot exact fit in A. If every
available location is too small, the result is `null` with
`no_location_available`.

## 9. Concurrency and application rules

The recommendation endpoint is read-only. It must not change a location to
`reserved` or `occupied`, because that would violate the "advice only" rule.

Two staff members can therefore receive the same recommendation. The location
must be rechecked when the parcel is saved:

1. Read the recommendation.
2. Staff accepts it, chooses another location, or ignores the suggestion.
3. The save transaction claims the chosen location through the allowed state
   transitions: `available -> reserved` when the assignment is accepted, then
   `reserved -> occupied` when the parcel is shelved. It also checks the
   parcel's assigned point and size.
4. If the location can no longer be reserved, no parcel-location assignment is
   written. The operation returns a conflict such as
   `LOCATION_UNAVAILABLE`, and the staff member chooses again.
5. The unique partial constraint on `Location.parcelId` remains the final
   database guard.

The conflict response is not in the current OpenAPI contract. R6 ruled on
2026-10-15 that the behaviour is right but that adding it to a frozen contract
needs a change record first, so it is a dependency of T-42-02 and not a claim
that the contract is complete.

For batch intake, the service may calculate suggestions for a batch, but it
must not assign them automatically. Accepted locations have to be allocated
inside the save transaction, and each later item in the same batch has to
exclude locations already claimed by earlier items in that transaction.

## 10. Required tests

The test-plan target is at least 80 percent coverage for the recommender.
Component tests must cover:

1. an unavailable, disabled, reserved or occupied location is excluded;
2. a location smaller than the parcel is excluded;
3. an exact size match wins over a larger location;
4. an explicit zone wins over a colder zone elsewhere;
5. an unmatched zone hint falls back to all eligible zones;
6. the exact 20 percent and 80 percent occupancy boundaries classify as cold
   and hot respectively;
7. an older `updatedAt` wins when all earlier rules tie;
8. equal timestamps are resolved by location code;
9. an empty candidate set returns null and `no_location_available`;
10. a stale recommendation receives a conflict on save;
11. the recommendation itself writes no location or parcel state;
12. accepted assignment follows `available -> reserved -> occupied`;
13. the result is the same for the same inputs and candidate ordering.

## 11. Completion checklist for T-20-15

- [x] R6 confirms the lexicographic order and the cold-first rotation rule
      (2026-10-15; `location-recommender.md` section 5).
- [x] R6 confirms that ADR-004 must be amended for rules 3, 4 and 6. The
      amendment is made and recorded in ADR-004's Amended line.
- [x] R6 confirms the `LOCATION_UNAVAILABLE` conflict is the right behaviour for
      a stale recommendation, and that it stays out of the frozen contract until
      a change record approves it.
- [ ] The implementation task T-42-02 uses this rule table and does not add a
      weighted or learned score.



## Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v1.0 | Week 2 | First version recorded in this file. The rule table `T-42-02` implements, accepted by R6 on 2026-10-15. Any earlier edit is not listed: this file had no revision history until week 3, so records of those edits do not exist here. | R1 |
