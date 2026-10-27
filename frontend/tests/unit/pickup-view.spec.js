import { flushPromises, mount } from '@vue/test-utils'
import { nextTick } from 'vue'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { confirmHandover, verifyPickupCode } from '../../src/api/pickup.js'
import PickupVerifyView from '../../src/views/PickupVerifyView.vue'

vi.mock('../../src/api/pickup.js', () => ({
  verifyPickupCode: vi.fn(),
  confirmHandover: vi.fn(),
}))

vi.mock('../../src/components/ConfirmDialog.vue', () => ({
  default: {
    props: ['modelValue', 'title', 'message', 'confirmLabel', 'loading', 'danger'],
    emits: ['update:modelValue', 'confirm'],
    template: `
      <section v-if="modelValue" data-test="confirm-dialog">
        <button
          data-test="dialog-confirm"
          :disabled="loading"
          @click="$emit('confirm')"
        >
          {{ confirmLabel }}
        </button>
      </section>
    `,
  },
}))

const verifiedResult = {
  pickupCode: '4821',
  parcelSummary: {
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
  },
  location: {
    id: '22222222-2222-4222-8222-222222222222',
    code: 'A-03-2',
    zone: 'A',
    size: 'medium',
    status: 'occupied',
    parcelId: '11111111-1111-4111-8111-111111111111',
  },
}

const completedHandover = {
  id: '33333333-3333-4333-8333-333333333333',
  parcelId: verifiedResult.parcelSummary.id,
  result: 'collected',
  createdAt: '2026-10-23T08:30:00Z',
}

function mountView() {
  return mount(PickupVerifyView)
}

beforeEach(() => {
  vi.clearAllMocks()
  verifyPickupCode.mockResolvedValue(verifiedResult)
  confirmHandover.mockResolvedValue(completedHandover)
})

describe('PickupVerifyView', () => {
  it('starts idle with Verify disabled', () => {
    const wrapper = mountView()

    expect(wrapper.text()).toContain('Pickup verification')
    expect(wrapper.get('[data-test="verify"]').attributes('disabled')).toBeDefined()
  })

  it('focuses the pickup input for scanner input', async () => {
    const wrapper = mount(PickupVerifyView, {
      attachTo: document.body,
    })
    await nextTick()

    expect(document.activeElement).toBe(wrapper.get('#pickup-code').element)
    wrapper.unmount()
  })

  it('verifies on Enter and shows the shelf location prominently', async () => {
    const wrapper = mountView()

    await wrapper.get('#pickup-code').setValue('4821')
    await wrapper.get('#pickup-code').trigger('keyup.enter')
    await flushPromises()

    expect(verifyPickupCode).toHaveBeenCalledWith('4821')
    expect(wrapper.text()).toContain('SF1234567890')
    expect(wrapper.get('.shelf-code').text()).toBe('A-03-2')
    expect(wrapper.get('#pickup-code').attributes('disabled')).toBeDefined()
  })

  it('locks the field and button while verification is in progress', async () => {
    let finishVerification
    verifyPickupCode.mockImplementation(() => new Promise((resolve) => {
      finishVerification = resolve
    }))

    const wrapper = mountView()
    await wrapper.get('#pickup-code').setValue('4821')
    await wrapper.get('form').trigger('submit')

    expect(wrapper.get('#pickup-code').attributes('disabled')).toBeDefined()
    expect(wrapper.get('[data-test="verify"]').text()).toContain('Verifying')
    expect(wrapper.get('[data-test="verify"]').attributes('disabled')).toBeDefined()

    finishVerification(verifiedResult)
    await flushPromises()
  })

  it.each([
    ['PICKUP_CODE_INVALID', 'Pickup code not recognised'],
    ['PICKUP_CODE_EXPIRED', 'Pickup code expired'],
    ['PICKUP_CODE_USED', 'Pickup code already used'],
    ['PARCEL_NOT_ON_SHELF', 'Parcel not at the recorded shelf'],
  ])('shows the recoverable state for %s', async (code, title) => {
    verifyPickupCode.mockRejectedValue({
      status: 404,
      code,
      message: `State ${code}`,
      requestId: null,
    })

    const wrapper = mountView()
    await wrapper.get('#pickup-code').setValue('9999')
    await wrapper.get('form').trigger('submit')
    await flushPromises()

    expect(wrapper.text()).toContain(title)
    expect(wrapper.text()).toContain(`State ${code}`)
  })

  it('prevents a double handover while confirmation is in flight', async () => {
    let finishHandover
    confirmHandover.mockImplementation(() => new Promise((resolve) => {
      finishHandover = resolve
    }))

    const wrapper = mountView()
    await wrapper.get('#pickup-code').setValue('4821')
    await wrapper.get('form').trigger('submit')
    await flushPromises()
    await wrapper.get('[data-test="open-confirm"]').trigger('click')
    await wrapper.get('[data-test="dialog-confirm"]').trigger('click')
    await wrapper.get('[data-test="dialog-confirm"]').trigger('click')

    expect(confirmHandover).toHaveBeenCalledTimes(1)

    finishHandover(completedHandover)
    await flushPromises()

    expect(wrapper.text()).toContain('Handover recorded')
    expect(wrapper.text()).toContain('The handover was recorded successfully.')
  })
})
