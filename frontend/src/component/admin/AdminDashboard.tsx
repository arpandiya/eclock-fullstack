import React from 'react'
import { useQueryClient, useQuery, useMutation } from '@tanstack/react-query';
import TimeCard from '../employee/TimeCard';




export default function AdminDashboard() {

    const queryClient = useQueryClient();

    //query to fetch posts
   const query = useQuery({
    queryKey: ['posts'],
    queryFn: async () => {
        const response = await fetch('https://jsonplaceholder.typicode.com/posts');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        console.log('Fetched posts:', await response.clone().json());
        return response.json();
    },

   });

   console.log(query.data);

   //mutation to post data
   const mutation = useMutation({
    mutationFn: async (newPost: { title: string; body: string; userId: number }) => {
        const response = await fetch('https://jsonplaceholder.typicode.com/posts', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newPost),
        });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    },
    onSuccess: () => {
        // Invalidate and refetch
        queryClient.invalidateQueries({ queryKey: ['posts'] });
    },
   });

   console.log(mutation.data);

  return (
    <div>
       <div className="flex justify-center items-center h-24 mt-24 bg-linear-to-l from-green-500 to-blue-600 p-4 text-white">
        <h1 className="text-2xl font-bold">Admin Dashboard</h1>
       </div>

       <div className="flex flex-wrap justify-center mt-8">
        <MenuCard title="Employee" />
        <MenuCard title="Timesheet" />
        <MenuCard title="Task" />
        <MenuCard title="Setting" />
       </div>

    <div className='flex flex-wrap '>
       <TimeCard />
       <TimeCard />
       <TimeCard />
       <TimeCard />
       <TimeCard />
       </div>
    </div>
  )
}


const MenuCard = ({title}: {title: string}) => {
    return (
        <>
        <div className="flex justify-center items-center max-w-sm min-w-48 rounded overflow-hidden shadow-lg m-4 p-6 bg-white border-indigo-200 border-4 border-y-green-500 hover:scale-105 transform transition duration-300 ease-in-out cursor-pointer">
            <div className="font-bold text-xl mb-2 text-teal-600">{title}</div>
        </div>
        </>
    );
}
