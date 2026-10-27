# Frontend Design System

| Item | Detail |
|---|---|
| Status | Accepted |
| Version | v1.0 |
| Date | 2026-10-13 |
| Task | T-20-06 |
| Owners | R4 Frontend Lead, R5 Frontend Developer |
| Scope | the resident, staff, owner and administrator Web pages of the community parcel collection point system |
| Related | `docs/02-design/adr/ADR-003-frontend-architecture.md`, `docs/02-design/frontend-page-map.md`, `docs/01-requirements/specification/SRS.md`, `docs/03-api/openapi.yaml`, `docs/02-design/database/ERD.md`, `docs/02-design/backend-state-machines.md` |

## 1. Purpose

Two people build the frontend, and both must produce one consistent product. A
design system freezes the visual and interaction decisions once, in one place, so
that a page is assembled from shared decisions instead of re-decided on every
screen. It is the UI-layer part of the "design and implementation" activity: the
reusable design decisions that the rest of the frontend is built from.

This document is the single source of truth for colour, type, spacing, radius,
motion, components, tables, forms, responsive behaviour and accessibility. If a
value is not here and not a token, it does not belong in a page.

### 1.1 Relation to the archived reference prototype

`docs/02-design/prototype/` (R4, merged in PR #62) stays as design history. Its
CSS variables are the starting palette for the primitives in section 5, but it is
not the implementation: ADR-003 requires its structure to be converted into Vue
components rather than copied, and its own README records what the team has since
decided differently (no hardware, no courier account, English interface). This
document, not the prototype, is the visual authority for the application.

## 2. Sources

Every rule below traces to one of three levels. A rule that cannot name its
source should not be added.

| Level | Source | What it fixes |
|---|---|---|
| Course | Course Guide Topic 7 "Design and Implementation"; Lecture 2 pp.14-18 (reuse-oriented process, COTS), pp.25, 37-38 (prototyping, usability); Lecture 4 pp.25-26 (usability as a testable non-functional requirement); Lecture 6 pp.25-28, 33, 47-48 (layered architecture, MVC, the user-interface layer) | Why a design system exists, why we reuse a component library, why usability and mobile support are requirements |
| Standard | W3C Design Tokens Community Group format, `design-tokens/community-group`; Element Plus theming guide, `element-plus/element-plus`; GitHub Primer primitives, `primer/primitives` | Token structure and naming, the Element Plus variable boundary, contrast and interaction-state rules |
| Project | `docs/01-requirements/elicitation/survey-results.md` section 7 (375 px target; R2: 9 of 11 residents use a phone); `ADR-003` (Element Plus, plain JavaScript); `frontend-page-map.md` (component list, roles); `docs/02-design/database/ERD.md` and `backend-state-machines.md` (status vocabulary); `openapi.yaml`; `docs/02-design/prototype/styles.css` (the starting palette) | The actual values this project uses |

## 3. Principles

| # | Principle | Source |
|---|---|---|
| P1 | Mobile first. The baseline viewport is 375 px; the desktop layout only has to remain usable. | survey section 7; R2 (9 of 11) |
| P2 | Usable by occasional users with no training: few steps, self-explanatory labels. | survey R1; Lecture 4 usability requirement |
| P3 | One source of truth for visual values. A page never hard-codes a hex, px or ms value; it uses a token. | Primer primitives, core rule |
| P4 | Reuse before rebuild. Use Element Plus where it fits; build custom only where it does not. | Lecture 2 reuse / COTS; ADR-003 |
| P5 | English interface. Repository content, documents and the interface are in English. | `docs/glossary.md` |

## 4. Token architecture

Tokens follow the W3C DTCG shape: a token is a name, a value and a type, and
tokens are grouped. We deliver them as CSS custom properties, in three tiers:

| Tier | Meaning | Example |
|---|---|---|
| Primitive | a raw value, no meaning attached | `--slate-500: #778899` |
| Semantic | a role, aliased to a primitive | `--color-focus: var(--slate-500)` |
| Component | a component-scoped alias, only when a component must differ | `--table-header-bg: var(--color-surface-soft)` |

Rules:

- Components consume **semantic** tokens, never primitives and never raw values.
- A semantic token is an alias: it must resolve to a primitive, not repeat a hex.
- Adopting a JSON token pipeline is out of scope for now. It would add a build
  dependency, and ADR-003 requires a new dependency to be discussed first.

## 5. Colour

### 5.1 Primitives

| Token | Value | Role |
|---|---|---|
| `--ink-900` | `#21262b` | shell, body text, primary action |
| `--grey-400` | `#a9a9a9` | page base |
| `--paper-100` | `#f1f1f1` | surfaces, text on the shell |
| `--slate-500` | `#778899` | the single accent: focus, soft fill, current navigation item |
| `--green-600` | `#16825d` | success |
| `--amber-600` | `#a6610b` | warning |
| `--red-600` | `#b42318` | danger |
| `--blue-600` | `#246da8` | information |

One accent, four status roles and neutrals. The interface is deliberately
greyscale, so colour carries status only and never decoration.

### 5.2 Semantic

| Token | Value | Used for |
|---|---|---|
| `--color-bg` | `--grey-400` | page background |
| `--color-bg-gradient` | derived | page background, grey to a deeper grey |
| `--color-surface` | `--paper-100` | cards, tables, dialogs |
| `--color-surface-soft` | derived | table header, hover rows |
| `--color-border` | derived | dividers, inputs |
| `--color-text` | `--ink-900` | body text |
| `--color-text-muted` | derived | secondary text, meta |
| `--color-primary` | `--ink-900` | primary action |
| `--color-primary-strong` | derived | pressed primary |
| `--color-primary-soft` | derived | selected row, subtle fill |
| `--color-success` | `--green-600` | success feedback |
| `--color-warning` | `--amber-600` | warning feedback |
| `--color-danger` | `--red-600` | danger, destructive action |
| `--color-info` | `--blue-600` | information feedback |
| `--color-focus` | `--slate-500` | focus outline |
| `--color-on-primary` | `--paper-100` | text on the primary fill |
| `--color-sidebar` | `--ink-900` | shell surface |
| `--color-sidebar-gradient` | derived | the shell as a frosted pane, more transparent at the bottom |
| `--color-sidebar-text` | `--paper-100` | text on the shell |
| `--color-sidebar-active-bg` | derived | the current navigation item |
| `--color-sidebar-hover-bg` | derived | a navigation item under the pointer |
| `--color-surface-glass` | derived | frosted panel over the gradient |
| `--color-surface-glass-gradient` | derived | the same panel, thicker at the top |
| `--color-accent` | `--slate-500` | the accent: focus, tints, indicators, selected states |
| `--color-accent-strong` | derived | accent text that must reach 4.5:1, such as a link |
| `--color-field` | derived | form controls, dimmer than a surface |

`derived` means the value is a `color-mix` of two primitives, so it still names no
raw value of its own.

### 5.3 Status semantics

Status colour is business semantics, not decoration. The colour role of a state
is fixed here; the state names come from the data dictionary and the contract, and
`StatusTag` reads one mapping table and nothing else.

Colour roles:

| Role | Token | Meaning |
|---|---|---|
| info | `--color-info` | recorded, in hand, not yet actionable |
| success | `--color-success` | good, available, ready |
| warning | `--color-warning` | needs attention, or time-limited |
| danger | `--color-danger` | a problem, or an action that cannot be undone |
| neutral | `--color-text-muted` | finished, inactive, or not applicable |

Mapping (values from `docs/03-api/openapi.yaml` and
`docs/02-design/database/ERD.md`):

| Domain | Value | Role |
|---|---|---|
| Parcel | `registered` | info |
| Parcel | `shelved` | success |
| Parcel | `collected` | neutral |
| Storage location | `available` | success |
| Storage location | `reserved` | warning |
| Storage location | `occupied` | info |
| Storage location | `disabled` | neutral |
| Pickup code | `active` | success |
| Pickup code | `used` | neutral |
| Pickup code | `expired` | warning |
| Pickup code | `revoked` | danger |
| Exception ticket | `open` | danger |
| Exception ticket | `processing` | warning |
| Exception ticket | `resolved` | success |

The parcel enumeration is these three values, ruled by CR-006 and consistent
across `docs/03-api/openapi.yaml`, `docs/02-design/database/ERD.md` and
`docs/02-design/backend-state-machines.md`. An exception is not a parcel state:
it is a separate `ParcelException` ticket (`open -> processing -> resolved`)
recorded against the parcel, which keeps the status it had.

Exception types (`overdue`, `damaged`, `wrong_recipient`, `refused`, `lost`) are
categories, not states. They are shown as a neutral chip and never carry a status
colour.

### 5.4 Contrast

| Context | Minimum ratio | Standard |
|---|---|---|
| Body text on its background | 4.5:1 | WCAG AA |
| Large text (at or above the title size) and UI boundaries | 3:1 | WCAG AA |

Colour is never the only carrier of meaning: every status also shows its text
label, so the interface still reads in greyscale.

### 5.5 Frosted surfaces

Panels over the page gradient use `--color-surface-glass-gradient`: `--paper-100`
mixed with transparency rather than a flat surface, so a panel reads as glass and the
gradient shows through it. The blur is one token, `--blur-glass`, not a value written
per component. A static blur may stay under `prefers-reduced-motion`; only a blur that
moves must be dropped. Body text on every glass surface still meets the 4.5:1 rule of
section 5.4, because the surface resolves to near-white at either end of the gradient.

## 6. Typography

| Token | Size / line-height / weight | Used for |
|---|---|---|
| `--text-display` | 24 / 1.25 / 700 | page hero figure, metric value |
| `--text-title-lg` | 20 / 1.3 / 700 | page title |
| `--text-title` | 16 / 1.4 / 600 | section and card title |
| `--text-body` | 14 / 1.5 / 400 | default body text |
| `--text-body-sm` | 13 / 1.5 / 400 | tables, dense lists |
| `--text-caption` | 12 / 1.4 / 400 | labels, meta, help and error text |
| `--text-code` | 13 / 1.4 / 400 monospace | waybill numbers, pickup codes, identifiers |

- `--font-family-base`: `Inter, "Noto Sans SC", "Microsoft YaHei", system-ui, sans-serif`.
- Sizes are the mobile baseline. A page may step one level up at the desktop
  breakpoint, never more.
- Use a shorthand (size + line-height + weight together) so the three never drift.

## 7. Spacing, radius and elevation

### 7.1 Spacing (4 px base)

| Token | px | Typical use |
|---|---|---|
| `--space-1` | 4 | icon to label |
| `--space-2` | 8 | inside a tag or button |
| `--space-3` | 12 | between form controls |
| `--space-4` | 16 | card padding, mobile page gutter |
| `--space-6` | 24 | between cards and sections |
| `--space-8` | 32 | page top spacing |
| `--space-12` | 48 | empty-state spacing |

### 7.2 Radius

| Token | px | Used for |
|---|---|---|
| `--radius-sm` | 4 | inputs, buttons |
| `--radius-md` | 8 | cards, table container |
| `--radius-lg` | 12 | panels, dialogs |
| `--radius-xl` | 20 | glass panels |
| `--radius-pill` | 999 | status pills, chips |

### 7.3 Elevation

| Token | Used for |
|---|---|
| `--shadow-resting` | cards at rest |
| `--shadow-floating` | dropdowns, drawers, dialogs |

Both shadow tokens are primitives in `tokens.css`, because a shadow needs an alpha
channel that no colour token carries: `--shadow-resting` is
`0 1px 2px rgb(33 38 43 / 9%), 0 10px 24px rgb(33 38 43 / 16%)` and `--shadow-floating` is
`0 18px 44px rgb(33 38 43 / 24%)`.

Elevation pairs with a z-index layer; a floating element must sit above the
content it covers, and the two must not be chosen independently.

## 8. Motion

| Token | Value | Used for |
|---|---|---|
| `--duration-fast` | 100 ms | hover, focus |
| `--duration-base` | 200 ms | state change |
| `--duration-slow` | 300 ms | enter and exit |
| `--ease-standard` | `cubic-bezier(0.2, 0, 0, 1)` | all of the above |

Motion is functional, never decorative, and every transition is disabled under
`prefers-reduced-motion: reduce`.

## 9. Element Plus boundary

Element Plus is approved by ADR-003 and is introduced page by page. It is themed
through its CSS variables, not by editing its components. Following the Element
Plus theming guide, overrides are grouped in one stylesheet and, for performance,
applied at a layout class rather than globally on `:root` where possible.

### 9.1 Use Element Plus unmodified

`Table`, `Pagination`, `Form`, `Input`, `Select`, `DatePicker`, `Dialog`,
`Message`, `Tooltip`, `Tabs`.

### 9.2 Override to our tokens

| Element Plus variable | Our token |
|---|---|
| `--el-color-primary` | `--color-primary` |
| `--el-color-success` | `--color-success` |
| `--el-color-warning` | `--color-warning` |
| `--el-color-danger`, `--el-color-error` | `--color-danger` |
| `--el-color-info` | `--color-info` |
| `--el-border-radius-base` | `--radius-sm` |
| `--el-font-family` | `--font-family-base` |
| `--el-text-color-primary` | `--color-text` |
| `--el-border-color` | `--color-border` |
| `--el-bg-color` | `--color-surface` |

### 9.3 Build ourselves

Element Plus has no business meaning for these, so they are ours, built from
tokens: `StatusTag`, `LocationGrid`, `MetricCard`, `EmptyState`, `ErrorState`,
`LoadingState`, `PageHeader`, `AppLayout`.

### 9.4 Rule

Override once, centrally. A page must not restyle an Element Plus component
locally unless the page map records it as a deliberate exception.

## 10. Component rules

| Component | Rule |
|---|---|
| `AppLayout` | Sidebar 220 px, persistent from 641 px up; at 640 px and below it collapses to a single row above the content. Surface and border come from tokens. |
| `PageHeader` | Title uses `--text-title-lg`; description uses `--color-text-muted`; actions align right and are the only primary button on the page. |
| `StatusTag` | Maps a status value to its label and colour role in section 5.3. Pill radius, caption text, always shows the label. |
| `ParcelTable` | See section 11. |
| `LocationGrid` | One cell per storage location, filled with the location status token; the selected cell uses a `--color-focus` outline. |
| `MetricCard` | Value uses `--text-display`; label uses `--text-caption` and `--color-text-muted`. |
| `ConfirmDialog` | Element Plus `Dialog` with our tokens; a destructive confirm uses `--color-danger`. |
| `EmptyState` | Shown when a list is legitimately empty; caption text plus at most one primary action. |
| `ErrorState` | Renders the normalised API error (`{ status, code, message }`) in the danger tone with a retry action. |
| `LoadingState` | Skeleton or spinner that reserves the final layout size, so content does not shift when it arrives. |

## 11. Table rules

- Page size 20 by default; row height 44 px on mobile, 40 px on desktop.
- Header is sticky and uses `--color-surface-soft`; rows use `--color-surface`.
- The status column renders `StatusTag`; it never renders a raw enum string.
- Numbers and timestamps are right-aligned; dates are stored in the contract
  form and formatted for display only.
- Empty result renders `EmptyState`; a failed load renders `ErrorState`.
- Row actions sit in a fixed trailing column.
- Below 640 px the table becomes a card list: the title plus key-value rows.

## 12. Form rules

- Label position: above the field below 640 px, to the left at and above it.
- Every field has a visible label; a placeholder never replaces a label.
- Validate on blur and on submit; required fields are marked.
- Error text uses `--text-caption` and `--color-danger`, placed directly under the
  field. Help text uses `--color-text-muted`.
- The submit control is disabled while the request is in flight and shows
  `LoadingState`; a form has exactly one primary action.

## 13. Responsive rules

| Breakpoint | Width | Layout |
|---|---|---|
| base (mobile first) | 375-640 px | single column; the sidebar collapses to a single row above the content; tables become card lists; metric cards stack |
| md | 641-1023 px | the sidebar stays; two-column content where useful; tables drop to their priority columns |
| lg | 1024 px and above | full tables; multi-column dashboard |

The design target is the phone. The desktop layout only has to remain usable
(survey section 7). These breakpoints are the same ones the page prototype in
`docs/02-design/ui-prototype.md` uses.

## 14. Accessibility

- Contrast meets WCAG AA (4.5:1 text, 3:1 large text and UI).
- Focus is shown with a 2 px `--color-focus` outline at 2 px offset, on
  `:focus-visible`; the outline is never removed.
- Touch targets are at least 44 by 44 px.
- Status and errors are never signalled by colour alone; a label or icon always
  accompanies the colour.
- Motion is disabled under `prefers-reduced-motion: reduce`.
- Interactive elements define rest, hover, focus-visible, active and disabled
  states.

## 15. Out of scope

Dark mode, right-to-left layout, native mobile applications, print layout, and
any collection-point hardware interface. Each is a separate decision and would
need its own record.

## 16. Traceability

| Section | Traces to |
|---|---|
| 1, 1.1, 3 | Course Guide Topic 7; Lecture 2 pp.14-18, 37-38; `docs/02-design/prototype/` |
| 3 (P2, P5), 14 | Lecture 4 pp.25-26; survey conclusion 11; `docs/glossary.md` |
| 5.3 | `docs/03-api/openapi.yaml`; `docs/02-design/database/ERD.md`; `docs/02-design/backend-state-machines.md` |
| 11, 12 | `docs/02-design/frontend-page-map.md`; `docs/03-api/openapi.yaml` |
| 5, 6, 7 | `docs/02-design/prototype/styles.css`; W3C DTCG naming; Primer primitives |
| 9 | `ADR-003`; Element Plus theming guide |
| 13 | survey section 7 (375 px, 9 of 11); `docs/02-design/ui-prototype.md` |
| 8, 14 | Primer primitives (motion, states, contrast) |



## Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v1.0 | Week 2 | First version recorded in this file. Colour, type, spacing and component rules, implemented in `frontend/src/styles/tokens.css`. Any earlier edit is not listed: this file had no revision history until week 3, so records of those edits do not exist here. | R1 |
