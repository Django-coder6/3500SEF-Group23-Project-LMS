import { describe, expect, it } from 'vitest'
import {
  confirmHandover,
  verifyPickupCode,
} from '../../src/api/pickup.js'

describe('pickup API boundary', () => {
  it('exports the two functions used by the page', () => {
    // The page only needs to know about api/pickup.js, not the mock files directly.
    expect(typeof verifyPickupCode).toBe('function')
    expect(typeof confirmHandover).toBe('function')
  })

  it('returns the contract-shaped verification result through the boundary', async () => {
    const result = await verifyPickupCode('4821')

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

  it('returns the contract-shaped handover through the boundary', async () => {
    const handover = await confirmHandover('4821')

    expect(handover).toMatchObject({
      parcelId: '11111111-1111-4111-8111-111111111111',
      result: 'collected',
    })
  })
})
