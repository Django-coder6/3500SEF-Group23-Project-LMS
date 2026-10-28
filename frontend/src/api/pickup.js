import http from './http'

/**
 * Verify an active pickup code before handover.
 *
 * Contract: POST /pickup/verify
 * Request: { pickupCode }
 * Success: { pickupCode, parcelSummary, location }
 */
export function verifyPickupCode(pickupCode) {
  return http.post('/pickup/verify', {
    pickupCode,
  })
}

/**
 * Confirm the handover after the pickup code has been verified.
 *
 * Contract: POST /pickup/confirm
 * The operator is taken from the authenticated session, not sent here.
 */
export function confirmHandover(pickupCode, result = 'collected') {
  return http.post('/pickup/confirm', {
    pickupCode,
    result,
  })
}