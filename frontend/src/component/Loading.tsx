
import logo from '/eclock-emblem.png';

export default function Loading() {
  return (
   <div className="min-h-screen flex items-center justify-center bg-gray-100 ">
  
    <div className='flex flex-col'>
    <img
      src={logo}
      alt="Loading"
      className="relative right-6 w-48 h-auto animate-ping origin-center pb-0"
      style={{ animationDuration: '2s' }} // optional: control speed
    />
    <h2 className='relative left-18 animate-pulse text-green-600'>Loading...</h2>
    </div>
 
</div>

  )
}
