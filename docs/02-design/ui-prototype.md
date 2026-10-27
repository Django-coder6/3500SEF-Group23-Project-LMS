# UI Prototype: Pages for the Four Login Roles

| Item | Detail |
|---|---|
| Status | Complete for Sprint 1 |
| Version | v1.2 |
| Date | 2026-10-13 |
| Revised | 2026-10-23 |
| Revision | 5, completing the R5 corrections for `#170` findings 1, 5 and 8 in section 5.1 |
| Task | T-20-05 (Sprint 1, week 2) |
| Owner | R5 Frontend Developer |
| Reviewers | R4 Frontend Lead, R9 (usability review, T-20-10), R3 (requirement identifiers) |
| Due | 2026-10-18 |
| Depends on | `SRS.md` (section 2.2 exception types, section 2.3 operation log, merged to develop on 2026-10-12), the SRS change log (CR-003, CR-004, CR-006, CR-009), `frontend-page-map.md` (T-30-04), `glossary.md` (T-00-07), `backend-state-machines.md` (T-20-04), `ADR-003` and `ADR-004` (T-30-04), `survey-results.md`, `Personas.md`, `docs/02-design/prototype/` (PR #62), the API contract (OpenAPI) |
| Covers | the in-scope functions of SRS section 1.5.2 (IS-1 to IS-12), the five exception types of SRS section 2.2, and the operation log scope of SRS section 2.3 (5.9). Functional requirement identifiers are added once R3 publishes SRS chapter 3 (issue #76); until then every mapping row below carries `FR: pending R3`. |
| Issue | #83. This document answers T-20-05 only. The issue also carries T-20-09 (the frontend conventions) and T-20-10 (the R9 usability review), so the pull request does not close the issue on its own. |

## 1. Purpose

Sprint 1 has to end with a design baseline, and Sprint 2 has to start building
without making new design decisions. This document is the page-level prototype
for the four login roles. For every page it fixes the blocks, the fields, the
actions, the states and the number of steps the user needs.

It is low fidelity on purpose. Colours, type scale and spacing come from
`docs/02-design/design-system.md` (R4, T-20-06) once that document lands, and are
not repeated here. Routes, view file names and component names come from
`docs/02-design/frontend-page-map.md` and are not changed here. If this document
and the page map disagree, the page map wins and this document is corrected.

This document draws the pages the week 2 task asks for: the two resident pages,
the staff workbench with intake and batch intake, the storage location board,
pickup verification, the audit log, and the admin pages. The other pages of the
page map keep their routes, views and owners there, and the high fidelity
versions of pickup, intake and dashboard belong to T-20-12. Three of them are
worth naming: the two staff parcel pages, `/parcels` and `/parcels/:id`, are
detailed in Sprint 2, the audit log is drawn in 5.9, and `/exceptions` and
`/reports` are not drawn here at all.

### 1.1 Relation to the archived reference prototype

`docs/02-design/prototype/` (owner R4, PR #62) stays as design history. ADR-003
requires its structure to be converted into Vue components rather than copied,
and its README records what the team has since decided differently.

| Reused from the reference prototype | Must not appear in the Vue application |
|---|---|
| Screen structure (overview, parcels, exceptions, users, reports, audit, settings) | Cabinet hardware, 櫃機 and 格口 records |
| Table and filter patterns | A courier as a login role |
| Status pill treatment | Cabinet door faults as an exception type |
| Overview cards above the detail tables | Chinese interface text |
| | One fixed navigation set for a single operator |

### 1.2 Contract dependencies

Two features drawn in this prototype have no operation behind them in the API
contract. They stay in the prototype as design intent — they are marked `CD-1`
and `CD-2` wherever they appear — but no Sprint 2 task may build them until R6
and R2 record a contract decision. R4 raises both with R6 and R2.

| Id | Where it appears | What is missing | Decision owner |
|---|---|---|---|
| CD-1 | The `[ Assign parcel ]` action in 5.5 and the "shelve later" option in 5.4. The same gap reaches every other action that writes to a storage location: `[ + Add location ]`, `[ Disable ]`, `[ Clear ]` and the add action of the empty state in 5.5 | An operation that assigns a storage location to a parcel that already exists, and, if `/locations` stays read only, any operation that creates or changes a location. `POST /parcels` sets `locationId` at creation, `PATCH /parcels/{id}/status` carries only `status`, `expectedVersion` and `reason`, and `/locations` has only GET and `/recommend`. A parcel that is kept `registered` without a location can therefore never be given one later, and the board cannot be maintained from the interface. | R6 and R2 (contract), R4 |
| CD-2 | The identity fallback in 5.6 and the lost-code line in 5.2 | `POST /pickup/verify` accepts only `pickupCode`, so phone number plus surname cannot be verified. The fallback needs either a new endpoint or an added field in the contract. | R6 and R2 (contract), R4 |

## 2. Page inventory by role

| Role | Landing route | Pages in this prototype | Primary owner |
|---|---|---|---|
| Resident | `/my-parcels` | 5.1 my parcel list, 5.2 parcel detail | R5 |
| Staff | `/dashboard` | 5.3 dashboard (staff variant), 5.4 intake and batch intake, 5.5 storage location board, 5.6 pickup verification | R5, R4 for 5.5 and 5.6 |
| Owner | `/dashboard` | 5.3 dashboard (owner variant), 5.9 audit log (own points) | R5 |
| Admin | `/dashboard` | 5.3 dashboard (admin variant), 5.7 users, 5.8 collection point settings, 5.9 audit log (all points) | R5 |

Route access, role names and the landing rules are taken from the page map:
Resident to `/my-parcels`, Staff, Owner and Admin to `/dashboard`, `/login` is
the only public route, and any other route redirects to 403 or 404.

## 3. Terminology

Interface text and repository content are English. The Chinese column exists
because the team discusses in Chinese; it is never displayed in the application.
Spelling follows `docs/glossary.md` exactly.

| Term used in the screens | Chinese | Meaning in this project |
|---|---|---|
| collection point | 代收點 | The site. One site, several storage locations |
| storage location | 庫位 | A numbered position on a shelf, not a physical locker |
| zone | 分區 | Large-item, small-item, perishable, long-stay |
| shelving | 上架 | Placing a parcel at a storage location |
| handover | 核銷 | Confirming the parcel reached the right person |
| pickup code | 取件碼 | One-time code the resident shows at the counter |
| waybill number | 運單號 | Courier tracking number, recognised by its prefix |
| retention period | 免費保管期 | Three days by default, configurable per point |
| overdue parcel | 逾期包裹 | Held longer than the retention period |
| recipient | 收件人 | The person a parcel is addressed to and collects it. The contract names the fields `recipientName` and `recipientPhone`, so the screens write "recipient name" and "recipient phone" and never "resident name" or "resident phone". `Resident` stays the name of the login role and of the account |

The other glossary terms these screens need — carrier, delegated collection
code, exception parcel, receiver sheet — are used exactly as `glossary.md`
defines them and are not repeated here. The retired words of `glossary.md`
section 7 (櫃機, 格口, 開箱 and the older product names) appear on no screen, and
neither does a courier account, which the reference prototype README retires.

One word per field: `recipient` is the person, `resident` is the login role, and
the contract, the ERD and the screens use `recipientName` and `recipientPhone`
for the two fields on the intake form and the pickup fallback. R9 records the
`recipient` row above in `glossary.md`, so the prototype, the ERD and the
contract agree on one word.

## 4. Application shell

Shared by every authenticated page, implemented as `AppLayout.vue`.

```
┌──────────────────┬──────────────────────────────────────────────────────┐
│ Collection Point │  top bar: user name │ role chip │ Sign out           │
├──────────────────┼──────────────────────────────────────────────────────┤
│ > Dashboard      │  PageHeader: title, description, primary actions     │
│ > My parcels     ├──────────────────────────────────────────────────────┤
│ > Parcels        │                                                      │
│ > Locations      │  page content                                        │
│ > Pickup         │                                                      │
│ > Exceptions     │                                                      │
│ > Reports        │                                                      │
│ > Users          │                                                      │
│ > Settings       │                                                      │
│ > Audit          │                                                      │
└──────────────────┴──────────────────────────────────────────────────────┘
   220 px sidebar
```

1. One menu list, filtered by role, lives in `AppLayout.vue`. A page never adds
   its own navigation.
2. Every page starts with `PageHeader`: title, one line description, actions on
   the right.
3. The menu shows only pages the role may open, following the access column of
   the page map; the router still enforces the rule with `meta.roles`.
4. At 640 px and below the sidebar becomes a single row above the content, as
   already noted in `frontend/src/style.css`. Below 641 px the resident pages are
   the design target.

## 5. Page prototypes

Each page below lists its route, access, owner, the in-scope function it serves,
a wireframe, the fields where a form is involved, the states it must handle, and
the number of steps the main task takes.

### 5.1 Resident parcel list — `/my-parcels` — resident — R5 — IS-6 (FR: pending R3)

This is the landing page for a resident, so it is drawn as a phone card list and
not as a wide table. Survey finding: 9 of the 11 resident respondents use a
phone, and mobile support is specified as usable from 375 px.

```
┌────────────────────────────────────────┐
│ My parcels           [ All statuses v] │
│ Pickup point  Community Parcel Point   │
│ Hours         Mon–Sat 09:00–19:00      │
├────────────────────────────────────────┤
│ ┌────────────────────────────────────┐ │
│ │ SF1234567890        [ Shelved    ] │ │
│ │ Storage  A-03-2                    │ │
│ │ Pickup code  4821                  │ │
│ │ Held 1 day                         │ │
│ │ Return to sender  25 Oct 2026      │ │
│ │                        [ Open  ]   │ │
│ └────────────────────────────────────┘ │
│ ┌────────────────────────────────────┐ │
│ │ YT9876543210        [ Shelved    ] │ │
│ │ Storage  B-01-4                    │ │
│ │ Pickup code  7154                  │ │
│ │ Held 2 days                        │ │
│ │ Return to sender  24 Oct 2026      │ │
│ │                        [ Open  ]   │ │
│ └────────────────────────────────────┘ │
│ ┌────────────────────────────────────┐ │
│ │ JD5566778899        [ Shelved    ] │ │
│ │ ⚠ Overdue 4 days                   │ │
│ │ Storage  A-01-3                    │ │
│ │ Pickup code  6382                  │ │
│ │ Held 7 days                        │ │
│ │ Return to sender  19 Oct 2026      │ │
│ │                        [ Open  ]   │ │
│ └────────────────────────────────────┘ │
└────────────────────────────────────────┘
```

Every `shelved` card carries a code, as the element table below requires; a parcel
that is still `registered` shows "shown when ready" instead, as in 5.2.

| Element | Detail |
|---|---|
| One card per parcel | waybill, status pill, storage location, pickup code, days held, return-to-sender date, open action |
| Pickup code | shown only when the parcel is `shelved`; otherwise the line reads "shown when ready" |
| Filter | one status filter, default all |
| Sort | newest first |
| Collection point | displayed once above the list; mock value: Community Parcel Point |
| Opening hours | displayed below the collection point; mock value: Mon–Sat, 09:00–19:00 |
| Return-to-sender date | shown on every shelved parcel card |
| Overdue treatment | danger-toned overdue marker with a warning icon, a 2 px danger border and the text Overdue N days |
| Open action | available on every parcel card |

States:

- empty: "No parcels are waiting for you", with one line explaining that the
  resident is told when a parcel arrives (survey: notification is the highest
  priority resident function)
- loading: three skeleton cards
- error: `ErrorState` with the API message and a retry action
- overdue: the card uses a danger-coloured overdue marker with a warning icon, a 2 px danger-coloured card border and the text "Overdue N days"; the days held remain visible, so the resident does not need to open the parcel to notice

Steps to the pickup code: 2 (open the list, open the parcel). Target duration for
the whole collection task, including the counter handover: 3 to 5 minutes.

### 5.2 Resident parcel detail — `/my-parcels/:id` — resident — R5 — IS-6 (FR: pending R3)

```
┌────────────────────────────────────────┐
│ < Back to my parcels                   │
│ SF1234567890            [ Shelved    ] │
├────────────────────────────────────────┤
│ PICKUP CODE                            │
│ 4 8 2 1                                │
│ Show this at the counter. Valid for    │
│ 1 more day.                            │
├────────────────────────────────────────┤
│ Where to collect                       │
│ Front desk, Block A · Zone A · A-03-2  │
│ Opening hours 09:00 - 21:00            │
├────────────────────────────────────────┤
│ Progress                               │
│ Registered ✓  Shelved ✓  Collected ○   │
├────────────────────────────────────────┤
│ Facts                                  │
│ Carrier SF Express · Size M            │
│ Shelved 12 Oct 18:40 · Fee none        │
├────────────────────────────────────────┤
│ Lost your code? Bring your phone       │
│ number and surname to the counter.     │
└────────────────────────────────────────┘
```

The progress labels are the screen labels of section 6, so the words on this
screen and the state machine use the same vocabulary.

The "lost your code" line is the identity fallback: phone number and surname,
recorded as an alternative verification method. It has no operation behind it in
the contract today (CD-2), so the line stays as a design intent and the manual
procedure at the counter until R6 and R2 decide.

States:

- loading: skeleton blocks in the same order
- error: `ErrorState`, with back navigation that still works
- by parcel state: `registered` hides the code and explains when it appears;
  `shelved` shows the code and the validity; `collected` shows a completed
  progress row and the handover time; an open exception ticket replaces the code
  block with a message that the site will contact the resident
- overdue: the card uses a danger-toned overdue marker with a warning icon,
  a 2 px danger border and the text "Overdue N days"; the days held remain
  visible, so the resident does not need to open the parcel to notice

Steps to read the code: 1 (the code is above the fold). The fallback path uses
the survey decision "phone number plus surname, recorded as an alternative
verification method" and depends on the contract decision CD-2.

### 5.3 Dashboard — `/dashboard` — staff, owner, admin — R4 + R5 — IS-2, IS-6, IS-7, IS-10, IS-11, IS-12 (FR: pending R3)

One route, three variants, chosen by the signed-in role.

Staff variant (the landing page of the workday):

```
┌───────────┬───────────┬───────────┬───────────┐
│ Received  │ Waiting   │ Handovers │ Open      │
│ today  12 │ 34        │ today   9 │ exceptions│
├───────────┴───────────┴───────────┴───────────┤
│ Location board summary        │ Alerts        │
│ Zone A  ▓▓▓░  Zone B ▓▓░░     │ · shelf full  │
│ Zone C  ▓░░░  Zone D ▓▓▓▓     │ · due today   │
├───────────────────────────────┴───────────────┤
│ Recent parcels (waybill, status, location)    │
└───────────────────────────────────────────────┘
```

The first card of the staff variant carries the batch intake shortcut, because
batch registration is the largest recorded source of delay at the counter
(survey staff finding S3, chosen by 4 of 7).

Owner variant: the first card is the overdue parcel count, because the most
important report for an owner is the overdue list (survey: chosen by 6 of the 7
owner respondents). Then received and collected volume for the period, then the
same alert block.

Admin variant: open exceptions, accounts and collection points, plus the audit
shortcut. No parcel volume.

States: loading (skeletons in each block); error (the failing block shows
`ErrorState` while the other blocks stay usable, so one slow endpoint does not
empty the page); empty (a new site with no data yet shows `EmptyState` with the
first useful action, for example "register the first parcel"); permission (a role
that is not allowed is redirected to 403 by the router, never shown a blank
dashboard).

Steps for the owner's main question "are any parcels overdue": 1 (the count is on
the landing page; the detail is one click away).

### 5.4 Intake and batch intake — `/parcels/new` — staff — R5 — IS-2, IS-3 (FR: pending R3)

This is the highest priority staff function in week 2, and it is the page that
decides how long a courier drop takes. It has two modes on one route. Both are
Sprint 2 must-haves: CR-003 raises mode B (batch) to must-have and makes it the
first staff feature built, because batch registration is the largest recorded
source of delay at the counter (survey S3, chosen by 4 of 7).

Mode A, single parcel:

```
┌────────────────────────────────┬──────────────────────────────┐
│ Waybill *    [_______________] │ Suggested storage location   │
│ Carrier      (from the prefix) │ ┌──────────────────────────┐ │
│ Recipient phone * [__________] │ │ Zone A · A-03-2          │ │
│ Recipient name    [__________] │ │ size fits · zone priority│ │
│ Size *       [ S ] [ M ] [ L ] │ │ least loaded in zone     │ │
│                                │ │ suggestion only, not     │ │
│                                │ │ applied automatically    │ │
│ Note         [_______________] │ └──────────────────────────┘ │
│                                │ [ Accept ]  [ Choose ... ]   │
│ [ Register parcel ]            │                              │
└────────────────────────────────┴──────────────────────────────┘
```

The suggested location is a suggestion only. It is never applied automatically,
and the staff member may override it (CR-004): `[ Accept ]` takes the suggestion,
`[ Choose ... ]` opens the location picker. Survey S4 recorded 0 of 7 staff
asking for a location recommendation and S5 recorded 4 of 7 wanting manual
control or no suggestion at all, so the rule is written on the screen and not
only in the contract.

Mode B, batch (one courier drop, from the receiver sheet):

```
┌───────────────────────────────────────────────────────────────┐
│ Batch intake            [ single ] [ batch ]                  │
├───────────────────────────────────────────────────────────────┤
│ Scan or paste waybills, one per line                          │
│ ┌───────────────────────────────────────────────────────────┐ │
│ │ SF1234567890                                              │ │
│ │ YT9876543210                                              │ │
│ │ SF1234567999                                              │ │
│ └───────────────────────────────────────────────────────────┘ │
│ Recipient phone for this drop [_______________]  (optional)   │
│ At most 50 waybills per request; a longer list is refused     │
│ with 400. Split the drop before you run the check.            │
│ [ Check 3 parcels ]                                           │
├───────────────────────────────────────────────────────────────┤
│ Waybill        Size  Suggested location   Issue               │
│ SF1234567890   M     Zone A · A-03-2      ok                  │
│ YT9876543210   S     Zone B · B-01-4      ok                  │
│ SF1234567999   L     none                 no free large slot  │
├───────────────────────────────────────────────────────────────┤
│ [ Register 2 parcels ]   [ Review the 1 problem ]             │
└───────────────────────────────────────────────────────────────┘
```

The `Suggested location` column carries the same rule as mode A (CR-004): a
suggestion only, never applied automatically, and every row may be overridden
before the batch is registered.

If a parcel arrives damaged or mis-addressed, the same page records the exception
and its type, so the counter does not have to leave the intake flow:

```
┌───────────────────────────────────────────────────────────────┐
│ Exception at intake                                           │
├───────────────────────────────────────────────────────────────┤
│ Type  [ damaged v ]   overdue · damaged · wrong-recipient ·   │
│                       refused · lost                          │
│ Note  [___________________________________________]           │
│ [ Record exception ]    [ Continue registration ]             │
└───────────────────────────────────────────────────────────────┘
```

The five values of the selector are the exception types fixed by SRS section 2.2,
and the same selector is reused wherever an exception is raised, so a type is
picked the same way on every screen.

| Field | Control | Validation | Note |
|---|---|---|---|
| waybill | text | required, unique | the carrier is derived from the prefix and may be corrected |
| recipient phone | text | required in single mode, optional in batch mode | looked up against the resident account for this point |
| recipient name | text | required when no account matched | the field names follow the contract: `recipientName`, `recipientPhone` |
| size | segmented control | required | drives the storage location filter |
| note | textarea | optional | |
| waybills (batch) | textarea, one per line | between 1 and 50 per request | the API refuses more than 50 entries in one request with 400 |
| exception type | select, one of five | required only when an exception is recorded | `overdue`, `damaged`, `wrong-recipient`, `refused`, `lost`; the type is a separate field from the ticket state |

States:

- loading: while the suggested location is being computed, the suggestion panel
  shows a placeholder and the register button stays disabled
- error, duplicate waybill: a field level error on that waybill, never a generic
  page error
- full shelf: no free storage location in the required size class; the panel
  offers the next best zone and a "shelve later" option that keeps the parcel
  `registered` with a visible flag, instead of blocking the counter. "Shelve
  later" only works once a parcel can be given a location after creation, which
  the contract does not offer today (CD-1)
- batch too large: a drop of more than 50 waybills in one request is refused with
  400, so the cap is stated next to the check action and staff split the drop
  before they run it
- suggestion only: neither the suggestion panel nor the batch column applies a
  location by itself; the CR-004 rule is written next to the suggestion, and the
  staff member keeps the override at all times
- exception at intake: a damaged or mis-addressed parcel is recorded with its
  type before registration continues. The type is chosen once from the five SRS
  2.2 values; the ticket then moves through the lifecycle states (`open`,
  `processing`, `resolved`) on `/exceptions`, which Sprint 2 details. Recording
  the ticket at the counter is part of intake, working it is not
- partial batch failure: the successful rows are registered and the failures stay
  in the table with their reason, so staff never re-scan the whole drop
- empty batch: the check action is disabled until at least one waybill is present

Steps: single parcel, 2 fields plus one accept click after the waybill is typed;
batch, one check action per drop and at most one decision per problem row. Target:
the whole ten parcel drop is recorded in under three minutes.

### 5.5 Storage location board — `/locations` — staff, owner, admin — R4 — IS-3, IS-4 (FR: pending R3)

```
┌───────────────────────────────────────────────────────────────┐
│ Storage locations      zone [ All v ]   [ + Add location ]    │
├───────────────────────────────────────────────────────────────┤
│ Zone A  (full-shelf warning: 0 free)                          │
│  A-01-1 occupied  A-01-2 occupied  A-01-3 occupied            │
│  A-02-1 occupied  A-02-2 reserved  A-02-3 occupied            │
│ Zone B                                                        │
│  B-01-1 available B-01-2 occupied  B-01-3 disabled            │
├───────────────────────────────────────────────────────────────┤
│ selected: A-02-2   [ Assign parcel ] [ Disable ] [ Clear ]    │
└───────────────────────────────────────────────────────────────┘
```

Contract dependency (CD-1): `[ Assign parcel ]` gives a storage location to a
parcel that is already `registered` without one. `POST /parcels` sets `locationId`
at creation, `PATCH /parcels/{id}/status` carries only `status`, `expectedVersion`
and `reason`, and `/locations` has only GET and `/recommend`, so there is no
operation behind this button today. It stays drawn as the intended behaviour and
is not built until R6 and R2 record the decision. The same holds for the other
actions that write to a location — `[ + Add location ]` in the toolbar,
`[ Disable ]` and `[ Clear ]` in the selection bar, and the add action of the
empty state — because `/locations` is read only as it stands. All of them are
CD-1 until the contract says otherwise.

States: empty (no locations configured, with the add action); full shelf (a zone
where every location is occupied or reserved carries a warning banner, because
the intake page depends on it); loading (grid skeleton); error (`ErrorState` with
retry); a disabled location is visually distinct and cannot be assigned.

Steps to answer "is there room for a large parcel": 2 (open the board, read the
zone row).

### 5.6 Pickup verification — `/pickup` — staff — R4 — IS-7 (FR: pending R3)

This is the counter screen and the fastest path in the system: the staff member
must finish a handover in three minutes or less.

```
┌───────────────────────────────────────────────────────────────┐
│ Verify a pickup code                                          │
│  ┌─────────────────────────────┐                              │
│  │ 4 8 2 1                     │                              │
│  └─────────────────────────────┘                              │
│  [ Verify ]                                                   │
└───────────────────────────────────────────────────────────────┘
```

No person is named on any screen. The result view shows the role and the facts,
not a person, because the ethics statement promises that nobody who took part in
the survey is identified (CR-009). The Result region appears only after a
successful verification; the idle screen does not draw an empty result block.

States:

- idle: empty input, verify disabled
- verifying: the button shows progress and the field is locked
- success: parcel summary plus `Confirm handover`, which opens
  `ConfirmDialog` because the action is irreversible
- invalid code: "This code is not recognised", with the attempt count kept low
  and no hint about which digits are wrong
- expired or already used: the reason is stated and the next action is offered
  (issue a new code); the identity fallback is not available in Sprint 2
- parcel not on shelf: the code is valid but the parcel is absent from the
  recorded shelf. Keep the shelf location prominent, disable handover
  confirmation, and tell staff to re-check that location. The final API
  representation of this state remains an R6/R2 contract decision.
- API error: show the normalised message and a retry action without clearing the
  entered code

Enter after a scanner or keyboard entry starts verification directly; the
separate `Verify` button remains available for a mouse or touch interaction.

The delegated-code state is future work under `FR-9` and `FR-10`, so it is not
drawn or treated as available in Sprint 2. The identity fallback remains
unavailable until `#98/CD-2` settles the contract.

Steps: 3 for the happy path (scan or type the code, verify, confirm). Target:
three minutes or less per handover, and no more than two screens.

### 5.7 Users — `/users` — owner, admin — R5 — IS-11 (FR: pending R3)

```
┌───────────────────────────────────────────────────────────────┐
│ Users            [ + Create staff ]  [ + Create resident ]    │
├───────────────────────────────────────────────────────────────┤
│ role [ v ]  status [ v ]  keyword [ name / phone ]            │
├───────────────────────────────────────────────────────────────┤
│ Name │ Role │ Phone │ Status │ Last sign in │ actions         │
│      actions: edit · reset password · disable                 │
└───────────────────────────────────────────────────────────────┘
```

States: empty (a new site with no staff account besides the creator);
loading; error; create dialog validation (phone format, role choice, duplicate
account); disable requires `ConfirmDialog` and states that the account keeps its
history and its audit trail.

Owner and Admin differ: an Owner manages the accounts of their own collection
points, an Admin manages every account. The screen states which scope is shown,
otherwise the Owner wonders why other sites are missing.

Steps to disable a staff account: 3 (find, open the row action, confirm).

### 5.8 Collection point settings — `/settings` — admin — R5 — IS-12 (IS-11) (FR: pending R3)

```
┌───────────────────────────────────────────────────────────────┐
│ Settings                                                      │
│ Tabs: Collection points · Retention · Notifications · System  │
├───────────────────────────────────────────────────────────────┤
│ Collection points                                             │
│ name, address, opening hours, contact, storage location count │
├───────────────────────────────────────────────────────────────┤
│ banner: values marked read only are filled in by <task id>    │
└───────────────────────────────────────────────────────────────┘
```

| Tab | Content | Rule owner |
|---|---|---|
| Collection points | name, address, opening hours, contact | IS-11 |
| Retention | retention period, default three days, per point | survey decision |
| Notifications | which events notify a resident | T-43-01, R7 |
| System | pickup code length and expiry, overdue display | SRS OI-3 |

The survey showed that administrators do not want fee configuration (0 of 4 chose
it), so this page does not configure fees; it only controls how the days held are
displayed. Billing belongs to T-44-01 and is out of the must-have scope.

States: read only until the owning backend task is merged, with a banner naming
that task; validation errors per field; save confirmation; error with retry.

Steps to change the retention period: 3 (open the tab, edit, save).

### 5.9 Audit log — `/audit` — owner, admin — R5 — SRS 2.3, IS pending R3 (FR: pending R3)

SRS section 2.3 gives the operation log to the Owner for their own collection
points and to the System Administrator for every point, which is R4's use case
`UC_AdminAudit`. The page is read only: the rows are written by the actions on
the other pages, and nothing on this screen edits or deletes them.

```
┌───────────────────────────────────────────────────────────────┐
│ Audit log               scope: your collection points         │
├───────────────────────────────────────────────────────────────┤
│ from [ 12 Oct ] to [ today ]   actor [ any v ]                │
│ site [ All v ]   action [ all v ]   keyword [ waybill ]       │
├───────────────────────────────────────────────────────────────┤
│ 19:14  staff1  handover    SF1234567890  Front desk · Block A │
│ 18:40  staff2  shelving    SF1234567890  Zone A · A-03-2      │
│ 18:02  admin   sign in     —             all points           │
└───────────────────────────────────────────────────────────────┘
```

| Filter | Control | Note |
|---|---|---|
| date range | two date inputs, today by default | a "last 7 days" preset sits next to them |
| actor | select | only the accounts inside the scope shown |
| site | select | admin only; the owner's scope is fixed |
| action | select | registration, shelving, handover, exception, account change, settings change, sign in |
| keyword | text | waybill number or account |

The two roles differ in scope, and the page says which scope is on screen,
otherwise the Owner wonders why rows from other sites are missing:

| Role | Rows shown | Banner on the page |
|---|---|---|
| Owner | the collection points they own, and the accounts of those points | "scope: your collection points" |
| Admin | every point, plus the account and settings changes that belong to no single point | "scope: all collection points" |

States: empty (no entries in the range yet, with the range shown back to the
user); loading (table skeleton); error (`ErrorState` with retry); permission
(staff is redirected to 403 by the router, never shown a blank page); read only
(no row action and no bulk action anywhere on the page).

Steps to answer "who handed this parcel over": 2 (open the log, type the waybill
into the keyword filter).

## 6. Status and type labels used on the screens

States come from `docs/02-design/backend-state-machines.md`, so the prototype, the
database and the API use the same words. Labels are English, matching the rest of
the repository.

| Kind | State | Label on screen | Tone |
|---|---|---|---|
| Parcel | registered | Registered | info |
| Parcel | shelved | Shelved | success |
| Parcel | collected | Collected | neutral |
| Storage location | available | Available | success |
| Storage location | reserved | Reserved | warning |
| Storage location | occupied | Occupied | info |
| Storage location | disabled | Disabled | neutral |
| Pickup code | active | Active | success |
| Pickup code | used | Used | neutral |
| Pickup code | expired | Expired | warning |
| Pickup code | revoked | Revoked | danger |
| Exception ticket | open | Open | danger |
| Exception ticket | processing | Processing | warning |
| Exception ticket | resolved | Resolved | success |

### 6.1 Exception types

The exception list is closed: SRS section 2.2 fixes five types, and the screens,
the database and the API use these five words only.

| Type | What it means | Where it is usually raised |
|---|---|---|
| overdue | held longer than the retention period | at the site when the retention period runs out, not at intake |
| damaged | packaging or contents damaged | at intake, or when the resident collects |
| wrong-recipient | the parcel belongs to another resident or another point | at intake, or when the resident collects |
| refused | the recipient declined the parcel | at handover |
| lost | the parcel cannot be found at the site | during a search for a parcel |

A type is not a state. The type answers "which problem is this" and takes one of
the five values above; the state answers "where is this ticket in its life" and
takes `open`, `processing` or `resolved`. Every ticket carries one type and one
state at the same time, the two are separate fields, and no screen shows one in
the place of the other. The intake page picks the type through the shared
selector of 5.4, and Sprint 2 tests at least one case per type.

An exception is not a parcel state (CR-006): an open ticket describes the problem
while the parcel keeps the status it had, so no screen shows "Exception" as a
parcel status tag. The pickup code and storage location states are unchanged.

`overdue` also names a derived marker: a parcel is overdue when it has been held
longer than the retention period. That marker is not a state and not a ticket by
itself; the site raises an `overdue` exception ticket when the parcel needs
handling. The marker stays where it is, on the resident card and in the overdue
block of the dashboard, never as a status tag.

## 7. Step counts and time budgets

The non-functional targets are checked against the prototype here, before any
code exists. "Steps" counts user actions, not clicks on every control.

| Task | Page | Steps | Target |
|---|---|---|---|
| Resident finds the pickup code | 5.1 then 5.2 | 2 | 3 to 5 minutes for the whole collection task |
| Resident reads the code in the detail screen | 5.2 | 1 | code above the fold |
| Staff records one parcel at the counter | 5.4 | 3 | under 1 minute per parcel |
| Staff records a courier drop of ten parcels | 5.4 | 1 check plus one decision per problem | under 3 minutes per drop |
| Staff hands one parcel over | 5.6 | 3 | 3 minutes or less |
| Owner checks whether anything is overdue | 5.3 | 1 | immediate on the landing page |
| Admin disables a staff account | 5.7 | 3 | under 1 minute |
| Owner or admin traces who handled a parcel | 5.9 | 2 | filter and read, no export step |

## 8. Responsive rules

| Width | Layout |
|---|---|
| 375 px (primary target) | one column; resident pages are card lists; forms are single column; no horizontal scrolling anywhere; touch targets at least 44 px |
| 641 px to 1023 px | sidebar stays; tables drop to their priority columns; secondary facts move into the row detail |
| 1024 px and above | desktop layout with full tables and two column forms; desktop only has to remain usable |

The mobile target comes from the survey: 9 of 11 resident respondents use a phone,
and the agreed requirement is "usable from 375 px width, primary design target is a
phone screen". Staff and admin screens are desktop first but must not break at
375 px, because a staff member may check a parcel on a phone.

## 9. Traceability

| Page | In-scope function (SRS 1.5.2) | FR identifier | Sprint task (PLAN.md) |
|---|---|---|---|
| 5.1 my parcel list | IS-6, with IS-5 in the empty state | pending R3 | none named; the integration pass is T-42-09 |
| 5.2 parcel detail | IS-6 | pending R3 | none named; the integration pass is T-42-09 |
| 5.3 dashboard | IS-2, IS-6, IS-7 (staff), IS-10 (owner), IS-11, IS-12 (admin) | pending R3 | T-44-03 |
| 5.4 intake and batch intake | IS-2, IS-3; batch mode is a must-have (CR-003); exception type selector (SRS 2.2) | pending R3 | T-42-07 |
| 5.5 storage location board | IS-3, IS-4 | pending R3 | T-42-06; high fidelity version in T-20-12 |
| 5.6 pickup verification | IS-7 | pending R3 | T-42-05 (page, R4), T-42-04 (backend) |
| 5.7 users | IS-11 | pending R3 | T-43-05 |
| 5.8 collection point settings | IS-12, with IS-11 | pending R3 | T-43-05 |
| 5.9 audit log | SRS 2.3 (operation log, owner and administrator scope); IS to bind with R3 | pending R3 | none named yet |

## 10. Open points

1. Functional requirement identifiers. SRS chapter 3 was not published when this
   document was written (issue #76, due 2026-10-17), so the mapping uses the
   in-scope functions of section 1.5.2 and the FR column says "pending R3". This
   is filled in before the review. The audit page of 5.9 is the one exception in
   kind: it comes from SRS section 2.3 rather than from the 1.5.2 list, so its IS
   identifier is bound at the same time.
2. Pickup code visibility. This prototype hides the code until the parcel is
   `shelved` and shows the identity fallback instead. Confirm with R6 and R2,
   because it changes the resident detail screen and the notification wording
   in IS-5. The fallback itself is CD-2: `POST /pickup/verify` accepts only
   `pickupCode` today.
3. The resident pages have no named Sprint 2 task in `PLAN.md`; T-42-09 only wires
   existing pages to the API. Raise with R1 and R4 while sprint 2 is still being
   planned, otherwise the two resident pages fall between the tasks.
4. Batch intake scope (settled). CR-003 raises 5.4 mode B to must-have and makes
   it the first staff feature built in Sprint 2, on the survey S3 finding that
   batch registration is the largest recorded delay (4 of 7). The question is
   closed and T-42-07 carries it; nothing is left to confirm.
5. Owner as staff (SRS OI-2). It changes the Owner menu and 5.3. This prototype
   assumes an owner does not work the counter.
6. Retention period default. Three days is an assumption from the survey, not a
   measured value; it is displayed as a configurable default in 5.8. No fee amount
   is shown anywhere until T-44-01 is merged; the screens show the days held
   instead.
7. Assigning a storage location to an existing parcel (CD-1). The `[ Assign
   parcel ]` action in 5.5 and the "shelve later" option in 5.4 need an operation
   the contract does not have: creation sets `locationId`, the status patch
   carries only `status`, `expectedVersion` and `reason`, and `/locations` has no
   write operation. Raised with R6 and R2. Both features stay marked as design
   intent until the decision is recorded, so nothing on the page is left looking
   buildable when it is not.
8. Identity fallback verification (CD-2). `POST /pickup/verify` accepts only
   `pickupCode`, so the phone number plus surname fallback of 5.6 and 5.2 needs
   either a new endpoint or an added field. Raised with R6 and R2. Until then the
   fallback stays a counter procedure outside the application.
9. Recipient terminology. The screens and the field names now read `recipient`
   (`recipientName`, `recipientPhone`) to match the contract and the ERD, and
   `resident` stays the login role. R9 records the word in `glossary.md` and
   retires "resident name" and "resident phone" as field wording, so one word
   wins in the prototype, the glossary and the contract.
10. Residual contract check. This revision matched against the contract every
    action R4 named. Three groups of write actions elsewhere in the document are
    checked the same way before the Sprint 2 planning closes, so the same kind of
    gap cannot appear a second time: the account actions of 5.7 (create, edit,
    reset password, disable), raising an exception ticket from 5.4, issuing a
    replacement code in 5.6, and the write tabs of 5.8, which already say they
    stay read only until their owning task lands. None of them is known to be
    missing; they are unverified, which is not the same thing.

## 11. Self-check against the week 2 acceptance list

| Acceptance item for T-20-05 | Where it is satisfied |
|---|---|
| Every role has usable prototype pages | section 2: Resident, Staff, Owner and Admin each have at least one page, and section 5 draws each of them |
| Empty, loading, error and abnormal states are covered | the states list under each page in section 5, including the full shelf state in 5.4 and 5.5 and the batch cap in 5.4 |
| Every page points back to a requirement | section 9, with `pending R3` where the FR identifier does not exist yet |
| Mobile first at 375 px | section 8, plus the card layouts chosen in 5.1 and 5.2 |
| Terminology matches the glossary | section 3, with `recipient` settled for the two field names |
| Step counts support the NFR targets | section 7: resident 3 to 5 minutes, staff handover 3 minutes or less |
| Features with no operation in the contract are marked, not silently drawn | section 1.2, with CD-1 on 5.4 and 5.5 and CD-2 on 5.2 and 5.6 |
| Exception type is separate from exception state | section 6.1: the five types of SRS 2.2, with the shared type selector in 5.4 |
| The operation log has a page, with both role scopes | section 5.9, following SRS 2.3 and `UC_AdminAudit` |
| No person is named on any screen | 5.6 result row and the CR-009 note under it |
| A location suggestion is advice, never automatic | 5.4, both modes, with the CR-004 wording |

Issue #83 also carries acceptance criteria for T-20-09 (the conventions
document) and T-20-10 (the ten-heuristic usability review by R9). Those two are
not part of this document and are not claimed here, so the pull request does not
close the issue on its own.



## Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v1.0 | Week 2 | First version recorded in this file. The nine page prototypes. This is revision 4 of the document: the earlier three are described in the header's own Revision note, because the file had no revision history then. Seven contradictions against this document's own text are recorded in `#170` and are not yet fixed. Any earlier edit is not listed: this file had no revision history until week 3, so records of those edits do not exist here. | R1 |
| v1.1 | Week 3 | Corrected R4 findings 2, 3, 4, 6 and 7 in the pickup prototype: removed the staff identifier, unavailable identity action, future delegated state, developer note and empty idle result. Added the parcel-not-on-shelf state and Enter-to-verify behaviour for findings 9 and 10 | R4 |
| v1.2 | Week 3 | Completed R5 findings 1, 5 and 8 in the resident parcel list: completed the third card, defined the overdue treatment, and added collection-point, opening-hours and return-to-sender information | R5 |
