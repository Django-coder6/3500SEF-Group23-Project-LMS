# Product Backlog

This Product Backlog is ordered by business value (highest first). Each row includes: ID, Item, FR / NFR (R3), MoSCoW priority, Business value rationale (with survey / change-record references), Acceptance criteria in Given / When / Then format, and Sprint assignment.

> Source evidence used for prioritisation: employee survey S3, S4, S5; resident survey R5; change-log entries CR-003, CR-004, CR-005, CR-006; PLAN.md (Sprint allocations). See change-log.md for the records that changed priorities.

| ID | Item | FR / NFR (R3) | MoSCoW | Business value rationale (evidence) | Acceptance criteria (Given / When / Then) | Sprint |
|---|---|---:|---|---|---|---:|
| BL-001 | Batch registration (bulk intake of multiple parcels) | FR-18 (R3) | Must | CR-003: promoted to must-have because employee survey S3 showed 4/7 staff indicated bulk intake is the largest delay source; reducing staff time has high operational value and directly reduces throughput bottleneck. | Given staff at intake with multiple parcels, When they upload/scan a batch CSV or use batch-scan flow, Then the system registers all parcels, returns a summary with errors and assigns shelf locations or suggestions for each successfully registered parcel. (Validations: duplicates flagged; failed rows reported) | Sprint 2 — T-42-01 |
| BL-002 | Parcel registration and core lifecycle (register -> shelved -> collected) | FR-12, FR-4 (R3) | Must | Core system functionality: CR-006 narrows parcel status to registered / shelved / collected because delivery stages are out of system boundary; this is the minimal lifecycle required for traceability and user visibility. | Given a courier delivers a parcel, When staff registers it, Then the parcel status becomes `registered`; Given staff places parcel on shelf, When they mark it shelving-complete, Then the status becomes `shelved`; Given resident picks up parcel, When staff or resident completes pickup flow, Then status becomes `collected` and history recorded. | Sprint 2 — T-42-01 |
| BL-003 | Staff shelving UI and inventory lookup (manual shelf assignment and lookup) | FR-14, FR-15, FR-16 (R3) | Must | Employees require low-friction operations; S5 shows 4/7 want manual control over location decisions and S4 had 0/7 selecting auto-recommend as required. To avoid operational errors, shelving must be manual with optional suggestions (CR-004 downgraded). | Given a parcel is registered, When staff opens shelving UI, Then they can search available slots, assign a shelf location, and confirm placement; the UI shows suggested slots but does not auto-apply them. | Sprint 2 — T-42-03 |
| BL-004 | Resident pickup flow: claim code display and collection confirmation (no delegated authorization code) | FR-8, FR-21, FR-22, NFR-4 (R3) | Must | Resident convenience (fast & transparent) is critical; resident survey R5 shows 0/11 selected delegated-collection authorization, therefore CR-005 removed from must-have and delegated codes moved to future work. Pickup must be simple and testable. | Given resident receives notification and a pickup code, When they present the code at collection (or confirm in-app), Then staff/device marks parcel as collected and resident receives confirmation. | Sprint 2 — T-42-04, T-42-07 |
| BL-005 | Notifications: arrival and ready-for-collection notices to residents (email/SMS) | NFR-1, FR-6, FR-7 (R3) | Must | Residents expect transparency (speed and status). Notifications reduce enquiry load on staff and increase perceived speed. Supported by resident feedback favoring fast/transparent updates. Evidence: test-plan.md (notification delay is a high-priority test area), NFR-1 target (5 minutes / 60 minutes acceptable), survey-results.md (6/11 residents prefer 5-minute updates). | Given a parcel's status changes to `shelved`, When the system has resident contact info, Then an automated notification (email / SMS) is queued and sent with parcel ID and pickup instructions. | Sprint 3 — T-43-01 |
| BL-006 | Slot recommendation (non-automatic suggestions only) | FR-19, FR-20, NFR-5 (R3) | Should | CR-004 downgraded: employee survey S4: 0/7 selected auto-apply; S5: 4/7 prefer manual control. Therefore ranking as should-have: system may suggest but must not auto-apply recommendations. | Given a parcel to be shelved, When staff opens shelving suggestions, Then the system displays ranked suggestions with a rationale and confidence, and staff must explicitly accept a suggested slot to apply it. | Sprint 2 — T-42-02 |
| BL-007 | Admin dashboard: basic reporting and parcel metrics | FR-26, FR-29, FR-31, NFR-9, NFR-10 (R3) | Should | Management needs visibility for continuous improvement. Low implementation complexity and medium business value. | Given an admin is logged in, When they open the dashboard, Then they see weekly counts of registered/shelved/collected parcels, average intake time, and a list of recent exceptions. | Sprint 3 — T-44-03 |
| BL-008 | User account & consent management (privacy & contact info) | FR-1, FR-45, NFR-6, NFR-11 (R3) | Must | Necessary for notifications and data protection compliance. Required before sending contactable notifications. | Given a resident registers contact details, When they opt-in to notifications, Then the system stores consent and uses contact info for notifications; Given admin requests data export, When proper authorization is provided, Then resident data can be exported per policy. | Sprint 2 — T-41-01 |
| BL-009 | Exception handling: missing parcel, wrong parcel, damaged item workflow | FR-23, FR-24 (R3) | Must | Operational risk: disputes cause staff time and resident dissatisfaction. Having a clear exception workflow reduces resolution time and liability. | Given a resident reports a missing or damaged parcel, When staff opens exception case, Then the system records case details, links to parcel registration, and tracks resolution steps and final status. | Sprint 3 — T-43-04 |
| BL-010 | Audit log & traceability (immutable events) | FR-25, FR-47, NFR-9 (R3) | Must | For accountability and testing/verifiability of requirements. Verifiability is a priority per walkthrough needs. | Given any parcel lifecycle action, When action completes, Then the system appends an auditable event with actor, timestamp, and change details. | Sprint 2 — T-41-03 |

## Not this time (Will-not-have this Sprint / Release)
List of functionality explicitly excluded from current scope and the rationale (evidence-based):

- Online payment integration (will-not-have): Payment processing was not prioritized by any administrator respondents (A6: 0/4 selecting fee settings). Billing moved out of must-have pending R1 final decision; see change-log CR-A6. This reduces scope and implementation risk in the initial release.
- Native mobile app (will-not-have): Current plan targets a responsive web UI; building native apps is medium-term and not required for early validation of core workflows.
- Direct courier platform integration (will-not-have): Courier-side delivery stages are out of the system boundary — status simplified to registered/shelved/collected (CR-006). Integrations can be future work once core operations are stable.
- Delegated collection authorization codes (CR-005) (will-not-have): Resident survey R5 showed 0/11 interest; moved to future work.

## Traceability notes
- Each must-have item above references FR / NFR identifiers from R3 (SRS Chapter 3). When R8 produces the RTM, these backlog items will be cross-referenced to specific test cases.

## Change records reflected
- CR-003 (batch registration): promoted to Must — reflected in BL-001 and Sprint 2 priority.
- CR-004 (slot recommendation): downgraded to Should — recommendations only; not auto-applied (BL-006 / BL-003 rationale).
- CR-005 (delegated collection code): removed from must-have and marked future work (Not this time).
- CR-006 (parcel status): status set to registered / shelved / collected — reflected in BL-002.

## Notes for reviewers
- Business rationale lines cite the survey IDs and change-record IDs that justify priority decisions. See change-log.md for full records.
- Acceptance criteria use Given / When / Then so they can be copied as test cases by R8.
- Sprint assignments follow PLAN.md; confirm Sprint 2 task table aligns with the Sprint 2 subset here.

---

Generated by: Product Owner (Sprint 1 closeout). For traceability, please review and approve with at least one non-author reviewer.
