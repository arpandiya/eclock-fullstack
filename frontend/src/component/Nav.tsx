import React from 'react'

export default function Nav() {
  return (
    <div>
      <nav className='flex flex-col items-center justify-between h-[15vh] w-full  bg-radial from-green-500 to-green-600'>
       <div className="w-full flex items-baseline justify-center py-0 ">
        <img src="/eclock-logo-final.png" alt="eClock Logo" className='w-32 h-auto mt-4 pt-2 '/>
       </div>

       <ul className='flex flex-row justify-end items-center mt-2'>
        <li className='text-white mx-4 hover:underline cursor-pointer'>Home</li>
        <li className='text-white mx-4 hover:underline cursor-pointer'>About</li>
        <li className='text-white mx-4 hover:underline cursor-pointer'>Contact</li>
       </ul>
      </nav>
    </div>
  )
}
