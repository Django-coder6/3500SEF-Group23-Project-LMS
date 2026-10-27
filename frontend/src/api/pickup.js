// T-42-05: the single API boundary used by the pickup page.
// TODO(T-42-04): when the backend endpoints exist, replace this re-export
// with the real http.post('/pickup/verify') and http.post('/pickup/confirm')
// functions. The page imports this file only, so page code will not change.
export {
  verifyPickupCode,
  confirmHandover,
} from './mock/pickup'
