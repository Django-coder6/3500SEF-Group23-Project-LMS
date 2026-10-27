// Temporary parcel intake mock for T-42-07.
// TODO(T-42-01): replace this with POST /parcels and POST /parcels/batch.

const CARRIERS = {
  SF: 'SF Express',
  YT: 'YTO Express',
  JD: 'JD Logistics',
  ZTO: 'ZTO Express',
}

const knownTrackingNumbers = new Set()

function wait(milliseconds = 200) {
  return new Promise((resolve) => setTimeout(resolve, milliseconds))
}

function makeError(status, code, message) {
  const error = new Error(message)
  error.status = status
  error.code = code
  error.requestId = null
  return error
}

export function carrierFromTrackingNo(trackingNo) {
  const normalised = String(trackingNo).trim().toUpperCase()
  const prefix = Object.keys(CARRIERS).find((candidate) => (
    normalised.startsWith(candidate)
  ))
  return CARRIERS[prefix] ?? ''
}

function createParcel(input, index = 0) {
  const now = new Date().toISOString()
  const suffix = String(index + 1).padStart(12, '0')

  return {
    id: `30000000-0000-4000-8000-${suffix}`,
    trackingNo: input.trackingNo,
    carrier: input.carrier,
    recipientName: input.recipientName,
    recipientPhone: input.recipientPhone,
    status: 'registered',
    size: input.size,
    locationId: input.locationId ?? null,
    expectedVersion: 1,
    createdAt: now,
    updatedAt: now,
  }
}

export async function registerParcel(input) {
  await wait()

  if (knownTrackingNumbers.has(input.trackingNo)) {
    throw makeError(
      409,
      'DUPLICATE_WAYBILL',
      'This waybill is already registered.',
    )
  }

  knownTrackingNumbers.add(input.trackingNo)
  return createParcel(input)
}

export async function registerParcelBatch(parcels) {
  await wait()

  if (parcels.length > 50) {
    throw makeError(
      400,
      'BATCH_LIMIT_EXCEEDED',
      'A batch can contain at most 50 parcels.',
    )
  }

  const created = []
  const rejected = []

  parcels.forEach((parcel, index) => {
    if (knownTrackingNumbers.has(parcel.trackingNo)) {
      rejected.push({
        index,
        trackingNo: parcel.trackingNo,
        reason: 'Waybill already registered.',
      })
      return
    }

    knownTrackingNumbers.add(parcel.trackingNo)
    created.push(createParcel(parcel, index))
  })

  return { created, rejected }
}
