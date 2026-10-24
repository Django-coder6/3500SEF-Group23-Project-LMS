<script setup>
import { computed } from 'vue'

const props = defineProps({
  zone: {
    type: String,
    required: true,
  },
  locations: {
    type: Array,
    required: true,
  },
  selectedId: {
    type: String,
    default: '',
  },
})

defineEmits(['select'])

const statusLabels = {
  available: 'Available',
  reserved: 'Reserved',
  occupied: 'Occupied',
  disabled: 'Disabled',
}

const sizes = ['small', 'medium', 'large']

const availableCount = computed(() => (
  props.locations.filter((location) => (
    location.status === 'available'
  )).length
))

const isFull = computed(() => (
  props.locations.length > 0 && availableCount.value === 0
))

const availabilityBySize = computed(() => (
  sizes
    .map((size) => {
      const locationsOfSize = props.locations.filter(
        (location) => location.size === size,
      )

      return {
        size,
        total: locationsOfSize.length,
        available: locationsOfSize.filter((location) => (
          location.status === 'available'
        )).length,
      }
    })
    .filter((item) => item.total > 0)
))
</script>

<template>
  <section class="zone-section" :aria-labelledby="`zone-${zone}-title`">
    <header class="zone-header">
      <div>
        <h2 :id="`zone-${zone}-title`" class="zone-title">
          Zone {{ zone }}
        </h2>
        <p class="zone-summary">
          {{ availableCount }} of {{ locations.length }} slots available
        </p>
      </div>

      <span
        v-if="isFull"
        class="full-badge"
      >
        Full shelf
      </span>
    </header>

    <div
      v-if="isFull"
      class="full-warning"
      role="alert"
    >
      <strong>No available slots in Zone {{ zone }}.</strong>
      Check another zone before starting intake.
    </div>

    <div
      v-if="availabilityBySize.length"
      class="size-summary"
      aria-label="Availability by parcel size"
    >
      <span
        v-for="item in availabilityBySize"
        :key="item.size"
        class="size-item"
        :class="{ 'size-empty': item.available === 0 }"
      >
        {{ item.size }}: {{ item.available }} available
      </span>
    </div>

    <p v-if="locations.length === 0" class="empty-zone">
      No locations are configured in Zone {{ zone }}.
    </p>

    <div
      v-else
      class="location-grid"
      role="list"
      :aria-label="`Locations in Zone ${zone}`"
    >
      <button
        v-for="location in locations"
        :key="location.id"
        type="button"
        class="location-cell"
        :class="[
          `status-${location.status}`,
          { selected: selectedId === location.id },
        ]"
        :aria-pressed="selectedId === location.id"
        role="listitem"
        :data-test="`location-${location.code}`"
        @click="$emit('select', location)"
      >
        <strong class="location-code">{{ location.code }}</strong>
        <span class="location-size">{{ location.size }}</span>
        <span class="location-status">
          {{ statusLabels[location.status] }}
        </span>
      </button>
    </div>
  </section>
</template>

<style scoped>
.zone-section {
  display: grid;
  gap: var(--space-3);
  padding: var(--space-4);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-surface);
}

.zone-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: var(--space-3);
}

.zone-title {
  margin: 0;
  color: var(--color-text);
  font: var(--text-title);
}

.zone-summary {
  margin: var(--space-1) 0 0;
  color: var(--color-text-muted);
  font: var(--text-caption);
}

.full-badge {
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-pill);
  background: var(--color-danger);
  color: var(--color-on-primary);
  font: var(--text-caption);
}

.full-warning {
  padding: var(--space-3);
  border: 1px solid var(--color-danger);
  border-radius: var(--radius-sm);
  color: var(--color-text);
  font: var(--text-body-sm);
}

.full-warning strong {
  display: block;
  color: var(--color-danger);
}

.size-summary {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
}

.size-item {
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-pill);
  background: var(--color-surface-soft);
  color: var(--color-text-muted);
  font: var(--text-caption);
  text-transform: capitalize;
}

.size-empty {
  color: var(--color-danger);
  border: 1px solid var(--color-danger);
}

.empty-zone {
  margin: 0;
  color: var(--color-text-muted);
  font: var(--text-body-sm);
}

.location-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: var(--space-2);
}

.location-cell {
  min-height: 88px;
  display: grid;
  align-content: center;
  justify-items: start;
  gap: var(--space-1);
  padding: var(--space-3);
  border: 2px solid transparent;
  border-radius: var(--radius-sm);
  color: var(--color-text);
  font: var(--text-body-sm);
  text-align: left;
  cursor: pointer;
}

.location-code {
  font: var(--text-title);
}

.location-size {
  color: var(--color-text-muted);
  font: var(--text-caption);
  text-transform: capitalize;
}

.location-status {
  font: var(--text-caption);
}

.status-available {
  border-color: var(--color-success);
  background: color-mix(
    in srgb,
    var(--color-success) 10%,
    var(--color-surface)
  );
}

.status-reserved {
  border-color: var(--color-warning);
  background: color-mix(
    in srgb,
    var(--color-warning) 10%,
    var(--color-surface)
  );
}

.status-occupied {
  border-color: var(--color-info);
  background: color-mix(
    in srgb,
    var(--color-info) 10%,
    var(--color-surface)
  );
}

.status-disabled {
  border-color: var(--color-text-muted);
  background: var(--color-surface-soft);
  color: var(--color-text-muted);
}

.location-cell.selected {
  outline: 2px solid var(--color-focus);
  outline-offset: 2px;
}

.location-cell:focus-visible {
  outline: 2px solid var(--color-focus);
  outline-offset: 2px;
}

@media (max-width: 640px) {
  .location-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .location-cell {
    min-height: 80px;
  }
}
</style>