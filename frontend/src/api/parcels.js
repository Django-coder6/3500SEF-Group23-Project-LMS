import http from './http'

const CARRIERS = {
  SF: 'SF Express',
  YT: 'YTO Express',
  JD: 'JD Logistics',
  ZTO: 'ZTO Express',
}

/**
 * Derive the display carrier from the tracking-number prefix.
 *
 * This is local presentation logic and does not call the backend.
 */
export function carrierFromTrackingNo(trackingNo) {
  const normalised = String(trackingNo).trim().toUpperCase()
  const prefix = Object.keys(CARRIERS).find((candidate) => (
    normalised.startsWith(candidate)
  ))

  return CARRIERS[prefix] ?? ''
}

/**
 * Register one incoming parcel.
 *
 * Contract: POST /parcels
 * Success: Parcel
 */
export function registerParcel(input) {
  return http.post('/parcels', input)
}

/**
 * Register a batch of incoming parcels.
 *
 * The page passes an array, while the API contract expects { parcels }.
 * The adapter performs that translation so page code does not need to know
 * the HTTP request shape.
 */
export function registerParcelBatch(parcels) {
  return http.post('/parcels/batch', {
    parcels,
  })
}