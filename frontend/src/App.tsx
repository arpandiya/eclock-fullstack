
import './App.css'
import AdminDashboard from './component/admin/AdminDashboard'
import Login from './component/Login'


import {
    useQuery,
    useMutation,
    useQueryClient,
    QueryClient,
    QueryClientProvider,

} from '@tanstack/react-query'


const queryClient = new QueryClient();

function App() {

  return (
    <QueryClientProvider client={queryClient}>
    <AdminDashboard />
     </QueryClientProvider>
  )
}

export default App
