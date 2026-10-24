import { defineStore } from 'pinia'
import http, {
  EXPIRY_KEY,
  TOKEN_KEY,
  USER_KEY,
} from '../api/http'

// Taken from the route rules in docs/02-design/frontend-page-map.md.
const LANDING_ROUTE = {
  resident: '/my-parcels',
  staff: '/dashboard',
  owner: '/dashboard',
  admin: '/users',
}

function readUser() {
  try {
    return JSON.parse(localStorage.getItem(USER_KEY) || 'null')
  } catch {
    return null
  }
}

function clearStoredSession() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
  localStorage.removeItem(EXPIRY_KEY)
}

function readSession() {
  const token = localStorage.getItem(TOKEN_KEY) || ''
  const user = readUser()
  const expiresAt = Number(localStorage.getItem(EXPIRY_KEY) || 0)

  if (!token || !user?.role || !expiresAt || expiresAt <= Date.now()) {
    clearStoredSession()
    return { token: '', user: null, expiresAt: 0 }
  }

  return { token, user, expiresAt }
}

function decodeJwtPayload(token) {
  const encoded = token.split('.')[1]

  if (!encoded) {
    return null
  }

  try {
    const base64 = encoded
      .replace(/-/g, '+')
      .replace(/_/g, '/')
      .padEnd(Math.ceil(encoded.length / 4) * 4, '=')

    const binary = atob(base64)
    const bytes = Uint8Array.from(binary, (char) => char.charCodeAt(0))
    return JSON.parse(new TextDecoder().decode(bytes))
  } catch {
    return null
  }
}

function principalFromLogin(token, responseUser) {
  const payload = decodeJwtPayload(token)
  const role = payload?.role ?? responseUser?.roles?.[0]
  const userId = payload?.sub ?? responseUser?.id
  const expiresAt = Number(payload?.exp) * 1000

  if (!userId || !role || !Number.isFinite(expiresAt) || expiresAt <= Date.now()) {
    const error = new Error('The login response did not contain a usable session')
    error.code = 'INVALID_LOGIN_RESPONSE'
    throw error
  }

  return {
    userId,
    username: responseUser?.username ?? '',
    role,
    collectionPointId: payload?.collectionPointId ?? null,
    fullName: payload?.name ?? responseUser?.username ?? 'User',
    expiresAt,
  }
}

export const useAuthStore = defineStore('auth', {
  state: () => readSession(),

  getters: {
    isAuthenticated: (state) => Boolean(
      state.token
      && state.user?.role
      && state.expiresAt > Date.now(),
    ),
    role: (state) => state.user?.role ?? null,
  },

  actions: {
    async login(username, password) {
      const response = await http.post('/auth/login', {
        username,
        password,
      })

      if (!response?.token) {
        const error = new Error('The login response did not contain a token')
        error.code = 'INVALID_LOGIN_RESPONSE'
        throw error
      }

      const user = principalFromLogin(response.token, response.user)

      this.token = response.token
      this.user = {
        userId: user.userId,
        username: user.username,
        role: user.role,
        collectionPointId: user.collectionPointId,
        fullName: user.fullName,
      }
      this.expiresAt = user.expiresAt

      localStorage.setItem(TOKEN_KEY, this.token)
      localStorage.setItem(USER_KEY, JSON.stringify(this.user))
      localStorage.setItem(EXPIRY_KEY, String(this.expiresAt))

      return this.landingRoute()
    },

    logout() {
      this.token = ''
      this.user = null
      this.expiresAt = 0
      clearStoredSession()
    },

    ensureValidSession() {
      if (!this.isAuthenticated) {
        this.logout()
        return false
      }

      return true
    },

    landingRoute() {
      return LANDING_ROUTE[this.role] ?? '/login'
    },
  },
})