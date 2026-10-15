# Usability Review - UI Prototype
Owners: R9 (usability review)
Due: 2026-10-18
Related: PR #62, PLAN.md Sprint 1 deliverable 3, docs/02-design/prototype/README.md, docs/glossary.md section 7

---

## Executive Summary

This review evaluates the UI prototype against **Nielsen's ten usability heuristics**. It identifies **7 substantive findings** (exceeding the required minimum of 5). Every finding references a specific screen and a specific persona need, and each is converted into an actionable **Sprint 2 task or backlog item**.

A dedicated section evaluates the mobile experience at **375 px**, based on survey data showing that **9 of 11 residents use a phone**, most receive parcels monthly or less, and the interface must work **one-handed, first time, with no training**.

> **Note:** A review that finds nothing is not a review. Where the prototype satisfies a heuristic, this document states which one and how it was checked.

---

## Mobile Viewport Assessment (375 px)

**Requirement:** Survey section 7 confirms 9 of 11 residents use a phone, and most receive parcels monthly or less. The interface must work one-handed, first time, with no training. `design-system.md` sets **375 px as the baseline viewport**.

**Assessment:**
The prototype section 4 references responsive behaviour, and `design-system.md` section 13.1 defines the mobile-first breakpoints: 375–640 px (single column, sidebar collapses, tables become card lists).

R4's high-fidelity pages (`high-fidelity/README.md`) explicitly record a known limitation: **the 375 px viewport has not been verified** because static mockups overflow at narrow widths; real components are needed in Sprint 2 for validation.

**Specific concerns for the Resident persona:**
1. **Pickup flow buttons:** At 375 px, the primary action buttons must remain within the bottom thumb-reach zone to support one-handed, no-training pickup.
2. **Intake form:** The form has multiple fields; vertical scrolling must not separate the Submit button from the last field. This is critical for the Staff persona's fast-intake goal.
3. **Storage location board:** The board uses wide tables; `design-system.md` section 13.1 requires tables to become card lists at 375 px. This is needed for the Owner persona's quick monthly review.

**Action:** R4 must implement and verify the 375 px responsive layout in Sprint 2 using real components.

---

## Heuristic Evaluation (Nielsen's Ten Heuristics)

| # | Heuristic | Observation | Persona / Need | Severity | Recommended Change |
|---|---|---|---|---|---|
| 1 | Visibility of system status | The `/my-parcels` resident page has no contract endpoint yet, so loading and error states cannot be implemented. | Resident: needs to know if their parcel is ready for pickup | High | Define the `/my-parcels` contract and implement loading/error states (R6 + R4) → Sprint 2 issue |
| 2 | Match between system and real world | Terminology (waybill, collection point, storage location, exception parcel) is consistent across UML, OpenAPI, and glossary v1.1. | All roles: need to understand labels without training | Low | ✅ Pass. Maintain glossary as single source of truth |
| 3 | User control and freedom | Survey S5: 4/7 staff prefer manual selection or no suggestion. The prototype provides a "suggestion panel + ignore" design. | Staff: wants control over storage location assignment | Low | ✅ Pass. R2 to confirm this remains in Sprint 2 backlog |
| 4 | Consistency and standards | `AppLayout.vue` used an old light sidebar; R4 stated in PR #115 that it does not match the new greyscale palette. The archived prototype's CSS variables use a different palette and are still referenced in `ui-prototype.md` section 1.1. | All roles: need a consistent visual language | Medium | Update `AppLayout.vue` to match greyscale tokens; confirm archived prototype is not used as visual reference → Sprint 2 task |
| 5 | Error prevention | The prototype records one exception type at intake (section 5.4), but there is **no exception queue page and no working screen for exception handling**. `/exceptions` is listed as a P1 page in section 2, and `SRS IS-8` with `FR-23` to `FR-25` put exception handling in scope, so the screens a staff member needs to clear the queue are not designed. | Staff / Admin: need to handle exceptions without training | High | Design the exception queue and its handling actions; R7 models the transitions → Sprint 2 backlog (R5, R7) |
| 6 | Recognition rather than recall | Navigation and labels are clear; the glossary provides a single source of truth for terms. | Resident: needs to pick up parcel once, without remembering steps | Low | ✅ Pass. None |
| 7 | Flexibility and efficiency of use | 375 px mobile viewport is not verified at the high-fidelity stage. Desktop layout exists but mobile is the primary use case for residents. | Resident: one-handed pickup; Staff: fast handover | High | R4 to verify in Sprint 2 with real components (see Mobile section) → Sprint 2 task |
| 8 | Aesthetic and minimalist design | The interface uses a greyscale palette with colour reserved for status. This is an explicit design decision, not a defect. | Owner: needs to see monthly usage and revenue at a glance | Low | ✅ Pass. None |
| 9 | Help users recognize, diagnose, and recover from errors | 5 mentions of an `ErrorState` component exist in the prototype (L205, L251, L295, L453, L595), but **none of the 12 drawn mockups shows one**, and **no error message content is defined anywhere** (search for "error message" and "message content" returns zero matches). | Resident / Staff: need clear recovery steps | Medium | Define API error codes and the visual design/content of the `ErrorState` component for intake/pickup → Sprint 2 task (R5, R6) |
| 10 | Help and documentation | No help or documentation links are visible in the prototype. The glossary exists but is not linked from any screen. | Staff: may need quick help on exception handling | Low | Define help/documentation tooltips using glossary terms → Sprint 3 backlog (R2, R9) |

---

## Summary of Actionable Findings

| ID | Finding | Heuristic | Severity | Action Target | Assigned To |
|---|---|---|---|---|---|
| U-01 | `/my-parcels` lacks contract support and loading/error states | #1 | High | Sprint 2 backlog item | R2, R6, R4 |
| U-02 | `AppLayout.vue` uses outdated light sidebar, not matching greyscale palette | #4 | Medium | Sprint 2 task | R4 |
| U-03 | Exception queue and handling screens missing | #5 | High | Sprint 2 backlog item | R5, R7 |
| U-04 | 375 px mobile viewport not verified | #7 | High | Sprint 2 task | R4 |
| U-05 | `ErrorState` component content and error messages undefined | #9 | Medium | Sprint 2 task | R5, R6 |
| U-06 | Help/documentation links absent | #10 | Low | Sprint 3 backlog item | R2, R9 |
| U-07 | Archived prototype palette may still be referenced | #4 | Low | Verify | R4 |

---

## Sprint 2 Task Conversion

The following issues/backlog items must be created before Sprint 2 planning:

- [ ] **U-01:** Define contract for `/my-parcels` and add loading/error states
- [ ] **U-02:** Align `AppLayout.vue` with greyscale design tokens
- [ ] **U-03:** Design the exception queue page and its handling actions
- [ ] **U-04:** Implement and verify 375 px responsive layout
- [ ] **U-05:** Define API error codes and the visual design of the `ErrorState` component
- [ ] **U-06:** Define help/documentation tooltips using glossary terms
- [ ] **U-07:** Confirm archived prototype is not used as a visual reference

---

## References

- `docs/02-design/ui-prototype.md` (PR #62)
- `docs/02-design/design-system.md` (sections 3, 5.1, 13.1)
- `docs/02-design/frontend-conventions.md` (T-20-09)
- `docs/02-design/high-fidelity/README.md` (T-20-12)
- `docs/glossary.md` v1.1 (T-00-07)
- `docs/glossary.md` section 7 (exemption clause for archived artefacts)
- `docs/02-design/prototype/README.md` (exemption clause)
- `docs/05-management/change-log.md` v1.2 (CR-001, CR-013)
- Survey data: 9/11 residents use mobile; 4/7 staff prefer manual selection
- PR #115 (R4 known limitations)
- Issue #83 (T-20-10 acceptance criteria)
