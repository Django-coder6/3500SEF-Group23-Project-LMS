<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { fetchLocations } from '../api/locations'
import ErrorState from '../components/ErrorState.vue'
import LoadingState from '../components/LoadingState.vue'
import LocationGrid from '../components/LocationGrid.vue'
import PageHeader from '../components/PageHeader.vue'

const loading = ref(false)
const locations = ref([])
const error = ref(null)
const selectedZone = ref('all')
const selectedId = ref('')

const zones = computed(() => (
  [...new Set(locations.value.map((location) => location.zone))]
    .sort((left, right) => left.localeCompare(right))
))

const filteredLocations = computed(() => {
  if (selectedZone.value === 'all') {
    return locations.value
  }

  return locations.value.filter(
    (location) => location.zone === selectedZone.value,
  )
})

const visibleZones = computed(() => {
  if (selectedZone.value === 'all') {
    return zones.value
  }

  return zones.value.filter((zone) => zone === selectedZone.value)
})

const locationsByZone = computed(() => (
  Object.fromEntries(
    visibleZones.value.map((zone) => [
      zone,
      filteredLocations.value.filter(
        (location) => location.zone === zone,
      ),
    ]),
  )
))

const selectedLocation = computed(() => (
  locations.value.find((location) => location.id === selectedId.value)
    ?? null
))

watch(selectedZone, () => {
  selectedId.value = ''
})

function normaliseError(value) {
  return {
    status: value?.status ?? 0,
    code: value?.code ?? 'REQUEST_FAILED',
    message: value?.message ?? 'Storage locations could not be loaded.',
    requestId: value?.requestId ?? null,
  }
}

async function loadLocations() {
  loading.value = true
  error.value = null

  try {
    locations.value = await fetchLocations()
  } catch (value) {
    error.value = normaliseError(value)
  } finally {
    loading.value = false
  }
}

function selectLocation(location) {
  selectedId.value = location.id
}

function formatLabel(value) {
  return value
    ? `${value.charAt(0).toUpperCase()}${value.slice(1)}`
    : ''
}

onMounted(loadLocations)
</script>

<template>
  <section class="locations-page">
    <PageHeader
      title="Storage locations"
      description="Review capacity by zone and confirm which slots are free before parcel intake."
    />

    <LoadingState
      v-if="loading"
      label="Loading storage locations"
    />

    <ErrorState
      v-else-if="error"
      :error="error"
      @retry="loadLocations"
    />

    <section
      v-else-if="locations.length === 0"
      class="empty-card"
    >
      <h2>No storage locations configured</h2>
      <p>
        Ask an administrator to configure the collection point before
        registering parcels.
      </p>
    </section>

    <template v-else>
      <section class="toolbar" aria-label="Location filters">
        <label for="zone-filter" class="filter-label">Zone</label>

        <select
          id="zone-filter"
          v-model="selectedZone"
          class="zone-select"
          data-test="zone-filter"
        >
          <option value="all">All zones</option>
          <option
            v-for="zone in zones"
            :key="zone"
            :value="zone"
          >
            Zone {{ zone }}
          </option>
        </select>

        <span class="result-count">
          {{ filteredLocations.length }}
          {{ filteredLocations.length === 1 ? 'location' : 'locations' }}
        </span>
      </section>

      <div class="zone-list">
        <LocationGrid
          v-for="zone in visibleZones"
          :key="zone"
          :zone="zone"
          :locations="locationsByZone[zone]"
          :selected-id="selectedId"
          @select="selectLocation"
        />
      </div>

      <section
        v-if="selectedLocation"
        class="selection-card"
        :aria-label="`Selected location ${selectedLocation.code}`"
      >
        <div class="selection-heading">
          <div>
            <p class="eyebrow">Selected location</p>
            <h2 class="selection-code">
              {{ selectedLocation.code }}
            </h2>
          </div>

          <span class="selection-status">
            {{ formatLabel(selectedLocation.status) }}
          </span>
        </div>

        <dl class="selection-facts">
          <div>
            <dt>Zone</dt>
            <dd>{{ selectedLocation.zone }}</dd>
          </div>
          <div>
            <dt>Size</dt>
            <dd>{{ formatLabel(selectedLocation.size) }}</dd>
          </div>
          <div>
            <dt>Status</dt>
            <dd>{{ formatLabel(selectedLocation.status) }}</dd>
          </div>
        </dl>

        <p class="contract-note">
          Assigning a storage location to an existing registered parcel is
          not available in the current API contract. A location is selected
          when the parcel is registered.
        </p>
      </section>
    </template>
  </section>
</template>

<style scoped>
.locations-page {
  display: grid;
  gap: var(--space-5);
}

.toolbar {
  display: flex;
  align-items: end;
  flex-wrap: wrap;
  gap: var(--space-3);
  padding: var(--space-4);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-surface);
}

.filter-label {
  display: grid;
  gap: var(--space-1);
  color: var(--color-text);
  font: var(--text-body-sm);
}

.zone-select {
  min-width: 160px;
  min-height: 44px;
  padding: var(--space-2) var(--space-3);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  background: var(--color-field);
  color: var(--color-text);
  font: var(--text-body);
}

.zone-select:focus-visible {
  outline: 2px solid var(--color-focus);
  outline-offset: 2px;
}

.result-count {
  margin-left: auto;
  color: var(--color-text-muted);
  font: var(--text-caption);
}

.zone-list {
  display: grid;
  gap: var(--space-4);
}

.empty-card,
.selection-card {
  padding: var(--space-4);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-surface);
}

.empty-card h2,
.selection-code {
  margin: 0;
  color: var(--color-text);
  font: var(--text-title-lg);
}

.empty-card p,
.contract-note {
  margin: var(--space-2) 0 0;
  color: var(--color-text-muted);
  font: var(--text-body-sm);
}

.selection-heading {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: var(--space-4);
}

.eyebrow {
  margin: 0 0 var(--space-1);
  color: var(--color-text-muted);
  font: var(--text-caption);
  text-transform: uppercase;
}

.selection-status {
  padding: var(--space-1) var(--space-2);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-pill);
  color: var(--color-text);
  font: var(--text-caption);
  text-transform: capitalize;
}

.selection-facts {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: var(--space-3);
  margin: var(--space-4) 0 0;
}

.selection-facts div {
  display: grid;
  gap: var(--space-1);
  padding-top: var(--space-2);
  border-top: 1px solid var(--color-border);
}

.selection-facts dt {
  color: var(--color-text-muted);
  font: var(--text-caption);
}

.selection-facts dd {
  margin: 0;
  color: var(--color-text);
  font: var(--text-body);
  text-transform: capitalize;
}

@media (max-width: 640px) {
  .toolbar {
    align-items: stretch;
  }

  .result-count {
    width: 100%;
    margin-left: 0;
  }

  .selection-facts {
    grid-template-columns: 1fr;
  }
}
</style>
