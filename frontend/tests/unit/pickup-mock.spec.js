import { describe, expect, it } from 'vitest'
import {
  confirmHandover,
  verifyPickupCode,
} from '../../src/api/mock/pickup.js'

describe('pickup mock', () => {
  it('returns a parcel and location for the happy path', async () => {
    // 4821 is the successful scenario.
    const result = await verifyPickupCode('4821')

    expect(result.pickupCode).toBe('4821')
    expect(result.parcelSummary.status).toBe('shelved')
    expect(result.location.code).toBe('A-03-2')
  })

  it('returns copies instead of the shared mock objects', async () => {
    // When the page modifies the first returned value, it should not pollute the next return value.
    const first = await verifyPickupCode('4821')
    first.parcelSummary.status = 'collected'

    const second = await verifyPickupCode('4821')
    expect(second.parcelSummary.status).toBe('shelved')
  })

  it('returns the same handover for a repeated confirmation', async () => {
    // Two confirmations should return the same result, simulating the idempotency of NFR-4.
    const first = await confirmHandover('4821')
    const second = await confirmHandover('4821')

    expect(second).toEqual(first)
  })

  it('rejects an unknown code', async () => {
    await expect(verifyPickupCode('0000')).rejects.toMatchObject({
      status: 404,
      code: 'PICKUP_CODE_INVALID',
      requestId: null,
    })
  })

  it('exposes expired, used and not-on-shelf states', async () => {
    await expect(verifyPickupCode('4822')).rejects.toMatchObject({
      code: 'PICKUP_CODE_EXPIRED',
    })

    await expect(verifyPickupCode('4823')).rejects.toMatchObject({
      code: 'PICKUP_CODE_USED',
    })

    await expect(verifyPickupCode('4824')).rejects.toMatchObject({
      code: 'PARCEL_NOT_ON_SHELF',
    })
  })
})
