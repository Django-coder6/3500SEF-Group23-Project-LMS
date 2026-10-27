# Frontend Page Map and Routing

| Item | Detail |
|---|---|
| Status | Active (aligned with the router, issue #82) |
| Version | v1.2 |
| Revised | 2026-10-25 |
| Task | T-30-04 |
| Owners | R4 Frontend Lead, R5 Frontend Developer |
| Assumption | the frontend workstream has two students; R4 owns architecture and R5 implements pages under that structure. |

`frontend/src/router/index.js` registers exactly the routes in the Page List
below: the same path, the same view name and the same role list. A view that is
still a placeholder says so and names the role that will implement it. Change
one file, change the other.

## Application Shell

- Public layout: login.
- Main layout: sidebar, top bar, user menu, notifications and breadcrumb.
- Route guards: redirect unauthenticated users to `/login` and unauthorised users to a 403 page.
- Error pages: 403 and 404, deliberately outside the main layout so that they render even when the shell cannot.
- Role names follow the SRS: Resident, Staff, Owner, System Administrator.

## Page List

| Priority | Route | View | Access | Main purpose | Primary owner | Status |
|---|---|---|---|---|---|---|
| P0 | `/login` | `LoginView.vue` | Public | User login | R5 | implemented |
| P0 | `/dashboard` | `DashboardView.vue` | Staff, Owner, Admin | Daily parcel and location overview | R4 + R5 | placeholder |
| P0 | `/my-parcels` | `MyParcelListView.vue` | Resident | View own parcels, status and pickup code | R5 | placeholder |
| P0 | `/my-parcels/:id` | `MyParcelDetailView.vue` | Resident | View one parcel and its storage location | R5 | placeholder |
| P0 | `/parcels` | `ParcelListView.vue` | Staff, Owner, Admin | Search, filter and review parcels | R5 | placeholder |
| P0 | `/parcels/new` | `ParcelCreateView.vue` | Staff | Register an incoming parcel | R5 | implemented with mock API |
| P0 | `/parcels/:id` | `ParcelDetailView.vue` | Staff, Owner, Admin | View parcel status and handling history | R5 | placeholder |
| P0 | `/locations` | `LocationBoardView.vue` | Staff, Owner, Admin | View and manage storage locations | R4 | implemented with mock API |
| P0 | `/pickup` | `PickupVerifyView.vue` | Staff | Verify a pickup code and confirm handover | R4 | implemented with mock API |
| P1 | `/exceptions` | `ExceptionListView.vue` | Staff, Owner, Admin | Register and process abnormal parcels | R5 | placeholder |
| P1 | `/reports` | `ReportView.vue` | Owner, Admin | View parcel-volume and overdue reports | R4 | placeholder |
| P1 | `/users` | `UserListView.vue` | Owner, Admin | Manage staff and resident accounts | R5 | placeholder |
| P1 | `/settings` | `SettingsView.vue` | Admin | Manage collection points and basic configuration | R5 | placeholder |
| P1 | `/audit` | `AuditLogView.vue` | Owner, Admin | Review sensitive operations and state changes | R5 | placeholder |
| — | `/403` | `Error403View.vue` | Any signed-in user | Forbidden: the role is not permitted on that route | R4 | implemented |
| — | `/:pathMatch(.*)*` (any unknown path) | `NotFoundView.vue` | Public | 404 fallback for an unknown route | R4| implemented |

The Status column is one of `implemented` (the page works now), `placeholder` (a view
file exists that renders a labelled placeholder naming the role that implements it in
Sprint 2) or `sprint 2` (planned, no view file yet).

The Access column uses the display names from the SRS. The router uses the role
enum from the OpenAPI contract: `resident`, `staff`, `owner`, `admin`. "Admin"
means `admin`, the System Administrator role.

## Component Map

| Component | Responsibility | Shared by |
|---|---|---|
| `AppLayout.vue` | Sidebar, top bar and main content area | All authenticated pages |
| `PageHeader.vue` | Title, description and page actions | Most pages |
| `StatusTag.vue` | Consistent parcel and location status colours | Parcel, location, exception pages |
| `ParcelTable.vue` | Parcel columns, pagination and row actions | Parcel list and dashboard |
| `LocationGrid.vue` | Storage-location status map and selection | Dashboard and location board |
| `MetricCard.vue` | Dashboard summary metric | Dashboard and reports |
| `ConfirmDialog.vue` | Confirmation for handover, disable and exception actions | Pickup, location and exception pages |
| `EmptyState.vue` | No-data and no-result states | All list pages |
| `ErrorState.vue` | API and permission error presentation | All pages |
| `LoadingState.vue` | Consistent loading display | All pages |

## Route Rules

- `/login` is the only public route.
- `/my-parcels` is the default route for a Resident.
- `/dashboard` is the default route for Staff, Owner and Admin.
- `/pickup` and `/parcels/new` are Staff workflows.
- `/users`, `/reports`, `/audit` and `/settings` require Owner or Admin permissions as appropriate.
- Unknown routes redirect to a 404 page.
- API failures must not leave the application shell in a broken state.

## MVP Order

1. Login and application layout.
2. Dashboard, parcel list and resident parcel list.
3. Parcel creation, location board and pickup verification.
4. Exception handling.
5. Reports, users, settings and audit.


## Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v1.0 | Week 2 | First version recorded in this file. The route list, aligned with `frontend/src/router/index.js`. Any earlier edit is not listed: this file had no revision history until week 3, so records of those edits do not exist here. | R1 |
| v1.1 | Week 3 | Marked `/pickup` implemented with the mock-backed API boundary and recorded the page's verification and handover states | R4 |
| v1.2 | Week 3 | Marked `/parcels/new` and `/locations` implemented with their mock-backed API boundaries after PR #205 | R4 |
