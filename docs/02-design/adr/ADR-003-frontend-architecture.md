# ADR-003: Frontend Architecture

- Status: Proposed
- Date: 2026-10-10
- Owners: R4 Frontend Lead (implementation lead), R5 Frontend Developer
- Scope: Frontend architecture for the community parcel collection-point Web system

## Context

The repository uses a Vue 3 + Vite frontend and an Express + Sequelize backend. The frontend workstream has only two students and the team has limited experience with advanced frontend tooling. The project has a fixed course deadline and needs a small, understandable stack that supports parallel work without requiring a large amount of new framework knowledge.

## Decision

The frontend will use:

- Vue 3 with the Composition API
- Vite for development and production builds
- Vue Router for page navigation
- Plain JavaScript, not TypeScript
- Native `fetch` with one shared API wrapper
- Reusable Vue components and the existing reference UI styling
- Simple `ref` and `reactive` modules for shared state

Element Plus is optional. It may be introduced gradually for complex tables, forms, dialogs or date pickers when it saves time. It is not a required dependency for the first MVP.

Pinia and TypeScript are deferred. They should only be added if a concrete need appears and the team agrees through a new ADR or documented change.

## Alternatives Considered

### Vue 3 + Vite + Pinia + Element Plus + TypeScript

Rejected for the first release. Pinia and TypeScript add extra learning and setup costs for a two-person team with limited frontend experience. Element Plus remains optional because it can reduce table and form work.

### Plain HTML, CSS and JavaScript served by Express

Rejected as the main frontend architecture. It is simple for one page, but repeated layouts, routing, permissions, shared state and multi-person maintenance become harder. The existing HTML prototype may be used as a visual reference, but not as the final application structure.

### Nuxt or server-side rendering

Rejected. The system is an internal management application and does not need SEO, server-side rendering or the extra Nuxt learning curve.

## Consequences

- Two developers can learn the stack quickly.
- R4 owns frontend architecture, routing, component standards and code review.
- R5 owns individual pages, forms, tables and API integration under those standards.
- Less framework abstraction means some common components must be written manually.
- If global state becomes difficult to manage, Pinia can be added later.
- If Element Plus is adopted, it should be imported only in the pages that need it.
- API field names and response shapes must follow R6's OpenAPI specification.

## Delivery Rules

- One layout shell and one router configuration.
- One API wrapper in `frontend/src/api/`.
- One component per reusable UI element.
- No duplicated HTML tables or dialogs across pages.
- Mock data is used until the backend endpoint is available.
- Every new dependency must be discussed before it is added.
