import {
  afterEach,
  beforeEach,
  describe,
  expect,
  it,
  vi,
} from 'vitest'
import { flushPromises, mount } from '@vue/test-utils'
import LocationBoardView from '../../src/views/LocationBoardView.vue'
import { fetchLocations } from '../../src/api/locations.js'

vi.mock('../../src/api/locations.js', () => ({
  fetchLocations: vi.fn(),
}))

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
    code: 'A-01-2',
    zone: 'A',
    size: 'large',
    status: 'reserved',
    parcelId: null,
  },
  {
    id: '10000000-0000-4000-8000-000000000003',
    code: 'B-01-1',
    zone: 'B',
    size: 'small',
    status: 'available',
    parcelId: null,
  },
  {
    id: '10000000-0000-4000-8000-000000000004',
    code: 'B-01-2',
    zone: 'B',
    size: 'small',
    status: 'disabled',
    parcelId: null,
  },
  {
    id: '10000000-0000-4000-8000-000000000005',
    code: 'B-01-3',
    zone: 'B',
    size: 'medium',
    status: 'occupied',
    parcelId: '20000000-0000-4000-8000-000000000002',
  },
]

let wrapper

beforeEach(() => {
  vi.clearAllMocks()
  fetchLocations.mockResolvedValue(locations)
})

afterEach(() => {
  wrapper?.unmount()
  wrapper = undefined
})

describe('LocationBoardView', () => {
  it('renders zones and all four location statuses', async () => {
    wrapper = mount(LocationBoardView)
    await flushPromises()

    expect(wrapper.text()).toContain('Zone A')
    expect(wrapper.text()).toContain('Zone B')
    expect(wrapper.text()).toContain('Available')
    expect(wrapper.text()).toContain('Reserved')
    expect(wrapper.text()).toContain('Occupied')
    expect(wrapper.text()).toContain('Disabled')
  })

  it('filters the board by zone', async () => {
    wrapper = mount(LocationBoardView)
    await flushPromises()

    await wrapper.get('[data-test="zone-filter"]').setValue('B')

    expect(wrapper.findAll('.zone-section')).toHaveLength(1)
    expect(wrapper.get('.zone-section').text()).toContain('Zone B')
    expect(wrapper.text()).toContain('3 locations')
  })

  it('shows a full-shelf warning for a zone without available slots', async () => {
    wrapper = mount(LocationBoardView)
    await flushPromises()

    expect(wrapper.text()).toContain(
      'No available slots in Zone A.',
    )
    expect(wrapper.text()).toContain(
      'Check another zone before starting intake.',
    )
  })

  it('shows the selected location details', async () => {
    wrapper = mount(LocationBoardView)
    await flushPromises()

    await wrapper.get('[data-test="location-B-01-1"]').trigger('click')

    expect(wrapper.text()).toContain('Selected location')
    expect(wrapper.text()).toContain('B-01-1')
    expect(wrapper.text()).toContain('Zone')
    expect(wrapper.text()).toContain('Small')
    expect(wrapper.text()).toContain('Available')
    expect(wrapper.text()).toContain(
      'Assigning a storage location to an existing registered parcel',
    )
  })

  it('clears the selected location when the zone filter changes', async () => {
    wrapper = mount(LocationBoardView)
    await flushPromises()

    await wrapper.get('[data-test="location-B-01-1"]').trigger('click')
    expect(wrapper.find('.selection-card').exists()).toBe(true)

    await wrapper.get('[data-test="zone-filter"]').setValue('A')

    expect(wrapper.find('.selection-card').exists()).toBe(false)
  })

  it('shows the empty state when no locations exist', async () => {
    fetchLocations.mockResolvedValueOnce([])

    wrapper = mount(LocationBoardView)
    await flushPromises()

    expect(wrapper.text()).toContain(
      'No storage locations configured',
    )
  })

  it('shows an error and retries the request', async () => {
    fetchLocations
      .mockRejectedValueOnce({
        status: 500,
        code: 'REQUEST_FAILED',
        message: 'Locations unavailable',
        requestId: 'req-locations',
      })
      .mockResolvedValueOnce(locations)

    wrapper = mount(LocationBoardView)
    await flushPromises()

    expect(wrapper.text()).toContain('Locations unavailable')
    expect(wrapper.text()).toContain('req-locations')

    await wrapper.get('button').trigger('click')
    await flushPromises()

    expect(fetchLocations).toHaveBeenCalledTimes(2)
    expect(wrapper.text()).toContain('Zone A')
    expect(wrapper.text()).not.toContain('Locations unavailable')
  })

  it('shows a loading state while the request is pending', async () => {
    let finishRequest

    fetchLocations.mockImplementationOnce(() => new Promise((resolve) => {
      finishRequest = resolve
    }))

    wrapper = mount(LocationBoardView)
    await flushPromises()

    expect(wrapper.text()).toContain('Loading storage locations')

    finishRequest(locations)
    await flushPromises()

    expect(wrapper.text()).toContain('Zone A')
  })
})
