# W06 speaker notes — slides 4 (requirements engineering) and 6 (traceability and CR-029)

| Item | Detail |
|---|---|
| Task | T-60-03 / #324 |
| Owner | R3 (Li Cheuk Fung Kenneth) |
| Version | v0.1 |
| Status | Draft — content only, no slides before 13 November |
| Use | R3 speaks slides 4 and 6 of the 10-minute review (shared spine: `docs/01-requirements/progress-review-re-story.md`) |

These notes are the content of the two slides, not the slides. The deck itself is built with R1 and R9 on 13 November, so anything here that assumes a layout is marked. Timing assumes about 1:40 for both slides: 50 seconds on slide 4 and 50 seconds on slide 6. Together with R2's slides 2 and 5, this is the requirements-engineering portion of the review.

## Slide 4 — how we engineered the requirements (about 50 s)

**Claim:** We turned a four-role elicitation into a prioritised SRS and RTM, then validated those requirements and corrected them through the change log when the design or the running system showed they were wrong.

**Evidence link to put on the slide:** `docs/01-requirements/elicitation/20261019_requirement-confirmation.md` (prototype confirmation with three respondents, identified by role only).

**Say this, in this order:**

1. "We started from four role-specific questionnaires and wrote personas and journeys into an SRS with **47 functional and 14 non-functional requirements**, each with a rationale, a MoSCoW priority and a verification method."
2. "Every Must is in the RTM, mapped to a design artefact and a reserved test identifier. **Mapped is not built**: implementation status is a separate column. SRS v0.11 approves **32 Must requirements as a product baseline**; the Sprint 3 release baseline is the scope table and the gap rows."
3. "We validated, not just wrote. On 16 October we ran an internal requirements walkthrough. On 19 October we confirmed the prototype with **three respondents: two residents and one staff member**, identified only by role. Fourteen findings were checked against the repository, not accepted as written."
4. "When the design or the running system showed a requirement was wrong, we corrected the requirement with a change record. That is the process I will show on the next slide."

**Point at, do not read:** the four-step spine (elicit → specify → validate → change control) and the confirmation record. One finding that changed a design is enough if a reviewer asks.

**If asked how you validated:** walkthrough, prototype confirmation with three respondents, and the RTM/test mapping. We did not run a full acceptance test with real end users; say that limitation plainly.

**If asked why only 29 responses:** it is a convenience sample. We use counts as design targets, not as population percentages.

**Evidence**

- `docs/01-requirements/progress-review-re-story.md` — the four-step review spine.
- `docs/01-requirements/specification/SRS.md` v0.11 — 47 FRs, 14 NFRs, and the 32 `Must (approved)` product baseline.
- `docs/01-requirements/RTM.md` v0.9 (2026-11-12) — mapping plus the separate implementation-status column. v0.8 recorded the FR-18 `CR-025` correction; v0.9 is the current document on `develop`.
- `docs/05-management/minutes/20261016_requirements-walkthrough.md` — internal walkthrough.
- `docs/01-requirements/elicitation/20261019_requirement-confirmation.md` — three respondents by role only; no names.

## Slide 6 — one chain, and the CR-029 correction (about 50 s)

**Claim:** FR-12 can be followed from the SRS to a passing test; CR-029 shows we changed the requirement when a proposed `PENDING` location state could not be represented in the data model.

**Evidence link to put on the slide:** `docs/01-requirements/traceability-chain-FR-12.md`

**Say this, in this order:**

1. "Here is one chain a reader can follow. **FR-12**: an authenticated staff member registers a single arriving parcel."
2. "The path is: SRS FR-12, to `sequence-staff-intake.puml`, to `POST /parcels` in the OpenAPI contract, to `ParcelRegistrationService`, to `ParcelRegistrationIntegrationTest`, to the current successful `develop` CI run recorded in the RTM."
3. "`TC-PARC-001` is a reserved test identifier, not itself the CI result. The executed evidence is the integration test class."
4. "Change control, one story: **CR-028 proposed a `PENDING` storage slot**. That state is impossible under `locations.parcel_id`, which holds one value per row. **CR-029 corrected the requirement**: a parcel may be registered without a location, and `locationId` is optional. We changed the requirement rather than forcing the schema."

**Point at, do not read:** the hop table in the chain file, and the before/after of CR-029. If time is short, say the FR-12 path once and land on CR-029.

**If asked "did you implement all Must requirements":** no. The product baseline is 32 approved Musts; delivery is row by row in the RTM. **FR-11 `GET /parcels`** has no controller and is a recorded Sprint 3 gap in #289. The resident read paths are in the contract as *specified and not built* under `CR-013`.

**If asked why not force a `PENDING` row:** because the column and its CHECK constraints assume one parcel per location. Inventing a holding slot would have contradicted the model. The honest fix was to allow `locationId` to be null at registration.

**Evidence**

- `docs/01-requirements/traceability-chain-FR-12.md` — SRS → sequence diagram → `POST /parcels` → service → integration test → CI.
- `docs/02-design/uml/sequence-staff-intake.puml`
- `docs/03-api/openapi.yaml` — `POST /parcels`
- `docs/05-management/change-log.md` — `CR-028` superseded by `CR-029`
- `docs/01-requirements/RTM.md` v0.9 — current implementation statuses and gap rows

## Q&A to stand for (slides 4 and 6)

| Question | Answer |
|---|---|
| How did you validate the requirements? | An internal walkthrough on 16 October, and a prototype confirmation on 19 October with three respondents (two residents, one staff), identified by role only. Fourteen findings were checked against the files. We did not run a full acceptance test with real users. |
| Show me one requirement through to a test. | FR-12, as in `traceability-chain-FR-12.md`. Do not present a reserved test ID as an executed result. |
| What is CR-029? | A requirements correction. A `PENDING` holding slot could not be stored in `locations.parcel_id`, so the requirement became: register a parcel with an optional `locationId`. |
| Why are resident pages in the SRS but not built? | CR-013 added them to the contract; they are marked *specified and not built*. We say that plainly rather than claiming them. |
| Did you implement all 47 requirements? | No. The review slice is the staff intake-to-handover path. The rest is designed, deferred, or recorded as a gap in the RTM. |

## What not to say

- "We implemented all 47 requirements." We did not.
- "The resident pages are done." They are not built; the contract says so.
- "RTM coverage means everything is implemented." Mapping is not implementation.
- "TC-PARC-001 passed in CI." That identifier is reserved; cite the integration test and the CI run instead.
- Any respondent's name. Use role only.
- "Coverage is 85.9%." That figure is not current. Leave coverage figures to the testing slides, using the report of record.

## Revision history

| Version | Date | Change | Author |
|---|---|---|---|
| v0.1 | 2026-11-12 | First version: slide 4 process notes and slide 6 FR-12 chain plus CR-029, with validation evidence, Q&A and wording guardrails | R3 |
