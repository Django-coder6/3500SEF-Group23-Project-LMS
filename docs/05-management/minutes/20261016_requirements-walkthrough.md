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
- Action: R3 to update SRS FR-?? (the FR id that CR-003 maps to) to include explicit Given/When/Then acceptance criteria for batch registration, including error/partial-success behaviour; add a note that the operation must be atomic per collection-point within a single CSV upload unless a streaming partial-apply mode is chosen (decision TBD). R8 to add at least two test cases to RTM mapping to the batch flow (happy path + partial failure).
- Owner: R3 (SRS update), R8 (RTM/test cases)
- Due: 2026-10-20
- Closure criterion: SRS PR merged that updates FR entry; RTM rows added and a PR that references the SRS PR; change-log notes the SRS update (if a new CR is needed, R2 to open it).

2) CR-004 — Storage-location recommender (zone suggestion)
- Checks:
  - Validity: Re-survey confirms low resident interest (S4 0/7) and staff preference for manual control (S5 4/7 want manual). Business rationale changed accordingly.
  - Consistency: SRS previously had language that could be interpreted as "automatic assignment" in places (contract earlier drafts). This conflicts with the new decision "recommend only; never auto-apply".
  - Completeness: Need a short UI flow and a clear operator action: how a recommendation is presented, how an operator accepts/overrides it, and whether a confidence score or reason is shown.
  - Realism: Recommender algorithm (score-based zone rules) is slated for Sprint 2 design but full automated placement is not accepted; implementing a suggest-only surface is low-to-medium effort.
  - Verifiability: Acceptance criteria must assert that the system never moves a parcel without operator confirmation; tests must assert operator-triggered action required.
- Finding: SRS and contract must be tightened to state “recommend only; operator must confirm to apply” and remove any wording implying auto-apply.
- Action: R3 to update SRS text for the affected FR(s) and API contract to specify an operator-acknowledgement step for placement; R6/R4 to add UI contract (frontend) language showing confirm/accept path; R8 to add RTM tests asserting manual confirmation is required.
- Owner: R3 (SRS/contract text), R4 (UI wording), R8 (RTM)
- Due: 2026-10-20
- Closure criterion: PR(s) merging SRS and contract edits that explicitly state "recommend only — operator must accept to apply"; RTM test cases added; change-log updated to reference the PR(s).

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

Additional cross-cutting findings
--------------------------------
A) Billing scope contradiction
- Symptom: OS-3 states billing will not be done, but PLAN.md and some design artefacts have a "billing engine" design and the log mentions an overdue fee engine (T-20-16). This conflict must be resolved.
- Action: R1 and R2 to decide this week whether billing is in scope (8-week vs 16-week horizon per R1 decision). If billing stays out of scope, R3 must remove billing items from SRS and any references in design docs; if billing is required, a CR must be raised to reinstate billing and record the impact. R1 to make the final call and record it as a CR.
- Owner: R1 (decision), R2 (record CR), R3 (SRS edits)
- Due: 2026-10-20
- Closure criterion: Decision recorded as a CR in change-log and SRS/design edited accordingly.

B) Delegated collection appearing in SRS (duplicate of CR-005 inconsistency)
- See CR-005 actions above. Owner: R3 / R2. Due 2026-10-20.

Meeting decisions
-----------------
- All four CRs (CR-003, CR-004, CR-005, CR-006) remain as the current decisions in change-log: CR-003 → must-have (Sprint 2 staff feature), CR-004 → should-have (recommend only), CR-005 → deferred/future, CR-006 → three-state status model. No reversals.
- For each CR where wording ambiguity existed we resolved to update the SRS and contract text to remove ambiguity rather than leave it implicit.
- Any SRS edit derived from this meeting will be accompanied by a PR that references this minutes file and will be listed in change-log as required by the change process.

Follow-up actions (summary)
---------------------------
- R3: Apply SRS edits for CR-003, CR-004, CR-005, CR-006 to remove ambiguities and add acceptance criteria. (Due 2026-10-20)
- R8: Add RTM/test cases for batch registration, recommender manual-confirm, delegated-collection flagged as future, and parcel-state transitions. (Due 2026-10-20)
- R6/R7: Plan DB migration and backend enum changes for parcel status; prepare PR and Flyway migration. (Due 2026-10-22)
- R2: If any new change record is required (e.g., to reconcile IS-8 or to log the billing decision), raise CR(s) in change-log and link PRs. (Due 2026-10-20)
- R1: Make the final billing scope decision (8-week vs 16-week) and inform R2/R3. (Due 2026-10-20)

Artifacts produced or referenced
--------------------------------
- This minutes file: docs/05-management/minutes/20261016_requirements-walkthrough.md
- change-log.md (existing) — any new CRs will be added here
- SRS (docs/01-requirements/specification/SRS.md) — to be edited by R3
- RTM (docs/04-testing/RTM.md or docs/04-testing/README) — R8 to update
- PRs that implement SRS/contract/ERD/migration edits will reference this minutes file and the CRs they close.

Closure statement
-----------------
Each finding in this minutes file has an owner, an action and a measurable closure criterion. The meeting closed after every attendee verbally agreed the recorded owners and dates. R2 will follow up on 2026-10-18 to ensure CRs were opened where required and that PRs are in draft.

Signed-off-by: R2 (Product Owner)

References
----------
- change-log: docs/05-management/change-log.md
- SRS: docs/01-requirements/specification/SRS.md
- PLAN: docs/05-management/plans/PLAN.md
- Survey evidence: docs/01-requirements/elicitation/survey-results.md (S3, S4, S5, R5 referenced in the meeting)

