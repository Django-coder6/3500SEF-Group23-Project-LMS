# Frontend Page Map and Routing

- Status: Proposed
- Owners: R4 Frontend Lead, R5 Frontend Developer
- Assumption: the frontend workstream has two students; R4 owns architecture and R5 owns page implementation.

## Application Shell

- Public layout: login and password/account recovery pages.
- Main layout: shared sidebar, top bar, user menu, notifications and breadcrumb.
- Permission guard: redirect users who do not have the required role.
- Error pages: 403 and 404.

## Page List

| Priority | Route | View | Access | Main purpose | Primary owner |
|---|---|---|---|---|---|
| P0 | `/login` | `LoginView.vue` | Public | Staff and admin login | R5 |
| P0 | `/dashboard` | `DashboardView.vue` | Staff, Admin | Daily parcel, pickup and capacity overview | R4 + R5 |
| P0 | `/parcels` | `ParcelListView.vue` | Staff, Admin | Search, filter and review parcels | R5 |
| P0 | `/parcels/new` | `ParcelCreateView.vue` | Staff | Register an incoming parcel and assign a location | R5 |
| P0 | `/parcels/:id` | `ParcelDetailView.vue` | Staff, Admin | View parcel status, history and pickup details | R5 |
| P0 | `/locations` | `LocationBoardView.vue` | Staff, Admin | View and manage location occupancy | R4 |
| P0 | `/pickup` | `PickupVerifyView.vue` | Staff | Verify a pickup code and confirm handover | R4 |
| P1 | `/exceptions` | `ExceptionListView.vue` | Staff, Admin | Register and process abnormal parcels | R5 |
| P1 | `/residents` | `ResidentListView.vue` | Admin | View resident records and parcel history | R5 |
| P1 | `/couriers` | `CourierListView.vue` | Admin | View courier accounts and delivery activity | R5 |
| P1 | `/reports` | `ReportView.vue` | Admin | View daily, monthly and exception reports | R4 |
| P1 | `/settings` | `SettingsView.vue` | Admin | Manage rules, prefixes and notification settings | R5 |
| P1 | `/audit` | `AuditLogView.vue` | Admin | Review sensitive operations and state changes | R5 |
| P2 | `/profile` | `ProfileView.vue` | Authenticated | Maintain profile and password | R5 |

## Component Map

| Component | Responsibility | Shared by |
|---|---|---|
| `AppLayout.vue` | Sidebar, top bar and main content area | All authenticated pages |
| `PageHeader.vue` | Title, description and page actions | Most pages |
| `StatusTag.vue` | Consistent parcel and location status colours | Parcel, location, exception pages |
| `ParcelTable.vue` | Parcel columns, pagination and row actions | Parcel list and resident detail |
| `LocationGrid.vue` | Location status map and selection | Dashboard and location board |
| `MetricCard.vue` | Dashboard summary metric | Dashboard |
| `ConfirmDialog.vue` | Dangerous action confirmation | Pickup, deletion and maintenance |
| `EmptyState.vue` | No-data and no-result states | All list pages |
| `ErrorState.vue` | API and permission error presentation | All pages |
| `LoadingState.vue` | Consistent loading display | All pages |

## Route Rules

- `/dashboard` is the default page after login.
- `/pickup` and `/parcels/new` are staff-focused workflows.
- `/settings`, `/audit` and `/reports` require the Admin role.
- Unknown routes redirect to a 404 page.
- API failures must not leave the shell in a broken state.

## MVP Order

1. Login and main layout.
2. Dashboard and parcel list.
3. Parcel creation and location board.
4. Pickup verification.
5. Exceptions.
6. Reports, users, audit and settings.
