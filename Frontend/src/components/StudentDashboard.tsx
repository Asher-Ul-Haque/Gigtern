"use client"

import type React from "react"
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

interface DashboardProps {
  onNavigate: (page: string) => void
}

const APP_LOGO = "/images/logo.png"

export const StudentDashboard: React.FC<DashboardProps> = ({ onNavigate }) => {
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
          <span className="text-2xl font-bold tracking-tight">GigTern</span>
        </div>

        <nav className="px-4 py-6 space-y-1.5 flex-1">
          <div className="px-4 text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">Student Menu</div>
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
          <div className="flex items-center gap-4">
            <button className="w-10 h-10 rounded-full bg-zinc-900 border border-white/10 flex items-center justify-center hover:bg-zinc-800 transition-colors relative">
              <Briefcase size={18} className="text-gray-400" />
              <span className="absolute top-0 right-0 w-3 h-3 bg-red-500 rounded-full border-2 border-black"></span>
            </button>
            <div className="w-10 h-10 rounded-full bg-gradient-to-br from-violet-500 to-indigo-600 p-0.5">
              <img
                src="https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&q=80"
                alt="Profile"
                className="w-full h-full rounded-full object-cover border-2 border-black"
              />
            </div>
          </div>
        </header>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-10">
          {[
            {
              label: "Total Earnings",
              value: "₹12,450",
              change: "+15%",
              icon: DollarSign,
              color: "from-green-500/20 to-emerald-500/20",
              text: "text-green-400",
            },
            {
              label: "Gigs Completed",
              value: "8",
              change: "+2",
              icon: CheckCircle2,
              color: "from-blue-500/20 to-cyan-500/20",
              text: "text-blue-400",
            },
            {
              label: "Profile Views",
              value: "142",
              change: "+24%",
              icon: TrendingUp,
              color: "from-violet-500/20 to-purple-500/20",
              text: "text-violet-400",
            },
          ].map((stat, i) => (
            <div
              key={i}
              className={`dashboard-card p-6 rounded-2xl bg-gradient-to-br ${stat.color} border border-white/5 relative overflow-hidden group hover:scale-[1.02] transition-transform`}
            >
              <div className="relative z-10">
                <div className="flex justify-between items-start mb-4">
                  <div className={`p-3 rounded-xl bg-black/20 ${stat.text}`}>
                    <stat.icon size={24} />
                  </div>
                  <span className="px-2 py-1 rounded-full bg-white/10 text-xs font-medium text-white">
                    {stat.change}
                  </span>
                </div>
                <h3 className="text-gray-400 text-sm font-medium mb-1">{stat.label}</h3>
                <p className="text-3xl font-bold text-white">{stat.value}</p>
              </div>
            </div>
          ))}
        </div>

        {/* Latest Opportunities */}
        <section>
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-xl font-bold">Recommended for You</h2>
            <button
              onClick={() => onNavigate("/find-gigs")}
              className="text-violet-400 text-sm font-medium hover:text-violet-300 flex items-center gap-1"
            >
              View All <ChevronRight size={16} />
            </button>
          </div>

          <div className="grid md:grid-cols-2 gap-4">
            {[
              {
                title: "Social Media Manager",
                company: "GrowthHackerz",
                pay: "₹5,000",
                time: "1 Week",
                img: "https://images.unsplash.com/photo-1611162617474-5b21e879e113?auto=format&fit=crop&q=80",
              },
              {
                title: "Event Photographer",
                company: "WedMeGood",
                pay: "₹2,000/day",
                time: "2 Days",
                img: "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?auto=format&fit=crop&q=80",
              },
            ].map((gig, i) => (
              <div
                key={i}
                className="dashboard-card p-4 rounded-2xl bg-zinc-900 border border-white/5 hover:border-violet-500/30 transition-all group cursor-pointer"
                onClick={() => onNavigate("/gig-details")}
              >
                <div className="flex gap-4">
                  <img src={gig.img || "/placeholder.svg"} alt="" className="w-24 h-24 rounded-xl object-cover" />
                  <div className="flex-1 py-1">
                    <div className="flex justify-between items-start">
                      <div>
                        <h3 className="font-bold text-lg mb-1 group-hover:text-violet-400 transition-colors">
                          {gig.title}
                        </h3>
                        <p className="text-sm text-gray-400 mb-2">{gig.company}</p>
                      </div>
                      <button className="p-2 hover:bg-white/10 rounded-full transition-colors">
                        <Star size={18} className="text-gray-500 hover:text-yellow-400" />
                      </button>
                    </div>
                    <div className="flex gap-3 text-xs font-medium">
                      <span className="px-2 py-1 rounded bg-green-500/10 text-green-400 flex items-center gap-1">
                        <DollarSign size={12} /> {gig.pay}
                      </span>
                      <span className="px-2 py-1 rounded bg-blue-500/10 text-blue-400 flex items-center gap-1">
                        <Clock size={12} /> {gig.time}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </section>
      </main>
    </div>
  )
}

function CheckCircle2(props: React.SVGProps<SVGSVGElement>) {
  return (
    <svg
      {...props}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <circle cx="12" cy="12" r="10" />
      <path d="m9 12 2 2 4-4" />
    </svg>
  )
}
