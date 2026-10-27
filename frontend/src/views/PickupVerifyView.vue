<script setup>
import { computed, nextTick, onMounted, ref } from 'vue'
import { confirmHandover, verifyPickupCode } from '../api/pickup'
import ConfirmDialog from '../components/ConfirmDialog.vue'
import ErrorState from '../components/ErrorState.vue'
import LoadingState from '../components/LoadingState.vue'
import PageHeader from '../components/PageHeader.vue'

// T-42-05: staff pickup verification and handover.
// The API adapter currently points at the mock until T-42-04 lands.
const pickupCode = ref('')
const pickupInput = ref(null)
const viewState = ref('idle')
const result = ref(null)
const handover = ref(null)
const operationError = ref(null)
const confirmError = ref(null)
const confirmOpen = ref(false)
const confirmBusy = ref(false)

const busy = computed(() => viewState.value === 'verifying')
const hasVerifiedResult = computed(() => (
  viewState.value === 'success' || viewState.value === 'completed'
))
const inputLocked = computed(() => (
  busy.value
  || confirmBusy.value
  || ['success', 'completed', 'not-on-shelf'].includes(viewState.value)
))
const canVerify = computed(() => (
  pickupCode.value.trim().length > 0 && !inputLocked.value
))
const confirmMessage = computed(() => {
  if (!result.value) {
    return 'Confirm this handover?'
  }

  return `Confirm handover for parcel ${result.value.parcelSummary.trackingNo} at ${result.value.location.code}?`
})

const noticeTitles = {
  invalid: 'Pickup code not recognised',
  expired: 'Pickup code expired',
  used: 'Pickup code already used',
  'not-on-shelf': 'Parcel not at the recorded shelf',
}

// A route-mounted input does not reliably honour the HTML autofocus attribute,
// so explicitly focus it after Vue has rendered the page for scanner input.
onMounted(async () => {
  await nextTick()
  pickupInput.value?.focus()
})

function normaliseError(error) {
  return {
    status: error?.status ?? 0,
    code: error?.code ?? 'REQUEST_FAILED',
    message: error?.message ?? 'The request could not be completed.',
    requestId: error?.requestId ?? null,
  }
}

async function verifyCode() {
  if (!canVerify.value) {
    return
  }

  const code = pickupCode.value.trim()
  pickupCode.value = code
  result.value = null
  handover.value = null
  operationError.value = null
  confirmError.value = null
  viewState.value = 'verifying'

  try {
    result.value = await verifyPickupCode(code)
    viewState.value = 'success'
  } catch (error) {
    operationError.value = normaliseError(error)

    if (error?.code === 'PICKUP_CODE_EXPIRED') {
      viewState.value = 'expired'
    } else if (error?.code === 'PICKUP_CODE_USED') {
      viewState.value = 'used'
    } else if (error?.code === 'PARCEL_NOT_ON_SHELF') {
      viewState.value = 'not-on-shelf'
    } else if (error?.code === 'PICKUP_CODE_INVALID') {
      viewState.value = 'invalid'
    } else {
      viewState.value = 'error'
    }
  }
}

function openConfirmation() {
  if (viewState.value === 'success' && !confirmBusy.value) {
    confirmError.value = null
    confirmOpen.value = true
  }
}

async function submitHandover() {
  if (confirmBusy.value || !result.value) {
    return
  }

  confirmBusy.value = true
  confirmError.value = null

  try {
    handover.value = await confirmHandover(result.value.pickupCode, 'collected')
    confirmOpen.value = false
    viewState.value = 'completed'
  } catch (error) {
    confirmOpen.value = false
    confirmError.value = normaliseError(error)
  } finally {
    confirmBusy.value = false
  }
}

async function startAnotherVerification() {
  pickupCode.value = ''
  viewState.value = 'idle'
  result.value = null
  handover.value = null
  operationError.value = null
  confirmError.value = null
  confirmOpen.value = false
  await nextTick()
  pickupInput.value?.focus()
}
</script>

<template>
  <section class="pickup-page">
    <PageHeader
      title="Pickup verification"
      description="Verify a pickup code, check the shelf location, and confirm the handover."
    />

    <form class="verify-panel" @submit.prevent="verifyCode">
      <label class="field-label" for="pickup-code">Pickup code</label>
      <div class="code-row">
        <input
          id="pickup-code"
          ref="pickupInput"
          v-model="pickupCode"
          class="code-input"
          name="pickupCode"
          type="text"
          inputmode="numeric"
          autocomplete="off"
          autofocus
          :disabled="inputLocked"
          aria-describedby="pickup-code-hint"
          @keyup.enter.prevent="verifyCode"
        >

        <button
          type="submit"
          class="primary-button"
          data-test="verify"
          :disabled="!canVerify"
        >
          {{ busy ? 'Verifying...' : 'Verify' }}
        </button>
      </div>
      <p id="pickup-code-hint" class="field-hint">
        Scan or type the code. Press Enter to verify it immediately.
      </p>
    </form>

    <div class="state-region" aria-live="polite">
      <LoadingState
        v-if="busy"
        label="Verifying pickup code"
      />

      <ErrorState
        v-else-if="viewState === 'error'"
        :error="operationError"
        @retry="verifyCode"
      />

      <section
        v-else-if="hasVerifiedResult"
        class="result-card"
      >
        <p class="eyebrow">
          {{ viewState === 'completed' ? 'Handover recorded' : 'Verified parcel' }}
        </p>
        <h2 class="parcel-number">{{ result.parcelSummary.trackingNo }}</h2>

        <dl class="fact-list">
          <div>
            <dt>Recipient type</dt>
            <dd>resident</dd>
          </div>
          <div>
            <dt>Parcel status</dt>
            <dd>{{ result.parcelSummary.status }}</dd>
          </div>
          <div>
            <dt>Carrier</dt>
            <dd>{{ result.parcelSummary.carrier }}</dd>
          </div>
        </dl>

        <div class="shelf-card">
          <span class="shelf-label">Storage location</span>
          <strong class="shelf-code">{{ result.location.code }}</strong>
          <span class="shelf-zone">Zone {{ result.location.zone }}</span>
        </div>

        <p v-if="handover" class="handover-note">
          The handover was recorded successfully.
        </p>

        <div
          v-if="confirmError"
          class="inline-error"
          role="alert"
        >
          <p>{{ confirmError.message }}</p>
          <button
            type="button"
            class="secondary-button"
            @click="openConfirmation"
          >
            Retry confirmation
          </button>
        </div>

        <div class="action-row">
          <button
            v-if="viewState === 'success'"
            type="button"
            class="primary-button"
            data-test="open-confirm"
            :disabled="confirmBusy"
            @click="openConfirmation"
          >
            {{ confirmBusy ? 'Confirming...' : 'Confirm handover' }}
          </button>
          <button
            type="button"
            class="secondary-button"
            data-test="start-another"
            :disabled="confirmBusy"
            @click="startAnotherVerification"
          >
            Verify another code
          </button>
        </div>
      </section>

      <section
        v-else-if="viewState === 'not-on-shelf'"
        class="notice-card"
        role="alert"
      >
        <h2>{{ noticeTitles[viewState] }}</h2>
        <p>{{ operationError.message }}</p>
        <p>
          Re-check the shelf shown on the parcel label or the location board
          before using an exception path. Do not confirm this handover until
          the parcel is found.
        </p>
        <button
          type="button"
          class="secondary-button"
          data-test="start-another"
          @click="startAnotherVerification"
        >
          Try another code
        </button>
      </section>

      <section
        v-else-if="['invalid', 'expired', 'used'].includes(viewState)"
        class="notice-card"
        role="alert"
      >
        <h2>{{ noticeTitles[viewState] }}</h2>
        <p>{{ operationError.message }}</p>
        <button
          type="button"
          class="secondary-button"
          data-test="start-another"
          @click="startAnotherVerification"
        >
          Try another code
        </button>
      </section>
    </div>

    <ConfirmDialog
      v-model="confirmOpen"
      title="Confirm handover"
      :message="confirmMessage"
      confirm-label="Confirm handover"
      :loading="confirmBusy"
      :danger="true"
      @confirm="submitHandover"
    />
  </section>
</template>

<style scoped>
.pickup-page {
  display: grid;
  gap: var(--space-6);
}

.verify-panel,
.result-card,
.notice-card {
  padding: var(--space-4);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-surface);
}

.field-label {
  display: block;
  margin-bottom: var(--space-2);
  color: var(--color-text);
  font: var(--text-title);
}

.code-row {
  display: flex;
  align-items: stretch;
  gap: var(--space-2);
}

.code-input {
  min-width: 0;
  min-height: 44px;
  flex: 1;
  padding: var(--space-2) var(--space-3);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  background: var(--color-field);
  color: var(--color-text);
  font: var(--text-title);
}

.code-input:focus-visible,
.primary-button:focus-visible,
.secondary-button:focus-visible {
  outline: 2px solid var(--color-focus);
  outline-offset: 2px;
}

.code-input:disabled {
  opacity: 0.65;
}

.field-hint {
  margin: var(--space-2) 0 0;
  color: var(--color-text-muted);
  font: var(--text-caption);
}

.primary-button,
.secondary-button {
  min-height: 44px;
  padding: var(--space-2) var(--space-3);
  border-radius: var(--radius-sm);
  font: var(--text-body);
  cursor: pointer;
}

.primary-button {
  border: 1px solid var(--color-primary);
  background: var(--color-primary);
  color: var(--color-on-primary);
}

.secondary-button {
  border: 1px solid var(--color-border);
  background: var(--color-surface-soft);
  color: var(--color-text);
}

.primary-button:disabled,
.secondary-button:disabled {
  opacity: 0.6;
  cursor: default;
}

.eyebrow {
  margin: 0 0 var(--space-2);
  color: var(--color-text-muted);
  font: var(--text-caption);
  text-transform: uppercase;
}

.parcel-number {
  margin: 0 0 var(--space-4);
  font: var(--text-title-lg);
}

.fact-list {
  display: grid;
  gap: var(--space-3);
  margin: 0 0 var(--space-4);
}

.fact-list div {
  display: flex;
  justify-content: space-between;
  gap: var(--space-4);
  border-bottom: 1px solid var(--color-border);
  padding-bottom: var(--space-2);
}

.fact-list dt {
  color: var(--color-text-muted);
  font: var(--text-body);
}

.fact-list dd {
  margin: 0;
  color: var(--color-text);
  font: var(--text-body);
}

.shelf-card {
  display: grid;
  gap: var(--space-2);
  margin-bottom: var(--space-4);
  padding: var(--space-3);
  border-radius: var(--radius-md);
  background: var(--color-surface-soft);
}

.shelf-label,
.shelf-zone {
  color: var(--color-text-muted);
  font: var(--text-caption);
}

.shelf-code {
  color: var(--color-text);
  font: var(--text-display);
}

.handover-note {
  margin: 0 0 var(--space-4);
  color: var(--color-success);
  font: var(--text-body);
}

.inline-error {
  margin-bottom: var(--space-4);
  padding: var(--space-3);
  border: 1px solid var(--color-danger);
  border-radius: var(--radius-sm);
}

.inline-error p {
  margin: 0 0 var(--space-2);
  color: var(--color-text);
  font: var(--text-body);
}

.action-row {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
}

.notice-card h2 {
  margin: 0 0 var(--space-2);
  color: var(--color-danger);
  font: var(--text-title);
}

.notice-card p {
  margin: 0 0 var(--space-3);
  color: var(--color-text);
  font: var(--text-body);
}

@media (max-width: 640px) {
  .code-row {
    flex-direction: column;
  }
}
</style>
