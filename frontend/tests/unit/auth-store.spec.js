import {
  afterEach,
  beforeEach,
  describe,
  expect,
  it,
  vi,
} from 'vitest'
import { createPinia, setActivePinia } from 'pinia'
import { useAuthStore } from '../../src/stores/auth.js'
import http, {
  EXPIRY_KEY,
  TOKEN_KEY,
  USER_KEY,
} from '../../src/api/http.js'

function makeToken({
  userId,
  role,
  collectionPointId = null,
  expiresAt = Date.now() + 60 * 60 * 1000,
}) {
  const encode = (value) => btoa(JSON.stringify(value))
    .replace(/\+/g, '-')
    .replace(/\//g, '_')
    .replace(/=+$/, '')

  const header = encode({ alg: 'HS256', typ: 'JWT' })
  const payload = encode({
    sub: userId,
    role,
    collectionPointId,
    exp: Math.floor(expiresAt / 1000),
  })

  return `${header}.${payload}.signature`
}

// A resident lands on /my-parcels, staff and owner land on /dashboard,
// and an admin lands on /users.
describe('auth store session', () => {
  beforeEach(() => {
    localStorage.clear()
    setActivePinia(createPinia())

    vi.spyOn(http, 'post').mockImplementation(async (_url, body) => {
      const role = body.username
      const userId = `${role}-user-id`
      const collectionPointId = role === 'admin'
        ? null
        : `${role}-collection-point-id`

      return {
        token: makeToken({
          userId,
          role,
          collectionPointId,
        }),
        user: {
          id: userId,
          username: role,
          roles: [role],
        },
      }
    })
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('sends a resident to /my-parcels', async () => {
    const auth = useAuthStore()
    const landing = await auth.login('resident', 'resident')

    expect(auth.role).toBe('resident')
    expect(landing).toBe('/my-parcels')
  })

  it.each([
    ['staff', '/dashboard'],
    ['owner', '/dashboard'],
    ['admin', '/users'],
  ])('sends %s to %s', async (username, expected) => {
    const auth = useAuthStore()

    expect(await auth.login(username, username)).toBe(expected)
  })

  it('calls the real login endpoint with the supplied credentials', async () => {
    const auth = useAuthStore()
    await auth.login('resident', 'resident')

    expect(http.post).toHaveBeenCalledWith('/auth/login', {
      username: 'resident',
      password: 'resident',
    })
  })

  it('rejects invalid credentials and stays signed out', async () => {
    http.post.mockRejectedValueOnce({
      status: 401,
      code: 'INVALID_CREDENTIALS',
      message: 'Invalid username or password.',
      requestId: 'req-401',
    })

    const auth = useAuthStore()

    await expect(
      auth.login('resident', 'wrong'),
    ).rejects.toMatchObject({
      code: 'INVALID_CREDENTIALS',
    })

    expect(auth.isAuthenticated).toBe(false)
    expect(auth.user).toBeNull()
    expect(auth.landingRoute()).toBe('/login')
  })

  it('stores the token, principal and expiry', async () => {
    const auth = useAuthStore()
    await auth.login('resident', 'resident')

    expect(auth.token).toBeTruthy()
    expect(auth.user).toMatchObject({
      userId: expect.any(String),
      username: 'resident',
      role: 'resident',
      collectionPointId: expect.any(String),
      fullName: 'resident',
    })
    expect(auth.expiresAt).toBeGreaterThan(Date.now())

    expect(localStorage.getItem(TOKEN_KEY)).toBe(auth.token)
    expect(JSON.parse(localStorage.getItem(USER_KEY))).toEqual(auth.user)
    expect(Number(localStorage.getItem(EXPIRY_KEY))).toBe(auth.expiresAt)
  })

  it('restores a valid session after the store is recreated', async () => {
    const auth = useAuthStore()
    await auth.login('staff', 'staff')

    const savedToken = auth.token
    const savedUser = auth.user
    const savedExpiry = auth.expiresAt

    setActivePinia(createPinia())
    const restored = useAuthStore()

    expect(restored.token).toBe(savedToken)
    expect(restored.user).toEqual(savedUser)
    expect(restored.expiresAt).toBe(savedExpiry)
    expect(restored.isAuthenticated).toBe(true)
    expect(restored.role).toBe('staff')
  })

  it('rejects and clears an expired session', async () => {
    const auth = useAuthStore()
    await auth.login('staff', 'staff')

    auth.expiresAt = Date.now() - 1

    expect(auth.isAuthenticated).toBe(false)
    expect(auth.ensureValidSession()).toBe(false)

    expect(auth.token).toBe('')
    expect(auth.user).toBeNull()
    expect(auth.expiresAt).toBe(0)

    expect(localStorage.getItem(TOKEN_KEY)).toBeNull()
    expect(localStorage.getItem(USER_KEY)).toBeNull()
    expect(localStorage.getItem(EXPIRY_KEY)).toBeNull()
  })

  it('clears the store and stored session data on logout', async () => {
    const auth = useAuthStore()
    await auth.login('owner', 'owner')

    auth.logout()

    expect(auth.token).toBe('')
    expect(auth.user).toBeNull()
    expect(auth.expiresAt).toBe(0)
    expect(auth.isAuthenticated).toBe(false)
    expect(auth.role).toBeNull()

    expect(localStorage.getItem(TOKEN_KEY)).toBeNull()
    expect(localStorage.getItem(USER_KEY)).toBeNull()
    expect(localStorage.getItem(EXPIRY_KEY)).toBeNull()
  })
})