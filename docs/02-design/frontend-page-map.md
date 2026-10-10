# Frontend Page Map and Routing

- Status: Proposed
- Task: T-30-04
- Owners: R4 Frontend Lead, R5 Frontend Developer
- Assumption: the frontend workstream has two students; R4 owns architecture and R5 implements pages under that structure.

## Application Shell

- Public layout: login.
- Main layout: sidebar, top bar, user menu, notifications and breadcrumb.
- Route guards: redirect unauthenticated users to `/login` and unauthorised users to a 403 page.
- Error pages: 403 and 404.
- Role names follow the SRS: Resident, Collection Point Staff, Collection Point Owner, System Administrator.

## Page List

| Priority | Route | View | Access | Main purpose | Primary owner |
|---|---|---|---|---|---|
| P0 | `/login` | `LoginView.vue` | Public | User login | R5 |
| P0 | `/dashboard` | `DashboardView.vue` | Staff, Owner, Admin | Daily parcel and location overview | R4 + R5 |
| P0 | `/my-parcels` | `MyParcelListView.vue` | Resident | View own parcels, status and pickup code | R5 |
| P0 | `/my-parcels/:id` | `MyParcelDetailView.vue` | Resident | View one parcel and its storage location | R5 |
| P0 | `/parcels` | `ParcelListView.vue` | Staff, Owner, Admin | Search, filter and review parcels | R5 |
| P0 | `/parcels/new` | `ParcelCreateView.vue` | Staff | Register an incoming parcel | R5 |
| P0 | `/parcels/:id` | `ParcelDetailView.vue` | Staff, Owner, Admin | View parcel status and handling history | R5 |
| P0 | `/locations` | `LocationBoardView.vue` | Staff, Owner, Admin | View and manage storage locations | R4 |
| P0 | `/pickup` | `PickupVerifyView.vue` | Staff | Verify a pickup code and confirm handover | R4 |
| P1 | `/exceptions` | `ExceptionListView.vue` | Staff, Owner, Admin | Register and process abnormal parcels | R5 |
| P1 | `/reports` | `ReportView.vue` | Owner, Admin | View parcel-volume and overdue reports | R4 |
| P1 | `/users` | `UserListView.vue` | Owner, Admin | Manage staff and resident accounts | R5 |
| P1 | `/settings` | `SettingsView.vue` | Admin | Manage collection points and basic configuration | R5 |
| P1 | `/audit` | `AuditLogView.vue` | Owner, Admin | Review sensitive operations and state changes | R5 |

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
