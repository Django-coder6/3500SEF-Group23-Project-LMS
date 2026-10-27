<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const router = useRouter()
const route = useRoute()

const username = ref('')
const password = ref('')
const error = ref('')
const busy = ref(false)
const invalidMessage = 'Invalid username or password.'

async function submit() {
  error.value = ''
  busy.value = true

  try {
    const landing = await auth.login(username.value.trim(), password.value)
    const redirect = typeof route.query.redirect === 'string'
      && route.query.redirect.startsWith('/')
      && !route.query.redirect.startsWith('//')
      ? route.query.redirect
      : null

    await router.replace(redirect || landing)
  } catch (e) {
    error.value = e.code === 'INVALID_CREDENTIALS'
      ? invalidMessage
      : 'Sign in failed. Please try again.'
  } finally {
    busy.value = false
  }
}
</script>

<template>
  <div class="page">
    <form
  class="card"
  :aria-busy="busy"
  @submit.prevent="submit"
>
      <h1 class="title">Sign in</h1>
      <p class="hint">Enter your collection-point account details.</p>

      <label class="field">
        <span>Username</span>
        <input
          v-model="username"
          name="username"
          autocomplete="username"
          autofocus
          required
          :disabled="busy"
          :aria-invalid="Boolean(error)"
/>
      </label>

      <label class="field">
        <span>Password</span>
        <input
          v-model="password"
          type="password"
          name="password"
          autocomplete="current-password"
          required
          :disabled="busy"
          :aria-invalid="Boolean(error)"
/>
      </label>
       <p v-if="error" class="error" role="alert">
        {{ error }}
      </p>

      <button
        type="submit"
        class="submit"
        :disabled="busy"
        :aria-busy="busy"
>
        {{ busy ? 'Signing in...' : 'Sign in' }}
</button>

    </form>
  </div>
</template>

<style scoped>
.page {
  min-height: 100vh;
  display: grid;
  place-items: center;
  padding: var(--space-6);
  background: var(--color-bg-gradient);
}
.card {
  width: 100%;
  max-width: 340px;
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
  padding: var(--space-6);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-surface);
  box-shadow: var(--shadow-resting);
}
.title {
  margin: 0;
  color: var(--color-text);
  font: var(--text-title-lg);
}
.hint {
  margin: 0;
  color: var(--color-text-muted);
  font: var(--text-caption);
}
.field {
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
  font: var(--text-body-sm);
}
.field input {
  width: 100%;
  min-height: 44px;
  padding: var(--space-2) var(--space-3);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  background: var(--color-field);
  color: var(--color-text);
  font: var(--text-body);
}

.field input:focus-visible,
.submit:focus-visible {
  outline: 2px solid var(--color-focus);
  outline-offset: 2px;
}

.field input[aria-invalid='true'] {
  border-color: var(--color-danger);
}

.field input:disabled {
  opacity: 0.65;
  cursor: not-allowed;
}
.error {
  margin: 0;
  color: var(--color-danger);
  font: var(--text-caption);
}
.submit {
  min-height: 44px;
  padding: var(--space-2) var(--space-3);
  border: 1px solid var(--color-primary);
  border-radius: var(--radius-sm);
  background: var(--color-primary);
  color: var(--color-on-primary);
  font: var(--text-body);
  cursor: pointer;
}

.submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

</style>