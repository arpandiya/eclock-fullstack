import axios from 'axios';


const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
})

// Request interceptor: Attach token from localStorage to every request
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// Response interceptor: Auto-save token if server returns it
api.interceptors.response.use(
  (response) => {
    // Case 1: Token in response body (most common)
    if (response.data?.token) {
      localStorage.setItem('token', response.data.token)
    }
    // Case 2: Token in response headers (rare, but some APIs do this)
    else if (response.headers['authorization']) {
      const headerToken = response.headers['authorization'].replace('Bearer ', '')
      localStorage.setItem('token', headerToken)
    }
    // Case 3: Token under different key (e.g. accessToken, jwt)
    else if (response.data?.accessToken) {
      localStorage.setItem('token', response.data.accessToken)
    }
    else if (response.data?.jwt) {
      localStorage.setItem('token', response.data.jwt)
    }

    return response
  },
  (error) => {
    // Auto logout on 401
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default api

