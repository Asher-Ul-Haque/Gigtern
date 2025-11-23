"use client"

import type React from "react"
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

interface DashboardProps {
  onNavigate: (page: string) => void
}

const APP_LOGO = "/images/logo.png"

export const StartupDashboard: React.FC<DashboardProps> = ({ onNavigate }) => {
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
          <button className="px-5 py-2.5 bg-white text-black rounded-xl font-bold hover:bg-gray-200 transition-colors flex items-center gap-2">
            <Plus size={18} /> Post a New Gig
          </button>
        </header>

        {/* Talent Search */}
        <div className="bg-zinc-900 border border-white/10 p-6 rounded-2xl mb-10">
          <h2 className="text-xl font-bold mb-4">Find Student Talent</h2>
          <div className="flex gap-4 mb-4">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-500" size={20} />
              <input
                type="text"
                placeholder="Search by skills (e.g. React, Design, Video)..."
                className="w-full bg-black border border-white/10 rounded-xl pl-10 pr-4 py-3 focus:outline-none focus:border-violet-500 transition-colors"
              />
            </div>
            <button className="px-4 bg-zinc-800 border border-white/10 rounded-xl hover:bg-zinc-700 transition-colors">
              <Filter size={20} />
            </button>
          </div>
          <div className="flex gap-2">
            {["Hyperlocal", "Verified ID", "Portfolio Available", "Previous Experience"].map((filter, i) => (
              <span
                key={i}
                className="px-3 py-1 bg-white/5 border border-white/5 rounded-full text-xs text-gray-300 cursor-pointer hover:bg-white/10 transition-colors"
              >
                {filter}
              </span>
            ))}
          </div>
        </div>

        {/* Recommended Candidates */}
        <section className="mb-10">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-xl font-bold">Top Matched Students</h2>
            <button className="text-violet-400 text-sm font-medium hover:text-violet-300 flex items-center gap-1">
              View All <ChevronRight size={16} />
            </button>
          </div>

          <div className="grid md:grid-cols-3 gap-6">
            {[
              {
                name: "Alex M.",
                role: "Graphic Designer",
                skills: ["Figma", "Adobe Suite"],
                match: "98%",
                img: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&q=80",
              },
              {
                name: "Sarah K.",
                role: "Content Writer",
                skills: ["SEO", "Copywriting"],
                match: "95%",
                img: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&q=80",
              },
              {
                name: "Rahul V.",
                role: "Video Editor",
                skills: ["Premiere", "After Effects"],
                match: "92%",
                img: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&q=80",
              },
            ].map((student, i) => (
              <div
                key={i}
                onClick={() => onNavigate("/profile")}
                className="bg-zinc-900 border border-white/5 p-5 rounded-2xl hover:border-violet-500/30 transition-all group cursor-pointer relative overflow-hidden"
              >
                <div className="absolute top-0 right-0 bg-green-500 text-black text-xs font-bold px-3 py-1 rounded-bl-xl">
                  {student.match} Match
                </div>
                <div className="flex items-center gap-4 mb-4">
                  <img
                    src={student.img || "/placeholder.svg"}
                    alt={student.name}
                    className="w-14 h-14 rounded-full object-cover border-2 border-white/10"
                  />
                  <div>
                    <h3 className="font-bold text-lg group-hover:text-violet-400 transition-colors">{student.name}</h3>
                    <p className="text-sm text-gray-400">{student.role}</p>
                  </div>
                </div>
                <div className="flex flex-wrap gap-2 mb-4">
                  {student.skills.map((skill, j) => (
                    <span key={j} className="px-2 py-1 bg-white/5 rounded text-xs text-gray-300">
                      {skill}
                    </span>
                  ))}
                </div>
                <div className="flex items-center gap-2 text-xs text-green-400">
                  <CheckCircle2 size={12} /> ID Verified
                </div>
              </div>
            ))}
          </div>
        </section>

        {/* Active Listings */}
        <section>
          <h2 className="text-xl font-bold mb-6">Your Active Gigs</h2>
          <div className="space-y-4">
            {[
              { title: "Logo Design for Cafe", applicants: 12, views: 340, status: "Active" },
              { title: "Event Photography", applicants: 8, views: 156, status: "Active" },
            ].map((gig, i) => (
              <div
                key={i}
                className="flex items-center justify-between p-4 bg-zinc-900 border border-white/5 rounded-xl"
              >
                <div className="flex items-center gap-4">
                  <div className="p-3 bg-violet-500/10 rounded-lg text-violet-400">
                    <Briefcase size={20} />
                  </div>
                  <div>
                    <h3 className="font-bold">{gig.title}</h3>
                    <p className="text-sm text-gray-400">
                      {gig.applicants} Applicants • {gig.views} Views
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
            ))}
          </div>
        </section>
      </main>
    </div>
  )
}
