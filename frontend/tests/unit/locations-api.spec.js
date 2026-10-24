import { describe, expect, it } from 'vitest'
import { fetchLocations } from '../../src/api/locations.js'

describe('locations API boundary', () => {
  it('returns contract-shaped locations', async () => {
    const locations = await fetchLocations()

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

  it('contains all four location statuses', async () => {
    const locations = await fetchLocations()
    const statuses = new Set(locations.map((location) => location.status))

    expect(statuses).toEqual(
      new Set(['available', 'reserved', 'occupied', 'disabled']),
    )
  })

  it('returns copies instead of shared mock objects', async () => {
    const first = await fetchLocations()
    first[0].status = 'disabled'

    const second = await fetchLocations()
    expect(second[0].status).toBe('occupied')
  })
})