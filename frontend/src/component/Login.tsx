import React, {useState} from 'react'
import axios from 'axios';

export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        // Handle login logic here
        console.log('Username:', username);
        console.log('Password:', password);


const api = axios.create({
    baseURL: 'http://localhost:8080',
    withCredentials: true,
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',

    }
});

const loginUser = async (credentials: { username: string; password: string; }) => {
    try {
        const response = await api.post('/api/auth/login', credentials);
        return response.data;
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
};
        loginUser({ username, password })
            .then(data => {
                console.log('Login successful:', data);
            })
            .catch(error => {
                console.error('Login failed:', error);
            });

    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { id, value } = e.target;
        if (id === 'username') {
            setUsername(value);
        } else if (id === 'password') {
            setPassword(value);
        }
    };



  return (
    <div>
        <div className="min-h-screen flex items-center justify-center bg-gray-100">
        <div className="bg-white p-8 rounded shadow-md w-full max-w-md">
            <div className="">
            <h2 className="text-2xl font-bold mb-6 text-center">Login</h2>
            </div>
            <form onSubmit={handleSubmit} method='POST'>
                <div className="mb-4">
                    <label className="block text-gray-700 mb-2" htmlFor="username">Username</label>
                    <input
                    className="w-full px-3 py-2 border rounded"
                     type="text" id="username"
                      placeholder="Enter your username" value={username} onChange={handleChange} />
                </div>
                <div className="mb-4">
                    <label className="block text-gray-700 mb-2" htmlFor="password">Password</label>
                    <input className="w-full px-3 py-2 border rounded" type="password" id="password" placeholder="Enter your password" value={password} onChange={handleChange} />
                </div>
                <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600">Login</button>
            </form>
        </div>
    </div>
    </div>
  )
}

