import {
  afterEach,
  beforeEach,
  describe,
  expect,
  it,
  vi,
} from 'vitest'
import { createPinia, setActivePinia } from 'pinia'

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

async function signIn(username) {
  const storage = await import('../../src/api/http.js')
  const http = storage.default
  const role = username
  const userId = `${role}-user-id`
  const collectionPointId = role === 'admin'
    ? null
    : `${role}-collection-point-id`

  vi.spyOn(http, 'post').mockResolvedValue({
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
  })

  const { useAuthStore } = await import('../../src/stores/auth.js')
  const auth = useAuthStore()
  await auth.login(username, username)

  return { auth, storage }
}

// The guard in src/router/index.js is the executable form of the route rules in
// docs/02-design/frontend-page-map.md.
describe('route guard', () => {
  beforeEach(() => {
    vi.resetModules()
    localStorage.clear()
    setActivePinia(createPinia())
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('sends an unauthenticated visitor to /login and remembers the target', async () => {
    const router = (await import('../../src/router/index.js')).default
    await router.push('/dashboard')

    expect(router.currentRoute.value.path).toBe('/login')
    expect(router.currentRoute.value.query.redirect).toBe('/dashboard')
  })

  it('sends a resident away from a staff route', async () => {
    await signIn('resident')

    const router = (await import('../../src/router/index.js')).default
    await router.push('/dashboard')

    expect(router.currentRoute.value.path).toBe('/403')
  })

  it('lets an allowed role through', async () => {
    await signIn('staff')

    const router = (await import('../../src/router/index.js')).default
    await router.push('/parcels')

    expect(router.currentRoute.value.path).toBe('/parcels')
  })

  it('sends a resident away from the staff pickup page', async () => {
    await signIn('resident')

    const router = (await import('../../src/router/index.js')).default
    await router.push('/pickup')

    expect(router.currentRoute.value.path).toBe('/403')
  })

  it('lets staff open the pickup page', async () => {
    await signIn('staff')

    const router = (await import('../../src/router/index.js')).default
    await router.push('/pickup')

    expect(router.currentRoute.value.path).toBe('/pickup')
  })

  it('sends an authenticated admin from /login to /users', async () => {
    await signIn('admin')

    const router = (await import('../../src/router/index.js')).default
    await router.push('/login')

    expect(router.currentRoute.value.path).toBe('/users')
  })

  it('clears an expired session and sends the user to /login', async () => {
    const { auth, storage } = await signIn('staff')
    auth.expiresAt = Date.now() - 1

    const router = (await import('../../src/router/index.js')).default
    await router.push('/dashboard')

    expect(router.currentRoute.value.path).toBe('/login')
    expect(localStorage.getItem(storage.TOKEN_KEY)).toBeNull()
    expect(localStorage.getItem(storage.USER_KEY)).toBeNull()
    expect(localStorage.getItem(storage.EXPIRY_KEY)).toBeNull()
  })
})