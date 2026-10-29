import http from './http'

/**
 * Load all storage locations for the current collection point.
 *
 * Contract: GET /locations
 * Success response: a bare Location array, without pagination or an envelope.
 */
export function fetchLocations() {
  return http.get('/locations')
}