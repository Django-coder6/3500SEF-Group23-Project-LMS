<script setup>
// LoadingState only shows "Verifying..." not for requests
// Label
defineProps({
  label: {
    type: String,
    default: 'Loading',
  },
})
</script>

<template>
  <!-- role="status" let the screen reader know this is a live region -->
  <div class="loading-state" role="status" aria-live="polite">
    <span>{{ label }}...</span>
  </div>
</template>

<style scoped>
/* All colors, spacing and font both come form tokens.css */
.loading-state {
  min-height: var(--space-12);
  display: grid;
  place-items: center;
  padding: var(--space-4);
  color: var(--color-text-muted);
  font: var(--text-body);
}
</style>
