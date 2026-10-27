// T-42-06: the single API boundary used by the location board.
// TODO(T-42-03): replace this re-export with the real GET /locations call.
export { fetchLocations } from './mock/locations'