# ADR-003: Frontend Architecture

- Status: Proposed
- Date: 2026-10-11
- Task: T-30-04
- Owners: R4 Frontend Lead (implementation lead), R5 Frontend Developer
- Scope: Frontend architecture for the community parcel collection-point Web system

## Context

The repository now uses a Java 17 Spring Boot backend and a PostgreSQL database. The frontend workstream has two students and must support a small but visible management system with multiple roles, tables, forms and status-heavy workflows.

The project plan defines `T-30-04` as the frontend skeleton: Vite, router, Pinia and an HTTP wrapper. The README also lists Vue 3, Vite, Pinia and Element Plus as the frontend stack. The architecture therefore needs to be simple enough for two developers but structured enough to avoid duplicated state and UI code.

## Decision

Use the following frontend stack:

- Vue 3 with the Composition API
- Vite for development and production builds
- Vue Router for page navigation and route guards
- Pinia for shared authentication/session, parcel and location state
- Element Plus as the approved component library for tables, forms, dialogs and date controls
- Plain JavaScript, not TypeScript
- Axios with one shared wrapper for API calls
- Reusable Vue components and the existing visual reference

Element Plus should be introduced page by page where it reduces custom code. A page does not need to use Element Plus when ordinary HTML and project CSS are simpler.

TypeScript is deferred. It should only be added through a new ADR if a concrete need appears.

## Responsibilities

- R4 owns the frontend architecture, router, Pinia store structure, API wrapper, shared components and code review.
- R5 implements resident, staff and admin pages inside those standards.
- R4 and R5 must reuse shared components instead of creating separate copies.
- API field names and response shapes must follow the OpenAPI contract owned by R6.

## Alternatives Considered

### Vue without Pinia

Rejected. Authentication state, current user, parcel lists and location selections are shared across routes. Pinia gives both developers one predictable place for that state.

### Native fetch instead of Axios

Rejected for the final structure. Axios provides one consistent interceptor location for authentication, error normalisation and request IDs. The wrapper prevents pages from calling Axios directly.

### TypeScript

Deferred. The two-person frontend team has limited time and needs to prioritise working features, tests and integration over a new language workflow.

### Plain HTML, CSS and JavaScript served by Spring Boot

Rejected as the main frontend architecture. It makes reusable components, route guards, state management and multi-person maintenance harder.

## Consequences

- R4 and R5 can develop pages in parallel with a shared store and component model.
- Element Plus reduces work on data tables and forms.
- Pinia adds one learning topic, but avoids custom global-state code.
- Axios adds one dependency, but provides one consistent API boundary.
- Some reference HTML must be converted into Vue components rather than copied directly.

## Delivery Rules

- One application layout and one router configuration.
- One Axios instance in `frontend/src/api/http.js`.
- Feature API modules live under `frontend/src/api/`.
- Shared Pinia stores live under `frontend/src/stores/`.
- Reusable components live under `frontend/src/components/`.
- Pages live under `frontend/src/views/`.
- Mock data is used until the corresponding backend endpoint is approved.
- Every new dependency must be discussed before it is added.
