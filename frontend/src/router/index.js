import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import AppLayout from '../components/AppLayout.vue'

// One router configuration for the whole application. ADR-003 requires this.
//
// Every route here also appears in docs/02-design/frontend-page-map.md, with the
// same path, the same view name and the same role list (issue #82 asks for the
// two to agree). Routes whose view is still a placeholder are registered now so
// that navigation can be walked end to end before Sprint 2 fills the pages in.
// `/403` and the 404 catch-all are intentionally outside AppLayout: an error
// page must render even when the shell cannot.
const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginView.vue'),
    meta: { public: true },
  },
  {
    path: '/',
    component: AppLayout,
    children: [
      {
        path: 'dashboard',
        name: 'dashboard',
        component: () => import('../views/DashboardView.vue'),
        meta: { roles: ['staff', 'owner', 'admin'] },
      },
      {
        path: 'my-parcels',
        name: 'my-parcels',
        component: () => import('../views/MyParcelListView.vue'),
        meta: { roles: ['resident'] },
      },
      {
        path: 'my-parcels/:id',
        name: 'my-parcel-detail',
        component: () => import('../views/MyParcelDetailView.vue'),
        meta: { roles: ['resident'] },
      },
      {
        path: 'parcels',
        name: 'parcels',
        component: () => import('../views/ParcelListView.vue'),
        meta: { roles: ['staff', 'owner', 'admin'] },
      },
      {
        path: 'parcels/new',
        name: 'parcel-create',
        component: () => import('../views/ParcelCreateView.vue'),
        meta: { roles: ['staff'] },
      },
      {
        path: 'parcels/:id',
        name: 'parcel-detail',
        component: () => import('../views/ParcelDetailView.vue'),
        meta: { roles: ['staff', 'owner', 'admin'] },
      },
      {
        path: 'locations',
        name: 'locations',
        component: () => import('../views/LocationBoardView.vue'),
        meta: { roles: ['staff', 'owner', 'admin'] },
      },
      {
        path: 'pickup',
        name: 'pickup',
        component: () => import('../views/PickupVerifyView.vue'),
        meta: { roles: ['staff'] },
      },
      {
        path: 'exceptions',
        name: 'exceptions',
        component: () => import('../views/ExceptionListView.vue'),
        meta: { roles: ['staff', 'owner', 'admin'] },
      },
      {
        path: 'reports',
        name: 'reports',
        component: () => import('../views/ReportView.vue'),
        meta: { roles: ['owner', 'admin'] },
      },
      {
        path: 'users',
        name: 'users',
        component: () => import('../views/UserListView.vue'),
        meta: { roles: ['owner', 'admin'] },
      },
      {
        path: 'settings',
        name: 'settings',
        component: () => import('../views/SettingsView.vue'),
        meta: { roles: ['admin'] },
      },
      {
        path: 'audit',
        name: 'audit',
        component: () => import('../views/AuditLogView.vue'),
        meta: { roles: ['owner', 'admin'] },
      },
    ],
  },
  { path: '/403', name: 'forbidden', component: () => import('../views/Error403View.vue') },
  { path: '/:pathMatch(.*)*', name: 'not-found', component: () => import('../views/NotFoundView.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// Three rules, in the order the page map states them: unauthenticated users go
// to the login page, a signed-in user never sees the login page again, and a
// role that is not permitted goes to 403.
router.beforeEach((to) => {
  const auth = useAuthStore()
  const signedIn = auth.ensureValidSession()

  if (to.meta.public) {
    return signedIn ? auth.landingRoute() : true
  }

  if (!signedIn) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }

  if (to.path === '/') {
    return auth.landingRoute()
  }

  const allowed = to.meta.roles
  if (allowed && !allowed.includes(auth.role)) {
    return '/403'
  }

  return true
})

export default router