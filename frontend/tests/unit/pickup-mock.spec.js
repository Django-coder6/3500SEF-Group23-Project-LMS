import {
  beforeEach,
  describe,
  expect,
  it,
} from 'vitest'
import { createPickupFixture } from '../fixtures/pickup.js'

let fixture

beforeEach(() => {
  fixture = createPickupFixture()
})

describe('pickup fixture', () => {
  it('returns a parcel and location for the happy path', async () => {
    const result = await fixture.verifyPickupCode('4821')

    expect(result.pickupCode).toBe('4821')
    expect(result.parcelSummary.status).toBe('shelved')
    expect(result.location.code).toBe('A-03-2')
  })

  it('returns copies instead of shared fixture objects', async () => {
    const first = await fixture.verifyPickupCode('4821')
    first.parcelSummary.status = 'collected'

    const second = await fixture.verifyPickupCode('4821')

    expect(second.parcelSummary.status).toBe('shelved')
  })

  it('returns the same handover for a repeated confirmation', async () => {
    const first = await fixture.confirmHandover('4821')
    const second = await fixture.confirmHandover('4821')

    expect(second).toEqual(first)
  })

  it('rejects an unknown code', async () => {
    await expect(
      fixture.verifyPickupCode('0000'),
    ).rejects.toMatchObject({
      status: 404,
      code: 'PICKUP_CODE_INVALID',
      requestId: null,
    })
  })

  it('exposes expired, used and not-on-shelf states', async () => {
    await expect(
      fixture.verifyPickupCode('4822'),
    ).rejects.toMatchObject({
      code: 'PICKUP_CODE_EXPIRED',
    })

    await expect(
      fixture.verifyPickupCode('4823'),
    ).rejects.toMatchObject({
      code: 'PICKUP_CODE_USED',
    })

    await expect(
      fixture.verifyPickupCode('4824'),
    ).rejects.toMatchObject({
      code: 'PARCEL_NOT_ON_SHELF',
    })
  })
})