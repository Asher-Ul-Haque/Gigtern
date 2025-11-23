"use client"
import type React from "react"

import { useState, useEffect } from "react"

import {
Briefcase,
LayoutDashboard,
Search,
User,
LogOut,
TrendingUp,
DollarSign,
Clock,
ChevronRight,
Star,
} from "lucide-react"

import { getGigs, getMyGig } from "../api" // adjust path
interface DashboardProps {

onNavigate: (page: string) => void

}
interface GigResponse {

id: number

employerId: number

title: string

description: string

paymentType: string

payRate: number

status: string

skillsRequired: string[]

postedDate: string

employerCompanyName: string

durationEstimateHours: number | null

}
const APP_LOGO = "/images/logo.png"
export const StudentDashboard: React.FC<DashboardProps> = ({ onNavigate }) => { // Corrected prop type

const [recommendedGigs, setRecommendedGigs] = useState<GigResponse[]>([])

const [myGigs, setMyGigs] = useState<GigResponse[]>([])

const [loading, setLoading] = useState(true)
useEffect(() => {

async function fetchData() {

setLoading(true)

try {
// Check if rec is an array or object
const rec = await getGigs()
// Check if mine is an array or object
const mine = await getMyGig()

setRecommendedGigs(Array.isArray(rec) ? rec : (rec ? [rec] : []))

setMyGigs(Array.isArray(mine) ? mine : (mine ? [mine] : []))

} catch (err) {

console.error(err)

} finally {

setLoading(false)

}

}

fetchData()

}, [])

// FIX: Wrap the entire JSX structure in the main div and ensure proper closing structure.
return (
<div className="bg-black text-white min-h-screen flex">
  {/* Sidebar */}
  <aside className="w-72 bg-zinc-950 border-r border-white/5 hidden lg:flex flex-col fixed h-full z-10 shadow-2xl">
    <div className="p-8 flex items-center gap-4">
      <img
        src={APP_LOGO || "/placeholder.svg"}
        alt="GigTern Logo"
        className="w-10 h-10 object-contain rounded-lg"
      />
      <span className="text-xl font-bold tracking-tight">
        GigTern <span className="text-xs bg-violet-500 px-2 py-0.5 rounded-full ml-1">Student</span>
      </span>
    </div>

    <nav className="px-4 py-6 space-y-1.5 flex-1">
      <div className="px-4 text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">
        Student Menu
      </div>
      {[
        { icon: LayoutDashboard, label: "Dashboard", active: true, page: "/student-dashboard" },
        { icon: Search, label: "Find Gigs", page: "/find-gigs" },
        { icon: Briefcase, label: "My Gigs", page: "/my-gigs" },
        { icon: User, label: "Profile", page: "/profile" },
      ].map((item, index) => (
        <button
          key={index}
          onClick={() => item.page && onNavigate(item.page)}
          className={`sidebar-item w-full flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 group ${
            item.active
              ? "bg-gradient-to-r from-violet-600/10 to-indigo-600/10 text-violet-400 border border-violet-600/20"
              : "text-gray-400 hover:bg-white/5 hover:text-white"
          }`}
        >
          <item.icon className={`w-5 h-5 ${item.active ? "text-violet-400" : "group-hover:text-white"}`} />
          <span className="font-medium">{item.label}</span>
        </button>
      ))}
    </nav>

    <div className="p-4 border-t border-white/5">
      <button
        onClick={() => onNavigate("/")}
        className="sidebar-item flex items-center gap-3 px-4 py-3 rounded-xl text-red-400 hover:bg-red-400/10 w-full"
      >
        <LogOut size={20} /> Sign Out
      </button>
    </div>
  </aside>

  {/* Main Content */}
  <main className="flex-1 lg:ml-72 bg-black min-h-screen p-6 lg:p-10">
    <header className="flex justify-between items-center mb-10">
      <div>
        <h1 className="text-3xl font-bold mb-1">Welcome back, Alex! 👋</h1>
        <p className="text-gray-400">Here's what's happening with your gigs today.</p>
      </div>
    </header>

    {/* Recommended Gigs */}
    <section className="mb-10">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-xl font-bold">Recommended Gigs</h2>
        <button
          onClick={() => onNavigate("/find-gigs")}
          className="text-violet-400 text-sm font-medium hover:text-violet-300 flex items-center gap-1"
        >
          View All <ChevronRight size={16} />
        </button>
      </div>

      {loading ? (
        <p>Loading...</p>
      ) : (
        <div className="grid md:grid-cols-2 gap-4">
          {recommendedGigs.map((gig) => (
            <div
              key={gig.id}
              className="dashboard-card p-4 rounded-2xl bg-zinc-900 border border-white/5 hover:border-violet-500/30 transition-all group cursor-pointer"
              onClick={() => onNavigate(`/gig/${gig.id}`)}
            >
              <div className="flex justify-between items-center mb-2">
                <h3 className="font-bold text-lg group-hover:text-violet-400 transition-colors">{gig.title}</h3>
                <Star size={18} className="text-gray-500" />
              </div>
              <p className="text-sm text-gray-400 mb-2">{gig.employerCompanyName}</p>
              <div className="flex gap-3 text-xs font-medium">
                <span className="px-2 py-1 rounded bg-green-500/10 text-green-400 flex items-center gap-1">
                  <DollarSign size={12} /> {gig.payRate}
                </span>
                {gig.durationEstimateHours && (
                  <span className="px-2 py-1 rounded bg-blue-500/10 text-blue-400 flex items-center gap-1">
                    <Clock size={12} /> {gig.durationEstimateHours}h
                  </span>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </section>

    {/* My Gigs */}
    <section>
      <h2 className="text-xl font-bold mb-6">My Active Gigs</h2>
      {loading ? (
        <p>Loading...</p>
      ) : myGigs.length === 0 ? (
        <p>You currently have no active gigs.</p>
      ) : (
        <div className="space-y-4">
          {myGigs.map((gig) => (
            <div
              key={gig.id}
              className="flex items-center justify-between p-4 bg-zinc-900 border border-white/5 rounded-xl"
            >
              <div className="flex items-center gap-4">
                <div className="p-3 bg-violet-500/10 rounded-lg text-violet-400">
                  <Briefcase size={20} />
                </div>
                <div>
                  <h3 className="font-bold">{gig.title}</h3>
                  <p className="text-sm text-gray-400">{gig.status}</p>
                </div>
              </div>
              <div>
                <ChevronRight size={20} className="text-gray-500" />
              </div>
            </div>
          ))}
        </div>
      )}
    </section>
  </main>
</div>
)
}
export default StudentDashboard