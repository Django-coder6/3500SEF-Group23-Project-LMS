<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { fetchLocations } from '../api/locations'
import {
  carrierFromTrackingNo,
  registerParcel,
  registerParcelBatch,
} from '../api/parcels'
import ErrorState from '../components/ErrorState.vue'
import PageHeader from '../components/PageHeader.vue'

const mode = ref('single')
const busy = ref(false)
const locations = ref([])
const locationError = ref(null)
const submitError = ref(null)
const successMessage = ref('')
const batchResult = ref(null)

const fieldErrors = reactive({
  trackingNo: '',
})

const single = reactive({
  trackingNo: '',
  recipientName: '',
  recipientPhone: '',
  size: 'medium',
  locationId: '',
})

const batchText = ref('')

const carrier = computed(() => (
  carrierFromTrackingNo(single.trackingNo)
))

const availableLocations = computed(() => (
  locations.value.filter((location) => (
    location.status === 'available'
    && location.size === single.size
  ))
))

const noLocationAvailable = computed(() => (
  single.size !== ''
  && availableLocations.value.length === 0
))

const batchLines = computed(() => (
  batchText.value
    .split(/\r?\n/)
    .map((line) => line.trim())
    .filter(Boolean)
))

const batchCount = computed(() => batchLines.value.length)

const batchLimitExceeded = computed(() => batchCount.value > 50)

const batchOverLimitBy = computed(() => (
  Math.max(0, batchCount.value - 50)
))

const batchPreview = computed(() => (
  batchLines.value.map((line, index) => {
    const [
      trackingNo = '',
      size = '',
      recipientName = '',
      recipientPhone = '',
    ] = line.split(',').map((part) => part.trim())

    return {
      index,
      trackingNo,
      size,
      recipientName,
      recipientPhone,
      carrier: carrierFromTrackingNo(trackingNo),
    }
  })
))

const batchInputValid = computed(() => (
  batchCount.value > 0
  && !batchLimitExceeded.value
  && batchPreview.value.every((row) => (
    row.trackingNo
    && ['small', 'medium', 'large'].includes(row.size)
    && row.carrier
    && row.recipientName
    && row.recipientPhone
  ))
))

const singleValid = computed(() => Boolean(
  single.trackingNo.trim()
  && carrier.value
  && single.recipientName.trim()
  && single.recipientPhone.trim()
  && single.size
  && single.locationId
))

watch(() => single.size, () => {
  single.locationId = ''
})

watch(() => single.trackingNo, () => {
  fieldErrors.trackingNo = ''
})

function normaliseError(value) {
  return {
    status: value?.status ?? 0,
    code: value?.code ?? 'REQUEST_FAILED',
    message: value?.message ?? 'The request could not be completed.',
    requestId: value?.requestId ?? null,
  }
}

async function loadLocations() {
  locationError.value = null

  try {
    locations.value = await fetchLocations()
  } catch (value) {
    locationError.value = normaliseError(value)
  }
}

function resetMessages() {
  submitError.value = null
  successMessage.value = ''
  batchResult.value = null
}

async function submitSingle() {
  if (busy.value || !singleValid.value) {
    return
  }

  resetMessages()
  fieldErrors.trackingNo = ''
  busy.value = true

  try {
    const parcel = await registerParcel({
      trackingNo: single.trackingNo.trim(),
      carrier: carrier.value,
      recipientName: single.recipientName.trim(),
      recipientPhone: single.recipientPhone.trim(),
      size: single.size,
      locationId: single.locationId,
    })

    const locationCode = availableLocations.value.find(
      (location) => location.id === parcel.locationId,
    )?.code

    successMessage.value = `${parcel.trackingNo} registered at ${
      locationCode ?? 'the selected location'
    }.`

    single.trackingNo = ''
    single.recipientName = ''
    single.recipientPhone = ''
    single.locationId = ''
  } catch (value) {
    if (value?.code === 'DUPLICATE_TRACKING_NO') {
      fieldErrors.trackingNo = value.message
    } else {
      submitError.value = normaliseError(value)
    }
  } finally {
    busy.value = false
  }
}

async function submitBatch() {
  if (busy.value || batchLimitExceeded.value) {
    return
  }

  if (!batchInputValid.value) {
    submitError.value = {
      status: 400,
      code: 'VALIDATION_ERROR',
      message: 'Each batch row must contain a waybill, size, recipient name and recipient phone.',
      requestId: null,
    }
    return
  }

  resetMessages()
  busy.value = true

  try {
    batchResult.value = await registerParcelBatch(
      batchPreview.value.map((row) => ({
        trackingNo: row.trackingNo,
        carrier: row.carrier,
        recipientName: row.recipientName,
        recipientPhone: row.recipientPhone,
        size: row.size,
      })),
    )
  } catch (value) {
    submitError.value = normaliseError(value)
  } finally {
    busy.value = false
  }
}

onMounted(loadLocations)
</script>

<template>
  <section class="intake-page">
    <PageHeader
      title="Register a parcel"
      description="Record a single parcel or a courier drop of up to 50 parcels."
    />

    <section class="mode-panel" aria-label="Intake mode">
      <p class="mode-indicator">
        Current mode:
        <strong>
          {{ mode === 'single' ? 'Single parcel' : 'Batch intake' }}
        </strong>
      </p>

      <div class="mode-buttons">
        <button
          type="button"
          class="mode-button"
          :class="{ active: mode === 'single' }"
          :aria-pressed="mode === 'single'"
          @click="mode = 'single'"
        >
          Single
        </button>

        <button
          type="button"
          class="mode-button"
          :class="{ active: mode === 'batch' }"
          :aria-pressed="mode === 'batch'"
          @click="mode = 'batch'"
        >
          Batch
        </button>
      </div>
    </section>

    <ErrorState
      v-if="locationError"
      :error="locationError"
      @retry="loadLocations"
    />

    <form
      v-else-if="mode === 'single'"
      class="intake-card"
      data-test="single-form"
      @submit.prevent="submitSingle"
    >
      <div class="field">
        <label for="tracking-no">Waybill number</label>
        <input
          id="tracking-no"
          v-model="single.trackingNo"
          name="trackingNo"
          autocomplete="off"
          required
          :aria-invalid="Boolean(fieldErrors.trackingNo)"
          :disabled="busy"
        />
        <p
          v-if="fieldErrors.trackingNo"
          class="field-error"
          role="alert"
        >
          {{ fieldErrors.trackingNo }}
        </p>
      </div>

      <div class="field">
        <label for="carrier">Carrier</label>
        <input
          id="carrier"
          :value="carrier || 'Unknown prefix'"
          readonly
        />
      </div>

      <div class="field">
        <label for="recipient-name">Recipient name</label>
        <input
          id="recipient-name"
          v-model="single.recipientName"
          name="recipientName"
          autocomplete="name"
          required
          :disabled="busy"
        />
      </div>

      <div class="field">
        <label for="recipient-phone">Recipient phone</label>
        <input
          id="recipient-phone"
          v-model="single.recipientPhone"
          name="recipientPhone"
          autocomplete="tel"
          required
          :disabled="busy"
        />
      </div>

      <fieldset class="field">
        <legend>Parcel size</legend>
        <div class="size-options">
          <label
            v-for="size in ['small', 'medium', 'large']"
            :key="size"
          >
            <input
              v-model="single.size"
              type="radio"
              name="size"
              :value="size"
              :disabled="busy"
            />
            {{ size[0].toUpperCase() + size.slice(1) }}
          </label>
        </div>
      </fieldset>

      <div class="field">
        <label for="location-id">Storage location</label>
        <select
          id="location-id"
          v-model="single.locationId"
          name="locationId"
          required
          :disabled="busy || availableLocations.length === 0"
        >
          <option value="">Select an available location</option>
          <option
            v-for="location in availableLocations"
            :key="location.id"
            :value="location.id"
          >
            Zone {{ location.zone }} · {{ location.code }}
          </option>
        </select>
      </div>

      <p
        v-if="noLocationAvailable"
        class="full-shelf-warning"
        role="alert"
      >
        No available {{ single.size }} location. Check another zone before
        starting intake.
      </p>

      <button
        type="submit"
        class="primary-button"
        :disabled="busy || !singleValid || noLocationAvailable"
      >
        {{ busy ? 'Registering...' : 'Register parcel' }}
      </button>
    </form>

    <form
      v-else
      class="intake-card"
      data-test="batch-form"
      @submit.prevent="submitBatch"
    >
      <div class="field">
        <label for="batch-input">Courier drop</label>
        <p class="field-hint">
          One parcel per line: waybill, size, recipient name, recipient phone.
        </p>
        <textarea
          id="batch-input"
          v-model="batchText"
          rows="8"
          :disabled="busy"
          placeholder="SF1234567890, medium, Alex Chan, 13800000000"
        />
      </div>

      <p class="batch-count">
        {{ batchCount }} of 50 parcels
      </p>

      <p
        v-if="batchLimitExceeded"
        class="full-shelf-warning"
        role="alert"
      >
        Batch limit is 50 parcels. You entered {{ batchCount }}, which is
        {{ batchOverLimitBy }} over the limit. Split this courier drop and retry.
      </p>

      <button
        type="submit"
        class="primary-button"
        :disabled="busy || batchLimitExceeded || batchCount === 0"
      >
        {{ busy ? 'Registering...' : `Register ${batchCount} parcels` }}
      </button>
    </form>

    <ErrorState
      v-if="submitError"
      :error="submitError"
    />

    <p
      v-if="successMessage"
      class="success-message"
      role="status"
    >
      {{ successMessage }}
    </p>

    <section
      v-if="batchResult"
      class="batch-results"
      aria-labelledby="batch-results-title"
    >
      <h2 id="batch-results-title">Batch result</h2>

      <div class="result-columns">
        <section>
          <h3>Created: {{ batchResult.created.length }}</h3>
          <ul v-if="batchResult.created.length">
            <li
              v-for="parcel in batchResult.created"
              :key="parcel.id"
            >
              {{ parcel.trackingNo }}
            </li>
          </ul>
          <p v-else>No parcels were created.</p>
        </section>

        <section>
          <h3>Rejected: {{ batchResult.rejected.length }}</h3>
          <ul v-if="batchResult.rejected.length">
            <li
              v-for="item in batchResult.rejected"
              :key="`${item.index}-${item.trackingNo}`"
            >
              {{ item.trackingNo }}: {{ item.reason }}
            </li>
          </ul>
          <p v-else>No parcels were rejected.</p>
        </section>
      </div>
    </section>
  </section>
</template>

<style scoped>
.intake-page {
  display: grid;
  gap: var(--space-5);
}

.mode-panel,
.intake-card,
.batch-results {
  padding: var(--space-4);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-surface);
}

.mode-panel {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: var(--space-3);
}

.mode-indicator {
  margin: 0;
  color: var(--color-text);
  font: var(--text-body);
}

.mode-buttons {
  display: flex;
  gap: var(--space-2);
}

.mode-button,
.primary-button {
  min-height: 44px;
  padding: var(--space-2) var(--space-3);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  background: var(--color-surface-soft);
  color: var(--color-text);
  font: var(--text-body);
  cursor: pointer;
}

.mode-button.active,
.primary-button {
  border-color: var(--color-primary);
  background: var(--color-primary);
  color: var(--color-on-primary);
}

.mode-button:focus-visible,
.primary-button:focus-visible,
.field input:focus-visible,
.field select:focus-visible,
.field textarea:focus-visible {
  outline: 2px solid var(--color-focus);
  outline-offset: 2px;
}

.mode-button:disabled,
.primary-button:disabled,
.field input:disabled,
.field select:disabled,
.field textarea:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.intake-card {
  display: grid;
  gap: var(--space-4);
}

.field {
  display: grid;
  gap: var(--space-1);
  margin: 0;
  padding: 0;
  border: 0;
  color: var(--color-text);
  font: var(--text-body-sm);
}

.field input,
.field select,
.field textarea {
  width: 100%;
  min-height: 44px;
  padding: var(--space-2) var(--space-3);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  background: var(--color-field);
  color: var(--color-text);
  font: var(--text-body);
}

.field textarea {
  resize: vertical;
}

.field-hint,
.batch-count {
  margin: 0;
  color: var(--color-text-muted);
  font: var(--text-caption);
}

.field-error {
  margin: 0;
  color: var(--color-danger);
  font: var(--text-caption);
}

.field input[aria-invalid='true'] {
  border-color: var(--color-danger);
}

.size-options {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-3);
}

.size-options label {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  min-height: 44px;
}

.full-shelf-warning {
  margin: 0;
  padding: var(--space-3);
  border: 1px solid var(--color-danger);
  border-radius: var(--radius-sm);
  color: var(--color-danger);
  font: var(--text-body-sm);
}

.success-message {
  margin: 0;
  padding: var(--space-3);
  border: 1px solid var(--color-success);
  border-radius: var(--radius-sm);
  color: var(--color-text);
  font: var(--text-body-sm);
}

.batch-results h2,
.batch-results h3 {
  margin: 0;
  color: var(--color-text);
}

.batch-results h2 {
  font: var(--text-title-lg);
}

.batch-results h3 {
  font: var(--text-title);
}

.result-columns {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: var(--space-4);
  margin-top: var(--space-3);
}

.batch-results ul {
  margin: var(--space-2) 0 0;
  padding-left: var(--space-4);
}

@media (max-width: 640px) {
  .result-columns {
    grid-template-columns: 1fr;
  }
}
</style>