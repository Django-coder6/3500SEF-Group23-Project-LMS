import {
  afterEach,
  beforeEach,
  describe,
  expect,
  it,
  vi,
} from 'vitest'
import { flushPromises, mount } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import {
  createMemoryHistory,
  createRouter,
} from 'vue-router'
import http from '../../src/api/http.js'
import LoginView from '../../src/views/LoginView.vue'

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

  const payload = encode({
    sub: userId,
    role,
    collectionPointId,
    exp: Math.floor(expiresAt / 1000),
  })

  return `header.${payload}.signature`
}

function loginResponse(role) {
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
}

async function mountLoginView({
  redirect = null,
} = {}) {
  const pinia = createPinia()
  setActivePinia(pinia)

  const router = createRouter({
    history: createMemoryHistory(),
    routes: [
      {
        path: '/login',
        name: 'login',
        component: LoginView,
      },
      {
        path: '/dashboard',
        name: 'dashboard',
        component: { template: '<div>Dashboard</div>' },
      },
      {
        path: '/my-parcels',
        name: 'my-parcels',
        component: { template: '<div>My parcels</div>' },
      },
      {
        path: '/users',
        name: 'users',
        component: { template: '<div>Users</div>' },
      },
    ],
  })

  await router.push({
    path: '/login',
    query: redirect ? { redirect } : {},
  })
  await router.isReady()

  const wrapper = mount(LoginView, {
    attachTo: document.body,
    global: {
      plugins: [pinia, router],
    },
  })

  return { wrapper, router }
}

describe('LoginView', () => {
  beforeEach(() => {
    localStorage.clear()
    vi.spyOn(http, 'post').mockResolvedValue(
      loginResponse('staff'),
    )
  })

  afterEach(() => {
    vi.restoreAllMocks()
    localStorage.clear()
    document.body.innerHTML = ''
  })

  it('renders the login form with accessible labels', async () => {
    const { wrapper } = await mountLoginView()

    expect(wrapper.get('h1').text()).toBe('Sign in')
    const labels = wrapper.findAll('label')
    expect(labels).toHaveLength(2)
    expect(labels[0].text()).toContain('Username')
    expect(labels[1].text()).toContain('Password')
    expect(wrapper.get('input[name="username"]').exists()).toBe(true)
    expect(wrapper.get('input[name="password"]').exists()).toBe(true)
    expect(wrapper.get('button[type="submit"]').text()).toBe('Sign in')

    wrapper.unmount()
  })

  it('shows the generic error for invalid credentials', async () => {
    http.post.mockRejectedValueOnce({
      status: 401,
      code: 'INVALID_CREDENTIALS',
      message: 'Invalid username or password.',
      requestId: 'req-401',
    })

    const { wrapper } = await mountLoginView()

    await wrapper.get('input[name="username"]').setValue('resident')
    await wrapper.get('input[name="password"]').setValue('wrong')
    await wrapper.get('form').trigger('submit')
    await flushPromises()

    const error = wrapper.get('[role="alert"]')
    expect(error.text()).toBe('Invalid username or password.')
    expect(wrapper.get('input[name="username"]').attributes('aria-invalid'))
      .toBe('true')

    wrapper.unmount()
  })

  it('disables the form while login is in flight', async () => {
    let finishRequest

    http.post.mockImplementationOnce(() => new Promise((resolve) => {
      finishRequest = resolve
    }))

    const { wrapper } = await mountLoginView()

    await wrapper.get('input[name="username"]').setValue('staff')
    await wrapper.get('input[name="password"]').setValue('staff')
    await wrapper.get('form').trigger('submit')

    expect(wrapper.get('input[name="username"]').attributes('disabled'))
      .toBeDefined()
    expect(wrapper.get('input[name="password"]').attributes('disabled'))
      .toBeDefined()
    expect(wrapper.get('button[type="submit"]').attributes('disabled'))
      .toBeDefined()
    expect(wrapper.get('button[type="submit"]').text())
      .toBe('Signing in...')

    finishRequest(loginResponse('staff'))
    await flushPromises()

    wrapper.unmount()
  })

  it('sends staff to their role landing page', async () => {
    const { wrapper, router } = await mountLoginView()

    await wrapper.get('input[name="username"]').setValue('staff')
    await wrapper.get('input[name="password"]').setValue('staff')
    await wrapper.get('form').trigger('submit')
    await flushPromises()

    expect(router.currentRoute.value.path).toBe('/dashboard')

    wrapper.unmount()
  })

  it('sends admin to /users', async () => {
    http.post.mockResolvedValueOnce(loginResponse('admin'))

    const { wrapper, router } = await mountLoginView()

    await wrapper.get('input[name="username"]').setValue('admin')
    await wrapper.get('input[name="password"]').setValue('admin')
    await wrapper.get('form').trigger('submit')
    await flushPromises()

    expect(router.currentRoute.value.path).toBe('/users')

    wrapper.unmount()
  })

  it('honours a safe internal redirect', async () => {
    const { wrapper, router } = await mountLoginView({
      redirect: '/dashboard',
    })

    await wrapper.get('input[name="username"]').setValue('staff')
    await wrapper.get('input[name="password"]').setValue('staff')
    await wrapper.get('form').trigger('submit')
    await flushPromises()

    expect(router.currentRoute.value.path).toBe('/dashboard')

    wrapper.unmount()
  })

  it('ignores an external redirect and uses the role landing page', async () => {
    const { wrapper, router } = await mountLoginView({
      redirect: 'https://example.com',
    })

    await wrapper.get('input[name="username"]').setValue('staff')
    await wrapper.get('input[name="password"]').setValue('staff')
    await wrapper.get('form').trigger('submit')
    await flushPromises()

    expect(router.currentRoute.value.path).toBe('/dashboard')

    wrapper.unmount()
  })
})