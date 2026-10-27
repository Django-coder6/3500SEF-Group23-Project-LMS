# Requirements Walkthrough Minutes

Date: 2026-10-16
Duration: 60 minutes (19:00–20:00)
Location: synchronous meeting (video + shared document)
Facilitator: R2 (Product Owner)
Attendees: R2, R3 (SRS owner), R8 (RTM/test planning), R4 (prototype/UX), R5 (frontend), R6 (backend), R1 (Project Lead)
Absentees: none

Purpose
-------
Run a requirements verification walkthrough over the four requirements changed by recent evidence and listed in change-log: CR-003, CR-004, CR-005 and CR-006. Use the five-course checks (Validity, Consistency, Completeness, Realism, Verifiability) and close every finding with an owner, a concrete action and a closure criterion. Any artefact change resulting from this meeting will be recorded as a change record in docs/05-management/change-log.md per process.

Scope reviewed
---------------
- CR-003: Batch registration (raised to must-have per staff survey S3)
- CR-004: Storage-location recommender (downgraded to should-have; manual control requested)
- CR-005: Delegated pickup authorisation code (moved out of must-have; future work)
- CR-006: Parcel status model (converged to: `registered / shelved / collected`)
- Related SRS entries and contract items that the CRs touch (IS-8, OS-3, FR/NFR entries referenced in SRS chapter 3)

Method
------
Each requirement was read aloud. For each requirement we applied the five checks and recorded findings. For every finding we agreed an owner who will carry out the follow-up and a closure criterion that proves the finding is addressed (SRS revision, change-log entry, PR, RTM update, or test case addition). R3 will make the SRS edits; R8 will update the RTM; R2 will raise any additional change records required by the meeting where they do not already exist.

Findings and actions
--------------------
1) CR-003 — Batch registration (staff bulk intake)
- Checks:
  - Validity: Confirmed. Staff survey S3 (7 respondents) shows 4/7 identified bulk intake as a major delay source. Stakeholder acceptance: staff (site operators) explicitly requested this feature.
  - Consistency: No conflicting requirement found that forbids batch intake. No resident-facing endpoint is implicated.
  - Completeness: UI/UX details are missing for the bulk import flow (CSV vs UI multi-entry). Error handling and partial-success semantics (partial import, rollback) are not specified in SRS.
  - Realism: Backend feasibility assessed as medium effort; Flyway/DB constraints require transactional handling for bulk writes (R6). Scheduled for Sprint 2 as per PLAN.md.
  - Verifiability: Acceptance criteria exist at a high level in change-log but need explicit Given/When/Then test cases and RTM mapping.
- Finding: SRS lacks the detailed acceptance criteria and the transaction/partial-failure behaviour is unspecified.
- Action: R3 to update SRS FR-18 to include explicit Given/When/Then acceptance criteria for batch registration, including error/partial-success behaviour; add a note that the operation must be atomic per collection-point within a single CSV upload unless a streaming partial-apply mode is chosen (decision TBD). R8 to add at least two test cases to RTM mapping to the batch flow (happy path + partial failure).
- Owner: R3 (SRS update), R8 (RTM/test cases)
- Due: 2026-10-20
- Closure criterion: SRS PR merged that updates FR entry; RTM rows added and a PR that references the SRS PR; change-log notes the SRS update (if a new CR is needed, R2 to open it).
- Status: **Open.** `FR-18` still has no acceptance criteria and no partial-failure behaviour. R3 owns the edit; a draft of the Given/When/Then block and the partial-failure rule is prepared for him to review. The contract already states the rule, in the `/parcels/batch` description: each entry is validated on its own so one bad row does not discard the rest of the batch, and the response reports `created` and `rejected` separately. The SRS is what is missing, not the semantics.

2) CR-004 — Storage-location recommender (zone suggestion)
- Checks:
  - Validity: Re-survey confirms low staff interest (S4 0/7 chose it) and staff preference for manual control (S5 4/7 want manual). Business rationale changed accordingly.
  - Consistency: SRS previously had language that could be interpreted as "automatic assignment" in places (contract earlier drafts). This conflicts with the new decision "recommend only; never auto-apply".
  - Completeness: Need a short UI flow and a clear operator action: how a recommendation is presented, how an operator accepts/overrides it, and whether a confidence score or reason is shown.
  - Realism: Recommender algorithm (score-based zone rules) is slated for Sprint 2 design but full automated placement is not accepted; implementing a suggest-only surface is low-to-medium effort.
  - Verifiability: Acceptance criteria must assert that the system never moves a parcel without operator confirmation; tests must assert operator-triggered action required.
- Finding: SRS and contract must be tightened to state “recommend only; operator must confirm to apply” and remove any wording implying auto-apply.
- Action: R3 to update SRS text for the affected FR(s) and API contract to specify an operator-acknowledgement step for placement; R6/R4 to add UI contract (frontend) language showing confirm/accept path; R8 to add RTM tests asserting manual confirmation is required.
- Owner: R3 (SRS/contract text), R4 (UI wording), R8 (RTM)
- Due: 2026-10-20
- Closure criterion: PR(s) merging SRS and contract edits that explicitly state "recommend only — operator must accept to apply"; RTM test cases added; change-log updated to reference the PR(s).
- Status: **Closed.** `FR-19` and `FR-20` both state that the suggestion "shall never be applied automatically", and the `/locations/recommend` description in `openapi.yaml` records the same rule as the contract's own wording. No SRS or contract edit was needed; the wording was already there when the finding was written.

3) CR-005 — Delegated pickup authorisation code (delegated collection)
- Checks:
  - Validity: Resident questionnaire R5 showed 0/11 residents selected delegated collection; evidence supports deprioritising it.
  - Consistency: SRS still contains IS-8 which names delegated collection in scope; change-log records CR-005 as moved to future work, but SRS scope has not been fully reconciled resulting in a contradiction.
  - Completeness: If delegated collection is deferred, any UI/API endpoints and schema fragments that refer to delegated pickup must be removed or marked as future work in SRS and contract.
  - Realism: Deferred to future work; no implementation in sprint 2.
  - Verifiability: Tests referencing delegated collection must be removed from RTM or marked as future.
- Finding: Contradiction detected — IS-8 still lists delegated collection as in-scope while CR-005 marks it out. This is an SRS/contract inconsistency.
- Action: R3 to remove or move IS-8 out of scope in SRS; R2 (Product Owner) to raise a short CR entry if one does not already exist that records the scope removal (CR-005 status update); R8 to mark any RTM rows that assume delegated collection as "deferred/future".
- Owner: R3 (SRS edit), R2 (raise CR status update), R8 (RTM update)
- Due: 2026-10-20
- Closure criterion: SRS updated and merged, RTM updated, and change-log contains a CR recording the scope reconcilliation (CR-005 status change) with links to PRs.
- Status: **Closed.** `IS-8` now reads "Handling of exceptions: overdue, damaged, wrong-recipient, refused or lost parcels" and names delegated collection nowhere. `FR-9` and `FR-10` carry `[Future work]`, and `CR-005` records the scope change as `Approved`.

4) CR-006 — Parcel status (registered / shelved / collected)
- Checks:
  - Validity: Agreed — the three-state model captures the system boundary while delivery transit remains external.
  - Consistency: Need to check ERD, state machines and openapi contract for matching enumerations. There is a risk that some artifacts still use previous names (e.g., `in_transit`) which would cause runtime mapping issues.
  - Completeness: Define transitions and the allowed actor for each transition (who can move from `registered` to `shelved`, from `shelved` to `collected`). Define automated transitions if any (none expected).
  - Realism: Straightforward to implement; DB enum updates / migration required (R7/R6) — low-medium effort.
  - Verifiability: Tests must assert allowed transitions and that unknown states are rejected.
- Finding: Several artefacts (ERD, openapi.yaml contract, and backend state-machine draft) need small edits to align on the three-state vocabulary.
- Action: R6 and R7 to update ERD and backend enums and include a Flyway migration plan; R3 to update SRS enumeration lines; R8 to add RTM test cases covering allowed transitions and invalid transitions.
- Owner: R6/R7 (schema + migration), R3 (SRS), R8 (RTM)
- Due: 2026-10-22
- Closure criterion: PRs merging SRS, ERD and migration; RTM updated; change-log notes the implemented alignment (CR-006 implemented when Sprint 2 begins).
- Status: **Closed.** `ERD.md` gives `Parcel.status` as `enum(registered, shelved, collected)`, the contract carries the same enum, and the three state-machine sources use it. `out_for_delivery` and `ready_for_pickup` survive only in a note explaining why they were removed. The migration is Sprint 2 work under `T-30-03`.

Additional cross-cutting findings
--------------------------------
A) Billing scope contradiction
- Symptom: OS-3 states billing will not be done, but PLAN.md and some design artefacts have a "billing engine" design and the log mentions an overdue fee engine (T-20-16). This conflict must be resolved.
- Action: R1 and R2 to decide this week whether billing is in scope (8-week vs 16-week horizon per R1 decision). If billing stays out of scope, R3 must remove billing items from SRS and any references in design docs; if billing is required, a CR must be raised to reinstate billing and record the impact. R1 to make the final call and record it as a CR.
- Owner: R1 (decision), R2 (record CR), R3 (SRS edits)
- Due: 2026-10-20
- Closure criterion: Decision recorded as a CR in change-log and SRS/design edited accordingly.
- Ruling (2026-10-19): **eight weeks.** Payment and settlement are out; the overdue fee calculation is in and is delivered by `T-44-01`. `OS-3` already distinguishes the two, so the contradiction was a misreading of `OS-3` rather than a scope conflict. `PLAN.md`'s `T-44-01` builds the calculation, not the payment, so the plan and the SRS agree. Recorded as `CR-022`.
- Status: **Closed.** R1 ruled on 19 October that the horizon is eight weeks. Payment and settlement stay out of scope; the overdue fee *calculation* stays in scope and is delivered by `T-44-01`. `OS-3` already draws that line, so no SRS edit is needed. Recorded as `CR-022`.

B) Delegated collection appearing in SRS (duplicate of CR-005 inconsistency)
- See CR-005 actions above. Owner: R3 / R2. Due 2026-10-20.

Meeting decisions
-----------------
- All four CRs (CR-003, CR-004, CR-005, CR-006) remain as the current decisions in change-log: CR-003 → must-have (Sprint 2 staff feature), CR-004 → should-have (recommend only), CR-005 → deferred/future, CR-006 → three-state status model. No reversals.
- For each CR where wording ambiguity existed we resolved to update the SRS and contract text to remove ambiguity rather than leave it implicit.
- Any SRS edit derived from this meeting will be accompanied by a PR that references this minutes file and will be listed in change-log as required by the change process.

Follow-up actions (summary)
---------------------------
- **R3: outstanding.** Apply the `FR-18` acceptance criteria and the partial-failure rule. Finding 1 is the only one still open. (Due 2026-10-20)
- **R8: done.** The matrix landed as `docs/01-requirements/RTM.md` in PR #155 with every must-have mapped to a reserved test case. (Closed)
- **R6/R7: carried to Sprint 2.** The three-state vocabulary is aligned in `ERD.md`, `openapi.yaml` and the state machines; the Flyway migration that creates the schema is `T-30-03`, which needs the backend skeleton from `#137` first. (Due 2026-10-22, moved to Sprint 2)
- **R2: done.** `CR-005` records the delegated-collection scope change and `CR-011` had already recorded the `IS-8` reconciliation; `CR-022` records the billing ruling. (Closed)
- **R1: done.** The billing scope decision is recorded in `CR-022`: eight weeks, payment out, calculation in. (Closed)

Artifacts produced or referenced
--------------------------------
- This minutes file: docs/05-management/minutes/20261016_requirements-walkthrough.md
- change-log.md (existing) — any new CRs will be added here
- SRS (docs/01-requirements/specification/SRS.md) — to be edited by R3
- RTM (docs/01-requirements/RTM.md) — R8 to update
- PRs that implement SRS/contract/ERD/migration edits will reference this minutes file and the CRs they close.

Closure statement
-----------------
Each finding in this minutes file has an owner, an action and a measurable closure criterion, and since 19 October a `Status` line naming the evidence that satisfies it. The meeting closed after every attendee verbally agreed the recorded owners and dates. R2 will follow up on 2026-10-18 to ensure CRs were opened where required and that PRs are in draft.

Signed-off-by: R2 (Product Owner)

References
----------
- change-log: docs/05-management/change-log.md
- SRS: docs/01-requirements/specification/SRS.md
- PLAN: docs/05-management/plans/PLAN.md
- Survey evidence: docs/01-requirements/elicitation/survey-results.md (S3, S4, S5, R5 referenced in the meeting)

