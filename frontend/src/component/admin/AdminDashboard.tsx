
import axios from 'axios'
import { useRouter } from '@tanstack/react-router'


export default function AdminDashboard() {
    const router = useRouter();

    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const handleLogout = async () => {
        try {
            await axios.post('/api/auth/logout');   
            await router.navigate({ to: '/Login' });
        } catch (err) {
            console.error('Logout failed:', err);
        }

  return (
    <div>
        HELLLLLLOOOOOOO
       <button onClick={handleLogout} className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600">
        Logout
       </button>
    </div>
  )
}
}
