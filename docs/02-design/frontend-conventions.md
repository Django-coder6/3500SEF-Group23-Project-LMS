# Frontend Engineering Conventions

| Item | Detail |
|---|---|
| Status | Accepted |
| Version | v1.0 |
| Date | 2026-10-14 |
| Task | T-20-09 |
| Owner | R5 Frontend Developer |
| Agreed with | R4 Frontend Lead |
| Issue | #83, the frontend workstream issue. It carries T-20-05, T-20-09 and T-20-10, so this document answers T-20-09 only and its pull request does not close the issue on its own. |
| Depends on | `docs/02-design/adr/ADR-003-frontend-architecture.md` (T-30-04), `docs/02-design/frontend-page-map.md` (T-30-04), `docs/02-design/design-system.md` (T-20-06), `CONTRIBUTING.md` |

## 1. Purpose

ADR-003 fixes the frontend stack. This document fixes how code is written inside
that stack, so that two developers working in parallel produce one codebase
instead of two styles. It applies to every file under `frontend/`.

Everything here follows ADR-003: Vue 3 with the Composition API, Vite, Vue
Router, Pinia, Element Plus, plain JavaScript, and one Axios wrapper. Nothing in
this document may contradict that ADR; if a rule has to change, the ADR changes
first.

## 2. Folder structure

```text
frontend/
  index.html
  vite.config.js
  package.json
  public/
  src/
    main.js
    App.vue
    style.css            base styles only; the tokens live in styles/tokens.css
    styles/
      tokens.css         design tokens as CSS custom properties
    api/
      http.js            the only Axios instance
      auth.js            one module per feature area, named by
      parcels.js         docs/03-api/frontend-api-dependencies.md
      locations.js
      pickup.js
      exceptions.js
      reports.js
      mock/
        <feature>.js     stands in until the endpoint is approved
    components/          shared components from the page map
    views/               one file per route, one route per file
    stores/              one Pinia store per feature area
    utils/               format.js and other pure helpers
    router/
      index.js           the only router configuration
  tests/                 Vitest unit tests; Playwright tests arrive with T-44-06
```

| What you are adding | Where it goes |
|---|---|
| A page behind a route | `src/views/XView.vue`, registered in `src/router/index.js` |
| Something used by two pages | `src/components/X.vue` |
| A server call | `src/api/<feature>.js`, using `http` from `src/api/http.js` |
| Shared state | `src/stores/<feature>.js` |
| A pure helper | `src/utils/` |
| A style value | a CSS variable, defined in the design system |

## 3. Naming

| Item | Rule | Example |
|---|---|---|
| Folders | lower case, words separated by a hyphen | `docs/02-design` |
| Vue files | PascalCase; a page ends in `View`, a component is a noun | `ParcelDetailView.vue`, `StatusTag.vue`, `AppLayout.vue` |
| Other files | lower case, hyphenated, matching `CONTRIBUTING.md` | `frontend-conventions.md` |
| API modules | one module per feature area, plural, no suffix | `api/parcels.js`, `api/pickup.js` |
| JavaScript variables and functions | camelCase, verbs for functions | `pickupCode`, `formatMoney` |
| Pinia stores | `useXStore`, store id equal to the module name | `useParcelsStore`, id `parcels` |
| Routes | lower case, plural noun for a collection | `/my-parcels`, `/parcels/new` |
| Template attributes | kebab case | `:row-count`, `@row-click` |
| CSS classes | lower case, hyphenated, one block per component | `.page-header`, `.metric-row` |
| Test files | same name as the unit plus `.spec.js` | `format.spec.js` |

## 4. Vue component rules

1. Use `<script setup>` with the Composition API and plain JavaScript. No
   Options API, no TypeScript (ADR-003).
2. One component per file, with the order `script setup`, `template`, `style`.
3. Declare props with `defineProps`, including types and defaults, and emit with
   `defineEmits`. A child never reaches into a parent.
4. Navigation uses `RouterLink` and `router.push()`. The only exception is the
   expired session redirect, which stays in `api/http.js` where it already lives.
5. Split a component when it is used in two places, when it owns two unrelated
   responsibilities, or when the file passes roughly 150 lines. Shared pieces go
   to `src/components/`; a piece that only one page uses stays with that page.
6. A component never imports Axios. It calls an `api/` module or a store action.
7. Styles are `scoped` and use the CSS variables from the design system. Do not
   write a new hex colour in a component.
8. Presentational components (tags, cards, tables) receive data through props and
   emit events. Only pages and container components talk to stores.
9. The shared components are named and owned by the Component Map in
   `frontend-page-map.md`: `PageHeader`, `StatusTag`, `ParcelTable`,
   `LocationGrid`, `MetricCard`, `ConfirmDialog`. Build the named one rather
   than a new variant, and add a Component Map entry before adding a seventh.

## 5. State management (Pinia)

One store per feature area, named after its API module: `stores/auth.js`,
`stores/parcels.js`, `stores/locations.js` (`frontend-api-dependencies.md`,
"Shared State"); further stores follow the same plural naming. The `auth` store in
`src/stores/auth.js` is the reference implementation.

```javascript
import { defineStore } from 'pinia'
import { listParcels } from '../api/parcels'

export const useParcelsStore = defineStore('parcels', {
  state: () => ({ items: [], total: 0, loading: false, error: '' }),
  getters: { isEmpty: (state) => !state.loading && state.items.length === 0 },
  actions: {
    async fetchList(query) {
      this.loading = true
      this.error = ''
      try {
        const data = await listParcels(query)
        this.items = data.items
        this.total = data.total
      } catch (error) {
        this.error = error.message
      } finally {
        this.loading = false
      }
    },
  },
})
```

Rules:

1. Keep server data plus `loading` and `error` in the store.
2. Keep state that only one component needs in that component with `ref()`. A
   dropdown open flag does not belong in a store.
3. An action returns the data, or lets the normalised API error propagate, so the
   page can decide how to show it.
4. A store may import the `auth` store for the session. Any other cross store
   import needs a review, because it usually means the state belongs in one place.
5. `logout()` also resets the domain stores, so the next user never sees cached
   rows.
6. Mock data is allowed only while the backend endpoint does not exist, and only
   with a `TODO(T-xx-yy)` comment naming the owning backend task, exactly as
   `stores/auth.js` does for `POST /auth/login`.
7. A mock is named `MOCK_<NOUN>` and exported (`MOCK_PARCELS`); it never appears
   inline in a template. A mock that stands in for a contract endpoint lives in
   `src/api/mock/<feature>.js`, named after the module it will replace
   (`src/api/mock/parcels.js`), and it exports the same function names and the
   same return shape as the real module will, so switching changes the import and
   not the page (`frontend-api-dependencies.md`, "Mock Strategy"). Field names and
   enum values follow the contract even in mock data, and the mock and its
   `TODO(T-xx-yy)` are deleted in the pull request that lands the real call.

## 6. API layer

One module per feature area, exporting named functions. The names are fixed by
`docs/03-api/frontend-api-dependencies.md`: `auth.js`, `parcels.js`,
`locations.js`, `pickup.js`, `exceptions.js`, `reports.js`. A module that has no
endpoint yet is not created until its first endpoint lands.

```javascript
// src/api/parcels.js
import http from './http'

// T-42-07: staff parcel list, filtered and paginated on the server.
export function listParcels(query) {
  return http.get('/parcels', { params: query })
}

export function createParcel(payload) {
  return http.post('/parcels', payload)
}
```

Rules:

1. One Axios instance in `api/http.js`. A page, component or store never imports
   Axios directly.
2. Function names start with a verb: `listParcels`, `createParcel`,
   `updateLocation`, `revokePickupCode`.
3. The wrapper already returns `response.data`, so callers work with the payload
   only and never with `response.status`.
4. Every failure arrives as one shape, `{ status, code, message, requestId }`,
   produced by the response interceptor in `api/http.js` and pinned by
   `tests/unit/http-error.spec.js`. The contract carries the failure as
   `{ error: { code, message, requestId } }`, and the wrapper flattens it. A page
   shows `message`, uses `status` to choose between an inline error, `ErrorState`
   and a redirect, and may show `requestId` in the error detail so a user's report
   can be matched to one server log line. `requestId` is `null` when the request
   never reached the server.
5. A 401 is handled once, in the interceptor. A page must not repeat that logic.
6. Field names in requests and responses follow the OpenAPI contract owned by R6.
   A page does not rename fields for its own convenience.
7. Errors are never swallowed. A `catch` that only logs is not acceptable: either
   the store keeps the error for the page, or the page shows it.

## 7. Routing and access control

1. `src/router/index.js` is the only router configuration. New routes are added
   as children of the `AppLayout` route, except the login route and the error
   pages.
2. Access is declared in `meta`: `meta.public` for the login page, `meta.roles`
   with one or more of `resident`, `staff`, `owner`, `admin`.
3. Guard behaviour stays in the single `beforeEach`: an unauthenticated user goes
   to `/login` with a `redirect` query, a signed in user is sent away from
   `/login`, and a wrong role goes to `/403`.
4. The role landing route lives in the auth store and follows the page map.
5. A new route is added to the menu list in `AppLayout.vue` in the same pull
   request, otherwise the page is unreachable for the user.
6. Page level access uses `meta.roles`. Anything finer is a `v-if` on a named
   getter of the auth store, never an inline role comparison:
   `v-if="auth.canManageUsers"`. The role list then lives in one place instead of
   in every template. Hiding a control is presentation, not security: the backend
   enforces authorisation on every endpoint.
7. Route parity: every route record in `src/router/index.js` has exactly one row
   in `docs/02-design/frontend-page-map.md`, with the same path, the same view name
   and the same role list. Both files carry sixteen entries today. Change one,
   change the other in the same pull request.

## 8. UI composition

1. Element Plus is the approved component library (ADR-003). Use it page by page
   where it removes custom code: tables, forms, dialogs, date pickers and
   pagination. Plain HTML with project CSS is allowed when it is simpler.
2. Status colours are produced only by `StatusTag`, from the mapping table in
   `design-system.md` section 5.3. `ui-prototype.md` section 6 shows the same
   table for the prototype; if the two ever disagree, the design system wins.
3. Every table with server data supports loading, empty and error states, and
   paginates on the server rather than in the browser.
4. Forms: one label per control, required fields marked, inline field errors,
   submit disabled while a request is in flight, and keyboard submission works.
5. Destructive or irreversible actions (handover, disabling a user, revoking a
   code) go through `ConfirmDialog`, and the dialog states the effect.
6. Money, dates and phone numbers are formatted by helpers in
   `src/utils/format.js`, never inline in a template.
7. Text is English and in sentence case, and an error message explains what the
   user can do next.
8. Element Plus is not registered globally. `src/main.js` records why: global
   registration adds roughly 1 MB to a mobile-first bundle for nothing, so the
   first page that needs a table, a form or a dialog imports what it uses. Moving
   to global or automatic on-demand registration is an architecture change and
   needs its own ADR. `design-system.md` section 9 divides the library into used
   unmodified, themed through our tokens, and built by us, and section 9.4
   requires overrides to be made once, centrally.
9. A page defines no raw colour, spacing or radius value. They come from the
   tokens in `src/styles/tokens.css`, which express `design-system.md`.

## 9. Comments and traceability

1. Every new view, store and API module starts with a short comment naming its
   task ID, for example `// T-42-06: location board for the staff pages.`
2. Comments explain why something is done. The existing skeleton files, such as
   `frontend/src/router/index.js`, are the reference for the expected level.
3. Do not leave commented out code or `console.log` in a pull request.
4. A reference to another document names the file and the section
   (`design-system.md` section 5.3), so a reader can verify it.

## 10. Before opening a pull request

1. `npm run lint` and `npm run test:run` pass. Both scripts exist since PR #97,
   and a red lint or a red test is not waived in review. `npm run build` passes
   and a manual `npm run dev` check of the touched pages shows no console error.
2. A new store, helper or non trivial component comes with a Vitest spec under
   `tests/unit/`. The three existing specs are the reference; end to end tests
   belong to T-44-06.
3. Manual check per page: role access, loading, empty, error, busy state,
   destructive confirmation, and the 640px width.
4. Branch from the latest `develop`, and open the pull request against `develop`
   with the title format and the evidence required by `CONTRIBUTING.md`
   (`<type>(<scope>): <subject> [TASK-ID]`).

## 11. Placement cheat sheet

| Question | Answer |
|---|---|
| New page? | `src/views/`, registered in `router/index.js`, added to the `AppLayout` menu |
| Used by two pages? | `src/components/` |
| Calls the backend? | `src/api/<feature>.js` |
| Shared between pages? | `src/stores/<feature>.js` |
| Formatting or calculation? | `src/utils/` |
| Needs a colour or spacing? | a CSS variable from the design system |
| Temporary mock? | `src/api/mock/<feature>.js`, `MOCK_<NOUN>`, with `TODO(T-xx-yy)` |



## Revision history

| Version | Week | Change | Author |
|---|---|---|---|
| v1.0 | Week 2 | First version recorded in this file. Folder structure, naming, component split and state management for `frontend/`. Any earlier edit is not listed: this file had no revision history until week 3, so records of those edits do not exist here. | R1 |
