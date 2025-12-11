import React from 'react'

export default function TimeCard() {
  return (
    <div className="max-w-[400px] w-[300px] mx-auto mt-8">
        <div className="max-w-sm rounded overflow-hidden shadow-lg m-4">
           <div className="flex justify-around px-6 py-4 bg-linear-30 from-green-300 to-teal-200 text-white font-bold text-xl mb-2">
            <h2 className="text-green-900 mr-auto">Arpan Subedi</h2>
            <button className="text-xs border-2 border-green-700 text-teal-800 rounded p-2 hover:bg-green-600 hover:text-green-50 transition-all ease-in-out cursor-pointer">Pending</button>
           </div>
           <div className="px-6 py-4">
            <TimesheetData />
           </div>
           <div className="p-2 bg-green-200 flex-col justify-between w-full">
            <TimesheetRow className='text-sm ' title="Total Break: " value="2.00 Hours" />
            <TimesheetRow className='text-sm ' title="Total Hours: " value="8.00 Hours" />
           </div>
        </div>
    </div>
  )
}


const TimesheetData = () => {
    return (
        <div className="flex flex-col justify-around ">
            <TimesheetRow title="Clock In: " value="9:00 AM" />
            <TimesheetRow title="Clock Out: " value="15:00 PM" />
            <TimesheetRow title="Break In: " value="12:00 PM" />
            <TimesheetRow title="Break Out: " value="12:30 PM" />
            <TimesheetRow className=' font-normal' title="Note: " value="Busy day !" />
        </div>


    )
}


const TimesheetRow = ({title, value, className} : {title: string, value: string, className?: string}) => {
    return(
        <>
        <div className='flex justify-between mb-2 shadow-sm p-1 bg-white rounded'>
                <div>
                    <h6 className={`text-green-800  ${className}`}>{title} </h6>
                </div>




            <div>
            <h3 className={`text-green-900  ${className}`}>{value} </h3>
            </div>
        </div>
            </>
    );
}
