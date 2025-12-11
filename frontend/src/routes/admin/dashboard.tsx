// src/routes/admin/dashboard.tsx
import { createFileRoute, useRouter } from '@tanstack/react-router'
import api from '../../api/Axios'

export const Route = createFileRoute('/admin/dashboard')({
  component: Dashboard,
})

function Dashboard() {
  const router = useRouter();

  const user = Route.useLoaderData();

   api.get('/api/auth/authenticated-user').then(res => console.log(res.data));



  const handleLogout = async () => {
    try {
      await api.post('/api/auth/logout')
      localStorage.removeItem('authToken')
      console.log('Logged out from server')
    } catch (err) {
      console.warn('Logout API failed (still clearing local session):', err)
      // This is normal if backend doesn't have logout endpoint
    } finally {
      // Always clear client-side auth
      localStorage.removeItem('token')
      // sessionStorage.removeItem('token') // if you use sessionStorage

      // THIS IS THE KEY LINE → redirect to /login
      await router.navigate({ to: '/login' })
      
      // Optional: force full page reload (clears any React state)
      // window.location.href = '/login'
    }
  }

 return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-50 to-purple-50 p-8">
      <div className="max-w-4xl mx-auto">
        <div className="bg-white rounded-2xl shadow-xl p-10">
          <div className="flex justify-between items-start mb-10">
            <div>
              <h1 className="text-4xl font-bold text-indigo-800">
                {/* Welcome back, {user.username || user.email.split('@')[0]}! */}
              </h1>
              {/* <p className="text-gray-600 mt-2">Email: <span className="font-medium">{user.email}</span></p>
              {user.role && <p className="text-sm text-indigo-600 mt-1">Role: {user.role}</p>} */}
            </div>

            <button
              onClick={handleLogout}
              className="px-6 py-3 bg-red-600 hover:bg-red-700 text-white font-semibold rounded-lg shadow-md transition"
            >
              Logout
            </button>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-12">
            <div className="bg-blue-50 p-6 rounded-xl">
              <h3 className="text-lg font-semibold text-blue-900">Total Users</h3>
              <p className="text-3xl font-bold text-blue-700 mt-2">1,234</p>
            </div>
            {/* Add more cards */}
          </div>
        </div>
      </div>
    </div>
  )
}