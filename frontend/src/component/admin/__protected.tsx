import { createFileRoute, redirect } from '@tanstack/react-router'

import api from '../../api/Axios'


api.interceptors.request.use((config) => {

    const token = localStorage.getItem('authToken')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
});

export const Route = createFileRoute('/admin/__protected/')({
  // This runs BEFORE the route renders
  loader: async () => {
    // try {
    //   const response = await api.get('/api/auth/authenticated-user')
    //   console.log('Authenticated user data:', response.data)
    //   return response.data // → { name, email, role, avatar, etc. }
    // } catch (error: any) {
    //   // Token expired or invalid
    //   localStorage.removeItem('authToken')
    //   throw redirect({
    //     to: '/login',
    //     search: { redirect: '/admin/dashboard' },
    //   })
    // }
  },
})