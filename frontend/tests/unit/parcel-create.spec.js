import {
  afterEach,
  beforeEach,
  describe,
  expect,
  it,
  vi,
} from 'vitest'
import { flushPromises, mount } from '@vue/test-utils'
import ParcelCreateView from '../../src/views/ParcelCreateView.vue'
import { fetchLocations } from '../../src/api/locations.js'
import {
  registerParcel,
  registerParcelBatch,
} from '../../src/api/parcels.js'

vi.mock('../../src/api/locations.js', () => ({
  fetchLocations: vi.fn(),
}))

vi.mock('../../src/api/parcels.js', async (importOriginal) => {
  const actual = await importOriginal()

  return {
    ...actual,
    registerParcel: vi.fn(),
    registerParcelBatch: vi.fn(),
  }
})

const locations = [
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
    code: 'B-01-1',
    zone: 'B',
    size: 'small',
    status: 'available',
    parcelId: null,
  },
  {
    id: '10000000-0000-4000-8000-000000000003',
    code: 'C-01-1',
    zone: 'C',
    size: 'medium',
    status: 'available',
    parcelId: null,
  },
]

const createdParcel = {
  id: '30000000-0000-4000-8000-000000000001',
  trackingNo: 'SF1234567890',
  carrier: 'SF Express',
  recipientName: 'Alex Chan',
  recipientPhone: '13800000000',
  status: 'registered',
  size: 'medium',
  locationId: '10000000-0000-4000-8000-000000000003',
  expectedVersion: 1,
  createdAt: '2026-10-25T00:00:00Z',
  updatedAt: '2026-10-25T00:00:00Z',
}

let wrapper

async function mountView() {
  wrapper = mount(ParcelCreateView)
  await flushPromises()
  return wrapper
}

async function fillSingle(view) {
  await view.get('input[name="trackingNo"]').setValue('SF1234567890')
  await view.get('input[name="recipientName"]').setValue('Alex Chan')
  await view.get('input[name="recipientPhone"]').setValue('13800000000')
  await view.get('select[name="locationId"]')
    .setValue('10000000-0000-4000-8000-000000000003')
}

async function switchToBatch(view) {
  await view.findAll('.mode-button')[1].trigger('click')
}

beforeEach(() => {
  vi.clearAllMocks()
  fetchLocations.mockResolvedValue(locations)
  registerParcel.mockResolvedValue(createdParcel)
  registerParcelBatch.mockResolvedValue({
    created: [createdParcel],
    rejected: [],
  })
})

afterEach(() => {
  wrapper?.unmount()
  wrapper = undefined
})

describe('ParcelCreateView', () => {
  it('derives the carrier from the waybill prefix', async () => {
    const view = await mountView()

    await view.get('input[name="trackingNo"]').setValue('SF1234567890')

    expect(view.get('#carrier').element.value).toBe('SF Express')
  })

  it('shows only available locations for the selected size', async () => {
    const view = await mountView()

    const options = view.findAll('select[name="locationId"] option')

    expect(options).toHaveLength(2)
    expect(options[1].text()).toContain('C-01-1')
    expect(options.map((option) => option.text()).join(' '))
      .not.toContain('A-01-1')
  })

  it('registers a valid single parcel', async () => {
    const view = await mountView()
    await fillSingle(view)

    await view.get('[data-test="single-form"]').trigger('submit')
    await flushPromises()

    expect(registerParcel).toHaveBeenCalledWith({
      trackingNo: 'SF1234567890',
      carrier: 'SF Express',
      recipientName: 'Alex Chan',
      recipientPhone: '13800000000',
      size: 'medium',
      locationId: '10000000-0000-4000-8000-000000000003',
    })
    expect(view.text()).toContain('SF1234567890 registered at C-01-1')
  })

  it('shows a duplicate waybill as a field-level error', async () => {
    registerParcel.mockRejectedValueOnce({
      status: 409,
      code: 'DUPLICATE_TRACKING_NO',
      message: 'This waybill is already registered.',
      requestId: 'req-duplicate',
    })

    const view = await mountView()
    await fillSingle(view)
    await view.get('[data-test="single-form"]').trigger('submit')
    await flushPromises()

    expect(view.get('.field-error').text()).toBe(
      'This waybill is already registered.',
    )
    expect(view.get('input[name="trackingNo"]').attributes('aria-invalid'))
      .toBe('true')
  })

  it('rejects more than 50 batch rows before calling the API', async () => {
    const view = await mountView()
    await switchToBatch(view)

    const line = 'SF1234567890, medium, Alex Chan, 13800000000'
    await view.get('textarea').setValue(
      Array.from({ length: 52 }, () => line).join('\n'),
    )

    expect(view.text()).toContain(
      'Batch limit is 50 parcels. You entered 52',
    )
    expect(view.text()).toContain('which is 2 over the limit')

    await view.get('[data-test="batch-form"]').trigger('submit')
    expect(registerParcelBatch).not.toHaveBeenCalled()
  })

  it('shows created and rejected batch results separately', async () => {
    registerParcelBatch.mockResolvedValueOnce({
      created: [createdParcel],
      rejected: [{
        index: 1,
        trackingNo: 'SF1234567999',
        reason: 'Waybill already registered.',
      }],
    })

    const view = await mountView()
    await switchToBatch(view)
    await view.get('textarea').setValue([
      'SF1234567890, medium, Alex Chan, 13800000000',
      'SF1234567999, small, Bea Wong, 13900000000',
    ].join('\n'))

    await view.get('[data-test="batch-form"]').trigger('submit')
    await flushPromises()

    expect(view.text()).toContain('Created: 1')
    expect(view.text()).toContain('Rejected: 1')
    expect(view.text()).toContain(
      'SF1234567999: Waybill already registered.',
    )
  })

  it('does not expose deferred recommendation or shelve-later UI', async () => {
    const view = await mountView()

    expect(view.text()).not.toContain('Suggested storage location')
    expect(view.text()).not.toContain('shelve later')
    expect(view.text()).not.toContain('recommendation panel')
  })
})
