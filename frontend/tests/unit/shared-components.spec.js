import { mount } from '@vue/test-utils'
import { describe, expect, it } from 'vitest'
import ConfirmDialog from '../../src/components/ConfirmDialog.vue'
import ErrorState from '../../src/components/ErrorState.vue'
import LoadingState from '../../src/components/LoadingState.vue'
import PageHeader from '../../src/components/PageHeader.vue'

// use stubs to replace the actual Element Plus Dialog and Button.
// this way, the tests only verify the emit and deduplication logic of ConfirmDialog.
const ElDialogStub = {
  props: ['modelValue'],
  template: `
    <section v-if="modelValue">
      <slot />
      <slot name="footer" />
    </section>
  `,
}

const ElButtonStub = {
  props: ['loading', 'disabled'],
  emits: ['click'],
  template: `
    <button
      :disabled="disabled || loading"
      @click="$emit('click')"
    >
      <slot />
    </button>
  `,
}

describe('shared frontend components', () => {
  it('PageHeader renders the title and description', () => {
    const wrapper = mount(PageHeader, {
      props: {
        title: 'Pickup verification',
        description: 'Verify a pickup code.',
      },
    })

    expect(wrapper.text()).toContain('Pickup verification')
    expect(wrapper.text()).toContain('Verify a pickup code.')
  })

  it('LoadingState exposes an accessible status', () => {
  const wrapper = mount(LoadingState, {
    props: {
      label: 'Verifying code',
    },
  })

  const status = wrapper.get('[role="status"]')

  expect(status.attributes('role')).toBe('status')
  expect(status.text()).toContain('Verifying code')
})

  it('ErrorState renders the message and emits retry', async () => {
    const wrapper = mount(ErrorState, {
      props: {
        error: {
          status: 500,
          code: 'REQUEST_FAILED',
          message: 'Could not verify the code.',
          requestId: 'request-123',
        },
      },
    })

    expect(wrapper.text()).toContain('Could not verify the code.')
    expect(wrapper.text()).toContain('request-123')

    await wrapper.get('button').trigger('click')
    expect(wrapper.emitted('retry')).toHaveLength(1)
  })

  it('ConfirmDialog does not emit while loading or twice for one click', async () => {
    const wrapper = mount(ConfirmDialog, {
      props: {
        modelValue: true,
        title: 'Confirm handover',
        message: 'This action cannot be undone.',
        confirmLabel: 'Confirm handover',
        loading: true,
      },
      global: {
        stubs: {
          ElDialog: ElDialogStub,
          ElButton: ElButtonStub,
        },
      },
    })

    // When loading=true, the second button is disabled and cannot emit confirm.
    const buttons = wrapper.findAll('button')
    await buttons[1].trigger('click')
    expect(wrapper.emitted('confirm')).toBeUndefined()

    // After the request completes, clicking again should emit a confirm.
    await wrapper.setProps({ loading: false })
    await wrapper.findAll('button')[1].trigger('click')
    expect(wrapper.emitted('confirm')).toHaveLength(1)
  })
})
