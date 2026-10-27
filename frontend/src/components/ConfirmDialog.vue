<script setup>
import { ElButton, ElDialog } from 'element-plus'
import 'element-plus/theme-chalk/base.css'
import 'element-plus/theme-chalk/el-overlay.css'
import 'element-plus/theme-chalk/el-dialog.css'
import 'element-plus/theme-chalk/el-button.css'

// modelValue is used to control whether the dialog is open.
// loading is true when the dialog is submitting, preventing duplicate submissions.
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    required: true,
  },
  message: {
    type: String,
    required: true,
  },
  confirmLabel: {
    type: String,
    default: 'Confirm',
  },
  loading: {
    type: Boolean,
    default: false,
  },
  danger: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['update:modelValue', 'confirm'])

// Closing the dialog only notifies the parent component to update modelValue.
// If the request is still in progress, temporarily ignore the close action.
function changeVisibility(visible) {
  if (!props.loading) {
    emit('update:modelValue', visible)
  }
}

function closeDialog() {
  changeVisibility(false)
}

// ConfirmDialog does not handle API requests; the parent component calls the API after receiving the confirm event.
// props.loading serves as a safeguard to prevent multiple confirmations from being triggered by double-clicking.
function confirmDialog() {
  if (!props.loading) {
    emit('confirm')
  }
}
</script>

<template>
  <ElDialog
    :model-value="modelValue"
    :title="title"
    :close-on-click-modal="false"
    :close-on-press-escape="!loading"
    @update:model-value="changeVisibility"
  >
    <p class="dialog-message">{{ message }}</p>

    <template #footer>
      <ElButton
        :disabled="loading"
        @click="closeDialog"
      >
        Cancel
      </ElButton>

      <ElButton
        :type="danger ? 'danger' : 'primary'"
        :loading="loading"
        :disabled="loading"
        @click="confirmDialog"
      >
        {{ confirmLabel }}
      </ElButton>
    </template>
  </ElDialog>
</template>

<style scoped>
.dialog-message {
  margin: 0;
  color: var(--color-text);
  font: var(--text-body);
}
</style>
