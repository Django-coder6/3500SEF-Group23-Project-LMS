const SUCCESS_PICKUP_CODE = '4821'

const ALLOWED_RESULTS = [
  'collected',
  'returned',
  'exception',
]

const PARCEL = {
  id: '11111111-1111-4111-8111-111111111111',
  trackingNo: 'SF1234567890',
  carrier: 'SF',
  recipientName: 'Resident',
  recipientPhone: '138****8888',
  status: 'shelved',
  size: 'medium',
  locationId: '22222222-2222-4222-8222-222222222222',
  expectedVersion: 1,
  createdAt: '2026-10-23T08:00:00Z',
  updatedAt: '2026-10-23T08:00:00Z',
}

const LOCATION = {
  id: '22222222-2222-4222-8222-222222222222',
  code: 'A-03-2',
  zone: 'A',
  size: 'medium',
  status: 'occupied',
  parcelId: '11111111-1111-4111-8111-111111111111',
}

const PICKUP_ERRORS = {
  4822: {
    status: 404,
    code: 'PICKUP_CODE_EXPIRED',
    message: 'This pickup code has expired.',
  },
  4823: {
    status: 404,
    code: 'PICKUP_CODE_USED',
    message: 'This pickup code has already been used.',
  },
  4824: {
    status: 409,
    code: 'PARCEL_NOT_ON_SHELF',
    message: 'The parcel is not in its recorded location.',
  },
}

function copy(value) {
  return structuredClone(value)
}

function fixtureError(status, code, message) {
  const error = new Error(message)

  error.status = status
  error.code = code
  error.requestId = null

  return error
}

/**
 * Create an isolated pickup fixture for one test.
 *
 * Each test receives its own handover map, so repeated confirmation behavior
 * does not leak between tests.
 */
export function createPickupFixture() {
  const handovers = new Map()

  async function verifyPickupCode(pickupCode) {
    if (pickupCode === SUCCESS_PICKUP_CODE) {
      return {
        pickupCode,
        parcelSummary: copy(PARCEL),
        location: copy(LOCATION),
      }
    }

    const failure = PICKUP_ERRORS[pickupCode]

    if (failure) {
      throw fixtureError(
        failure.status,
        failure.code,
        failure.message,
      )
    }

    throw fixtureError(
      404,
      'PICKUP_CODE_INVALID',
      'This code is not recognised.',
    )
  }

  async function confirmHandover(
    pickupCode,
    result = 'collected',
  ) {
    if (pickupCode !== SUCCESS_PICKUP_CODE) {
      throw fixtureError(
        404,
        'PICKUP_CODE_INVALID',
        'This pickup code cannot be confirmed.',
      )
    }

    if (!ALLOWED_RESULTS.includes(result)) {
      throw fixtureError(
        400,
        'VALIDATION_ERROR',
        'The handover result is not valid.',
      )
    }

    if (!handovers.has(pickupCode)) {
      handovers.set(pickupCode, {
        id: '33333333-3333-4333-8333-333333333333',
        parcelId: PARCEL.id,
        result,
        createdAt: '2026-10-23T08:30:00Z',
      })
    }

    return copy(handovers.get(pickupCode))
  }

  return {
    verifyPickupCode,
    confirmHandover,
  }
}