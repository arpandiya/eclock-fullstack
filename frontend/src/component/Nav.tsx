import React from 'react'

export default function Nav() {
  return (
    <div>
      <nav className='flex flex-col items-center justify-between h-[15vh] w-full border-b-2 border-gray-200  bg-radial from-green-100 to-teal-50 shadow-md'>
       <div className="w-full flex items-baseline justify-center py-0 ">
        <img src="/eclock-logo-final.png" alt="eClock Logo" className='w-32 h-auto mt-4 pt-2'/>
       </div>

       <ul className='flex flex-row justify-end items-center mt-2'>
        {/* <li className='text-green-800 mx-4 hover:underline cursor-pointer'>Home</li>
        <li className='text-green-800 mx-4 hover:underline cursor-pointer'>About</li>
        <li className='text-green-800 mx-4 hover:underline cursor-pointer'>Contact</li> */}
       </ul>
      </nav>
    </div>
  )
}
