import {
  beforeEach,
  describe,
  expect,
  it,
  vi,
} from 'vitest'
import http from '../../src/api/http.js'
import {
  confirmHandover,
  verifyPickupCode,
} from '../../src/api/pickup.js'
import { createPickupFixture } from '../fixtures/pickup.js'

vi.mock('../../src/api/http.js', () => ({
  default: {
    post: vi.fn(),
  },
}))

describe('pickup API boundary', () => {
  beforeEach(() => {
    http.post.mockReset()
  })

  it('exports the two functions used by the page', () => {
    expect(typeof verifyPickupCode).toBe('function')
    expect(typeof confirmHandover).toBe('function')
  })

  it('posts the pickup code to the verification endpoint', async () => {
    const fixture = createPickupFixture()
    const expected = await fixture.verifyPickupCode('4821')
    http.post.mockResolvedValue(expected)

    const result = await verifyPickupCode('4821')

    expect(http.post).toHaveBeenCalledTimes(1)
    expect(http.post).toHaveBeenCalledWith('/pickup/verify', {
      pickupCode: '4821',
    })
    expect(result).toEqual(expected)
    expect(result).toMatchObject({
      pickupCode: '4821',
      parcelSummary: {
        trackingNo: 'SF1234567890',
        status: 'shelved',
      },
      location: {
        code: 'A-03-2',
        status: 'occupied',
      },
    })
  })

  it('posts the code and result to the confirmation endpoint', async () => {
    const fixture = createPickupFixture()
    const expected = await fixture.confirmHandover('4821', 'collected')
    http.post.mockResolvedValue(expected)

    const result = await confirmHandover('4821', 'collected')

    expect(http.post).toHaveBeenCalledTimes(1)
    expect(http.post).toHaveBeenCalledWith('/pickup/confirm', {
      pickupCode: '4821',
      result: 'collected',
    })
    expect(result).toEqual(expected)
    expect(result).toMatchObject({
      parcelId: '11111111-1111-4111-8111-111111111111',
      result: 'collected',
    })
  })
})