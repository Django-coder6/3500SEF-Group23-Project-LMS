<script setup>
// title is the page title that must be filled.
// description is an optional explanatory text.
defineProps({
  title: {
    type: String,
    required: true,
  },
  description: {
    type: String,
    default: '',
  },
})
</script>

<template>
  <header class="page-header">
    <div class="page-heading">
      <h1 class="page-title">{{ title }}</h1>

      <!-- only show description if it's provided -->
      <p v-if="description" class="page-description">
        {{ description }}
      </p>
    </div>

    <div v-if="$slots.actions" class="page-actions">
      <slot name="actions" />
    </div>
  </header>
</template>

<style scoped>
.page-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: var(--space-4);
  margin-bottom: var(--space-6);
}

.page-title {
  margin: 0;
  color: var(--color-text);
  font: var(--text-title-lg);
}

.page-description {
  margin: var(--space-1) 0 0;
  color: var(--color-text-muted);
  font: var(--text-body);
}

.page-actions {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

@media (max-width: 640px) {
  .page-header {
    flex-direction: column;}
}
</style>
