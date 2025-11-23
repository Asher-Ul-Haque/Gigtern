"use client"

import type React from "react"
import { useState, useEffect } from "react"
import {
LayoutDashboard,
Search,
Briefcase,
Users,
CheckCircle2,
Star,
ChevronRight,
Plus,
Filter,
LogOut,
} from "lucide-react"
import { createGig, getMyGig } from "../api" // adjust path

interface DashboardProps {
onNavigate: (page: string) => void
}

interface GigInput {
title: string
description: string
paymentType: "FIXED" | "HOURLY"
payRate: number
skillsRequired: string[]
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

export const StartupDashboard: React.FC<DashboardProps> = ({ onNavigate }) => {
const [gigs, setGigs] = useState<GigResponse[]>([])
const [loading, setLoading] = useState(false)
const [showNewGigForm, setShowNewGigForm] = useState(false)
const [newGig, setNewGig] = useState<GigInput>({
title: "",
description: "",
paymentType: "FIXED",
payRate: 0,
skillsRequired: [],
})

/* ---------------- FETCH MY GIGS ---------------- */
useEffect(() => {
async function fetchGigs() {
setLoading(true)
try {
const myGigs = await getMyGig()
// Ensure myGigs is treated as an array, even if it's a single object
setGigs(Array.isArray(myGigs) ? myGigs : (myGigs ? [myGigs] : []))
} catch (err) {
console.error("Failed to fetch gigs", err)
} finally {
setLoading(false)
}
}
fetchGigs()
}, [])

/* ---------------- CREATE NEW GIG ---------------- */
const handleCreateGig = async () => {
if (!newGig.title || !newGig.description || !newGig.payRate) {
alert("Please fill all required fields")
return
}
try {
const created = await createGig(newGig)
setGigs((prev) => [created, ...prev])
setShowNewGigForm(false)
setNewGig({
title: "",
description: "",
paymentType: "FIXED",
payRate: 0,
skillsRequired: [],
})
} catch (err) {
alert("Error creating gig")
}
}

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
          GigTern <span className="text-xs bg-violet-500 px-2 py-0.5 rounded-full ml-1">Biz</span>
        </span>
      </div>

      <nav className="px-4 py-6 space-y-1.5 flex-1">
        <div className="px-4 text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">Company Menu</div>
        {[
          { icon: LayoutDashboard, label: "Overview", active: true, page: "/startup-dashboard" },
          { icon: Users, label: "Find Talent", page: null },
          { icon: Briefcase, label: "My Listings", page: null },
          { icon: Star, label: "Reviews", page: null },
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
          <h1 className="text-3xl font-bold mb-1">Tech Innovators Inc.</h1>
          <p className="text-gray-400">Manage your gigs and find top student talent.</p>
        </div>
        <button
          onClick={() => setShowNewGigForm((prev) => !prev)}
          className="px-5 py-2.5 bg-white text-black rounded-xl font-bold hover:bg-gray-200 transition-colors flex items-center gap-2"
        >
          <Plus size={18} /> Post a New Gig
        </button>
      </header>

      {/* New Gig Form */}
      {showNewGigForm && (
        <div className="bg-zinc-900 border border-white/10 p-6 rounded-2xl mb-10">
          <h2 className="text-xl font-bold mb-4">New Gig</h2>
          <div className="space-y-4">
            <input
              type="text"
              placeholder="Title"
              className="w-full bg-black border border-white/10 rounded-xl px-4 py-2"
              value={newGig.title}
              onChange={(e) => setNewGig({ ...newGig, title: e.target.value })}
            />
            <textarea
              placeholder="Description"
              className="w-full bg-black border border-white/10 rounded-xl px-4 py-2"
              value={newGig.description}
              onChange={(e) => setNewGig({ ...newGig, description: e.target.value })}
            />
            <div className="flex gap-4">
                <input
                type="number"
                placeholder="Pay Rate"
                className="w-full bg-black border border-white/10 rounded-xl px-4 py-2"
                value={newGig.payRate === 0 ? "" : newGig.payRate} // Show empty string for 0 in UI
                onChange={(e) => setNewGig({ ...newGig, payRate: Number(e.target.value) })}
                />
                <select
                value={newGig.paymentType}
                onChange={(e) =>
                    setNewGig({ ...newGig, paymentType: e.target.value as "FIXED" | "HOURLY" })
                }
                className="w-full bg-black border border-white/10 rounded-xl px-4 py-2"
                >
                <option value="FIXED">Fixed</option>
                <option value="HOURLY">Hourly</option>
                </select>
            </div>
            
            {/* CORRECTED: Skills Input Field */}
            <input
              type="text"
              placeholder="Skills (comma separated: React, Node, SQL)"
              className="w-full bg-black border border-white/10 rounded-xl px-4 py-2"
              value={newGig.skillsRequired.join(", ")}
              onChange={(e) =>
                setNewGig({ ...newGig, skillsRequired: e.target.value.split(",").map((s) => s.trim()).filter(s => s.length > 0) })
              }
            />

            <button
              onClick={handleCreateGig}
              className="px-4 py-2 bg-violet-600 rounded-xl hover:bg-violet-500"
            >
              Create Gig
            </button>
          </div>
        </div>
      )}

      {/* Active Listings */}
      <section>
        <h2 className="text-xl font-bold mb-6">Your Active Gigs</h2>
        <div className="space-y-4">
          {loading ? (
            <p>Loading gigs...</p>
          ) : gigs.length === 0 ? (
            <p>No active gigs yet.</p>
          ) : (
            gigs.map((gig) => (
              <div key={gig.id} className="flex items-center justify-between p-4 bg-zinc-900 border border-white/5 rounded-xl">
                <div className="flex items-center gap-4">
                  <div className="p-3 bg-violet-500/10 rounded-lg text-violet-400">
                    <Briefcase size={20} />
                  </div>
                  <div>
                    <h3 className="font-bold">{gig.title}</h3>
                    <p className="text-sm text-gray-400">
                      {gig.skillsRequired.join(", ")} • {gig.payRate}{" "}
                      {gig.paymentType === "FIXED" ? "₹" : "₹/hr"}
                    </p>
                  </div>
                </div>
                <div className="flex items-center gap-4">
                  <span className="px-3 py-1 bg-green-500/10 text-green-400 text-xs font-bold rounded-full flex items-center gap-1">
                    <span className="w-1.5 h-1.5 bg-green-500 rounded-full animate-pulse"></span> {gig.status}
                  </span>
                  <button className="p-2 hover:bg-white/10 rounded-lg transition-colors">
                    <ChevronRight size={20} className="text-gray-500" />
                  </button>
                </div>
              </div>
            ))
          )}
        </div>
      </section>
    </main>
  </div>
)
}
export default StartupDashboard