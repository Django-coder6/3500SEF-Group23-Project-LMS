<script setup>
// error must use the normalized error structure from http.js:
// { status, code, message, requestId }
defineProps({
  error: {
    type: Object,
    required: true,
  },
  retryLabel: {
    type: String,
    default: 'Retry',
  },
})

// Component only emits the retry event, leaving it to the page to decide which request to re-execute.
defineEmits(['retry'])
</script>

<template>
  <!-- "Error" text ensures users don't rely solely on the red color to identify errors. -->
  <section class="error-state" role="alert">
    <p class="error-label">Error</p>
    <p class="error-message">{{ error.message }}</p>

    <!-- requestId is only displayed when included in the error. -->
    <p v-if="error.requestId" class="request-id">
      Request ID: {{ error.requestId }}
    </p>

    <button type="button" class="retry-button" @click="$emit('retry')">
      {{ retryLabel }}
    </button>
  </section>
</template>

<style scoped>
.error-state {
  padding: var(--space-4);
  border: 1px solid var(--color-danger);
  border-radius: var(--radius-sm);
  background: var(--color-surface);
}

.error-label {
  margin: 0 0 var(--space-2);
  color: var(--color-danger);
  font: var(--text-title);
}

.error-message {
  margin: 0 0 var(--space-2);
  color: var(--color-text);
  font: var(--text-body);
}

.request-id {
  margin: 0 0 var(--space-3);
  color: var(--color-text-muted);
  font: var(--text-caption);
}

/* 44px is the minimum touch target size defined by the design system. */
.retry-button {
  min-height: 44px;
  padding: var(--space-2) var(--space-3);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  background: var(--color-surface-soft);
  color: var(--color-text);
  font: inherit;
  cursor: pointer;
}

.retry-button:focus-visible {
  outline: 2px solid var(--color-focus);
  outline-offset: 2px;
}
</style>
