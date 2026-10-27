// Temporary locations mock for T-42-06.
// TODO(T-42-03): replace this with GET /locations when the backend task lands.

const LOCATIONS = [
  {
    id: '10000000-0000-4000-8000-000000000001',
    code: 'A-01-1',
    zone: 'A',
    size: 'large',
    status: 'occupied',
    parcelId: '20000000-0000-4000-8000-000000000001',
  },
  {
    id: '10000000-0000-4000-8000-000000000002',
    code: 'A-01-2',
    zone: 'A',
    size: 'large',
    status: 'occupied',
    parcelId: '20000000-0000-4000-8000-000000000002',
  },
  {
    id: '10000000-0000-4000-8000-000000000003',
    code: 'A-02-1',
    zone: 'A',
    size: 'medium',
    status: 'reserved',
    parcelId: null,
  },
  {
    id: '10000000-0000-4000-8000-000000000004',
    code: 'A-02-2',
    zone: 'A',
    size: 'medium',
    status: 'disabled',
    parcelId: null,
  },
  {
    id: '10000000-0000-4000-8000-000000000005',
    code: 'B-01-1',
    zone: 'B',
    size: 'small',
    status: 'available',
    parcelId: null,
  },
  {
    id: '10000000-0000-4000-8000-000000000006',
    code: 'B-01-2',
    zone: 'B',
    size: 'small',
    status: 'occupied',
    parcelId: '20000000-0000-4000-8000-000000000003',
  },
  {
    id: '10000000-0000-4000-8000-000000000007',
    code: 'B-02-1',
    zone: 'B',
    size: 'large',
    status: 'available',
    parcelId: null,
  },
  {
    id: '10000000-0000-4000-8000-000000000008',
    code: 'C-01-1',
    zone: 'C',
    size: 'medium',
    status: 'available',
    parcelId: null,
  },
  {
    id: '10000000-0000-4000-8000-000000000009',
    code: 'C-01-2',
    zone: 'C',
    size: 'medium',
    status: 'available',
    parcelId: null,
  },
  {
    id: '10000000-0000-4000-8000-000000000010',
    code: 'C-02-1',
    zone: 'C',
    size: 'large',
    status: 'reserved',
    parcelId: null,
  },
]

function wait(milliseconds = 200) {
  return new Promise((resolve) => setTimeout(resolve, milliseconds))
}

function copy(value) {
  return structuredClone(value)
}

export async function fetchLocations() {
  await wait()
  return copy(LOCATIONS)
}