import {
  afterEach,
  beforeEach,
  describe,
  expect,
  it,
} from 'vitest'
import http, {
  EXPIRY_KEY,
  TOKEN_KEY,
  USER_KEY,
} from '../../src/api/http.js'

const originalAdapter = http.defaults.adapter

beforeEach(() => {
  window.history.replaceState({}, '', '/login')
  localStorage.clear()
  localStorage.setItem(TOKEN_KEY, 'test-token')
  localStorage.setItem(USER_KEY, JSON.stringify({ role: 'staff' }))
  localStorage.setItem(EXPIRY_KEY, String(Date.now() + 60_000))
})

afterEach(() => {
  http.defaults.adapter = originalAdapter
  localStorage.clear()
})

describe('http interceptors and error normalisation', () => {
  it('adds the bearer token to requests', async () => {
    let authorization

    http.defaults.adapter = async (config) => {
      authorization = config.headers.get('Authorization')

      return {
        data: { items: [], total: 0 },
        status: 200,
        statusText: 'OK',
        headers: {},
        config,
      }
    }

    await http.get('/parcels')

    expect(authorization).toBe('Bearer test-token')
  })

  it('flattens the contract error object into { status, code, message, requestId }', async () => {
    http.defaults.adapter = async (config) => {
      const error = new Error('Request failed with status code 422')
      error.config = config
      error.response = {
        status: 422,
        data: {
          error: {
            code: 'VALIDATION',
            message: 'bad input',
            requestId: 'req-1',
          },
        },
      }
      throw error
    }

    await expect(http.get('/parcels')).rejects.toEqual({
      status: 422,
      code: 'VALIDATION',
      message: 'bad input',
      requestId: 'req-1',
    })
  })

  it('still produces a usable error when the failure never reached the server', async () => {
    http.defaults.adapter = async (config) => {
      const error = new Error('Network Error')
      error.config = config
      throw error
    }

    await expect(http.get('/parcels')).rejects.toEqual({
      status: 0,
      code: 'REQUEST_FAILED',
      message: 'Network Error',
      requestId: null,
    })
  })

  it('clears the complete stored session on 401', async () => {
    http.defaults.adapter = async (config) => {
      const error = new Error('Request failed with status code 401')
      error.config = config
      error.response = {
        status: 401,
        data: {
          error: {
            code: 'UNAUTHORIZED',
            message: 'Unauthorized',
            requestId: 'req-401',
          },
        },
      }
      throw error
    }

    await expect(http.get('/parcels')).rejects.toMatchObject({
      status: 401,
      code: 'UNAUTHORIZED',
      requestId: 'req-401',
    })

    expect(localStorage.getItem(TOKEN_KEY)).toBeNull()
    expect(localStorage.getItem(USER_KEY)).toBeNull()
    expect(localStorage.getItem(EXPIRY_KEY)).toBeNull()
  })

  it('returns a successful response body unchanged', async () => {
    http.defaults.adapter = async (config) => ({
      data: { items: [], total: 0 },
      status: 200,
      statusText: 'OK',
      headers: {},
      config,
    })

    await expect(http.get('/parcels')).resolves.toEqual({
      items: [],
      total: 0,
    })
  })
})