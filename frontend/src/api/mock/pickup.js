// T-42-05: temporary pickup mock for the handover page.
// TODO(T-42-04): delete this mock when the real backend endpoints land.

// MOCK_PARCEL is a fake parcel returned by the mock API.
// Fields come from the Parcel schema in docs/03-api/openapi.yaml
const MOCK_PARCEL = {
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

// MOCK_LOCATION is a fake location returned by the mock API.
// Its id must match MOCK_PARCEL.locationId.
// Its parcelId must match MOCK_PARCEL.id.
const MOCK_LOCATION = {
  id: '22222222-2222-4222-8222-222222222222',
  code: 'A-03-2',
  zone: 'A',
  size: 'medium',
  status: 'occupied',
  parcelId: '11111111-1111-4111-8111-111111111111',
}

// ALLOWED_RESULTS is a list of allowed values for the result field in handover requests.
// It must match the Handover.result enum in docs/03-api/openapi.yaml.
const ALLOWED_RESULTS = ['collected', 'returned', 'exception']

// SUCCESS_PICKUP_CODE is the pickup code used for successful demonstration scenarios.
const SUCCESS_PICKUP_CODE = '4821'

// Each demonstration code corresponds to a failure scenario.
// Those error codes are only used for frontend mock; the正式 contract has not confirmed all error codes yet.
const MOCK_ERRORS = new Map([
  [
    '4822',
    {
      status: 404,
      code: 'PICKUP_CODE_EXPIRED',
      message: 'This pickup code has expired.',
    },
  ],
  [
    '4823',
    {
      status: 404,
      code: 'PICKUP_CODE_USED',
      message: 'This pickup code has already been used.',
    },
  ],
  [
    '4824',
    {
      status: 409,
      code: 'PARCEL_NOT_ON_SHELF',
      message: 'The parcel is not in its recorded location.',
    },
  ],
])

// handovers is a mock database table for handover records.
// key is the pickupCode, value is the Handover generated after the first confirmation.
const handovers = new Map()

// structuredClone will copy the object, avoiding pollution of the next mock return value after page modification.
function copy(value) {
  return structuredClone(value)
}

// simulate network delay, allowing the page to display verifying/loading states.
function wait(milliseconds = 200) {
  return new Promise((resolve) => setTimeout(resolve, milliseconds))
}

// generate an error in the same format as the frontend/src/api/http.js.
function makeError(status, code, message) {
  const error = new Error(message)
  error.status = status
  error.code = code
  error.requestId = null
  return error
}

// verify the pickup code.
// on success, return PickupVerifyResponse: pickupCode, parcelSummary, location.
export async function verifyPickupCode(pickupCode) {
  await wait()

  if (pickupCode === SUCCESS_PICKUP_CODE) {
    return {
      pickupCode,
      parcelSummary: copy(MOCK_PARCEL),
      location: copy(MOCK_LOCATION),
    }
  }

  // From the Map, read the failure scenario corresponding to this code.
  const failure = MOCK_ERRORS.get(pickupCode)
  if (failure) {
    throw makeError(failure.status, failure.code, failure.message)
  }

  // Any other value not in the demonstration table is considered an invalid code.
  throw makeError(404, 'PICKUP_CODE_INVALID', 'This code is not recognised.')
}

// confirm the handover.
// When confirming the same pickupCode multiple times, return the object created during the first confirmation, simulating the idempotency of NFR-4.
export async function confirmHandover(pickupCode, result = 'collected') {
  await wait()

  if (pickupCode !== SUCCESS_PICKUP_CODE) {
    throw makeError(404, 'PICKUP_CODE_INVALID', 'This pickup code cannot be confirmed.')
  }

  if (!ALLOWED_RESULTS.includes(result)) {
    throw makeError(400, 'VALIDATION_ERROR', 'The handover result is not valid.')
  }

  // First confirmation: create a unique handover.
  if (!handovers.has(pickupCode)) {
    handovers.set(pickupCode, {
      id: '33333333-3333-4333-8333-333333333333',
      parcelId: MOCK_PARCEL.id,
      result,
      createdAt: '2026-10-23T08:30:00Z',
    })
  }

  // Subsequent confirmations: retrieve the result created during the first confirmation and return a copy.
  return copy(handovers.get(pickupCode))
}
