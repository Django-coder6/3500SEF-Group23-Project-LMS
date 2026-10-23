import axios from 'axios'

// The only Axios instance in the application. ADR-003 requires this single
// wrapper so that pages never talk to Axios directly: a page imports this
// module and never imports axios itself.
//
// Two shapes are defined by the contract (OpenAPI v0.3.0-draft) and this file
// is the one place that knows about them:
//
//   success  the payload is returned FLAT, with no envelope. For example
//            POST /auth/login answers { token, user } and
//            GET /parcels answers { items, total, page, pageSize }.
//   failure  the payload is wrapped as { error: { code, message, requestId } }.

const TOKEN_KEY = 'auth.token'
const USER_KEY = 'auth.user'
const EXPIRY_KEY = 'auth.expiry'

// Base path follows ADR-003: the browser calls /api and the Vite dev server
// proxies /api to the backend (see vite.config.js), so local development does
// not depend on CORS. VITE_API_BASE_URL overrides the path for a deployed build
// where the frontend and the backend are served from different origins.
const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000,
})

// --- Request interceptor ----------------------------------------------------
// One place injects both the token and a request id, so every call is
// traceable and no page has to remember to do it.
//
// A request with no stored token goes out without an Authorization header:
// that is what a signed-out visitor sends, and it is also what /auth/login and
// /health expect (the contract marks them as the only endpoints without a
// token). Everything else the contract expects to carry one.
http.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_KEY)
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  // A per-request id makes one browser action findable in the server log.
  config.headers['X-Request-Id'] = crypto.randomUUID()
  return config
})

// --- Response interceptor ---------------------------------------------------
http.interceptors.response.use(
  // Success: axios' own `response.data` is already the flat payload the
  // contract returns, so unwrapping it here is all that is needed. If the
  // backend ever adds an envelope, this is the single line that changes.
  (response) => response.data,

  // Failure: flatten the contract's error object into one predictable shape,
  // { status, code, message, requestId }, so a page only ever handles one
  // thing and never reads an axios error.
  (error) => {
    const status = error.response?.status ?? 0
    // The contract puts the error inside an `error` object. Both lookups fall
    // back, because a network failure has no response body at all and the
    // caller still needs something usable to show.
    const body = error.response?.data?.error

    if (status === 401) {
      // An expired or rejected token must send the user back to the login page.
      // The rule lives here, in one place, instead of in every page.
      //
      // The reload is deliberate: a router guard cannot run inside an
      // interceptor, and a full reload avoids a circular import between the
      // router, the store and this file.
      localStorage.removeItem(TOKEN_KEY)
      localStorage.removeItem(USER_KEY)
      localStorage.removeItem(EXPIRY_KEY)
      if (window.location.pathname !== '/login') {
        window.location.assign('/login')
      }
    }

    return Promise.reject({
      status,
      code: body?.code ?? 'REQUEST_FAILED',
      message: body?.message ?? error.message,
      // The contract supplies this id so a user-facing error report can be
      // matched to one server log line. Null when the failure never reached
      // the server (offline, DNS, timeout).
      requestId: body?.requestId ?? null,
    })
  },
)

export { TOKEN_KEY, USER_KEY, EXPIRY_KEY }
export default http
