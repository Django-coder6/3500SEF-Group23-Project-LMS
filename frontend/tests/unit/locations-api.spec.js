import {
  beforeEach,
  describe,
  expect,
  it,
  vi,
} from 'vitest'
import http from '../../src/api/http.js'
import { fetchLocations } from '../../src/api/locations.js'
import { createLocationsFixture } from '../fixtures/locations.js'

vi.mock('../../src/api/http.js', () => ({
  default: {
    get: vi.fn(),
  },
}))

describe('locations API boundary', () => {
  beforeEach(() => {
    http.get.mockReset()
  })

  it('requests the location list and returns contract-shaped data', async () => {
    const fixture = createLocationsFixture()
    http.get.mockResolvedValue(fixture)

    const locations = await fetchLocations()

    expect(http.get).toHaveBeenCalledTimes(1)
    expect(http.get).toHaveBeenCalledWith('/locations')
    expect(locations.length).toBeGreaterThan(0)
    expect(locations[0]).toMatchObject({
      id: expect.any(String),
      code: expect.any(String),
      zone: expect.any(String),
      size: expect.stringMatching(/^(small|medium|large)$/),
      status: expect.stringMatching(
        /^(available|reserved|occupied|disabled)$/,
      ),
    })
  })

  it('returns all four location statuses from the fixture', async () => {
    http.get.mockResolvedValue(createLocationsFixture())

    const locations = await fetchLocations()
    const statuses = new Set(locations.map((location) => location.status))

    expect(statuses).toEqual(
      new Set(['available', 'reserved', 'occupied', 'disabled']),
    )
  })

  it('provides independent fixture copies', async () => {
    http.get.mockImplementation(async () => createLocationsFixture())

    const first = await fetchLocations()
    first[0].status = 'disabled'

    const second = await fetchLocations()

    expect(first).not.toBe(second)
    expect(second[0].status).toBe('occupied')
  })
})