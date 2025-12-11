// src/routes/index.tsx  (or wherever your login route is)
import React, { useState } from 'react'
import api from '../api/Axios'
import logo from '/eclock-logo-final.png'
import { useRouter } from '@tanstack/react-router'





export default function LoginComponent() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')

  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    try {
      const response = await api.post('/api/auth/login', { username, password })
      console.log('Login successful:', response.data)

      await router.navigate({ to: '/admin/dashboard' });

      // Save token or user data if needed
      // localStorage.setItem('token', response.data.token)

      // THIS IS THE CORRECT WAY TO NAVIGATE
    
      // or: router.navigate({ to: '/admin/dashboard' })

    } catch (error: any) {
      console.error('Login failed:', error.response?.data || error.message)
      alert('Login failed. Please check your credentials.')
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <div className="text-center mb-6">
          <img src={logo} alt="Eclock Logo" className="mx-auto mb-4 w-40" />
          <h2 className="text-3xl font-bold text-green-600 border-t-2 border-green-200 pt-4">
            Login
          </h2>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-green-700 font-medium mb-2" htmlFor="username">
              Username
            </label>
            <input
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:border-green-500 transition"
              type="text"
              id="username"
              placeholder="Enter your username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>

          <div>
            <label className="block text-green-700 font-medium mb-2" htmlFor="password">
              Password
            </label>
            <input
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:border-green-500 transition"
              type="password"
              id="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button
            type="submit"
            className="w-full h-12 bg-gradient-to-r from-green-500 to-green-600 text-white text-xl font-semibold rounded-lg hover:from-green-600 hover:to-green-700 transform hover:scale-105 transition duration-200 shadow-md"
          >
            Login
          </button>

          <a href="#" className="block text-center text-green-600 hover:underline text-sm">
            Forgot Password?
          </a>
        </form>
      </div>
    </div>
  )
}