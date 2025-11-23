"use client"

import type React from "react"
import {
  LayoutDashboard,
  Search,
  Briefcase,
  User,
  LogOut,
  MapPin,
  LinkIcon,
  Github,
  Linkedin,
  Mail,
  Star,
  Award,
  CheckCircle2,
} from "lucide-react"

interface DashboardProps {
  onNavigate: (page: string) => void
}

const APP_LOGO = "/images/logo.png"

export const StudentProfile: React.FC<DashboardProps> = ({ onNavigate }) => {
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
            { icon: LayoutDashboard, label: "Dashboard", page: "/student-dashboard" },
            { icon: Search, label: "Find Gigs", page: "/find-gigs" },
            { icon: Briefcase, label: "My Gigs", page: "/my-gigs" },
            { icon: User, label: "Profile", active: true, page: "/profile" },
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
      <main className="flex-1 lg:ml-72 bg-black min-h-screen p-6 lg:p-10 relative overflow-hidden">
        {/* Profile Background */}
        <div className="absolute top-0 left-0 right-0 h-64 bg-gradient-to-b from-violet-900/30 to-black z-0"></div>

        <div className="relative z-10 max-w-4xl mx-auto pt-20">
          {/* Profile Header Card */}
          <div className="bg-zinc-900 border border-white/10 rounded-2xl p-8 mb-8 relative shadow-2xl">
            <div className="absolute -top-16 left-8 p-1 bg-black rounded-full">
              <img
                src="https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&q=80"
                alt="Profile"
                className="w-32 h-32 rounded-full object-cover border-4 border-zinc-900"
              />
              <div className="absolute bottom-2 right-2 w-6 h-6 bg-green-500 border-4 border-black rounded-full"></div>
            </div>

            <div className="flex justify-end mb-4 gap-3">
              <button className="px-4 py-2 bg-white/5 border border-white/10 rounded-xl font-medium hover:bg-white/10 transition-colors flex items-center gap-2">
                <LinkIcon size={16} /> Copy Portfolio
              </button>
              <button className="px-4 py-2 bg-violet-600 text-white rounded-xl font-bold hover:bg-violet-700 transition-colors shadow-lg shadow-violet-900/20">
                Edit Profile
              </button>
            </div>

            <div className="mt-4">
              <div className="flex items-center gap-3 mb-1">
                <h1 className="text-3xl font-bold">Alex Mitchell</h1>
                <CheckCircle2 className="text-blue-400" size={20} />
              </div>
              <p className="text-gray-400 text-lg mb-4">Graphic Designer & Creative Director</p>

              <div className="flex flex-wrap gap-6 text-sm text-gray-500 mb-6">
                <div className="flex items-center gap-2">
                  <MapPin size={16} /> New Delhi, India
                </div>
                <div className="flex items-center gap-2">
                  <Mail size={16} /> alex.m@design.com
                </div>
                <div className="flex items-center gap-2 text-violet-400 font-medium">
                  <Star size={16} className="fill-current" /> 4.9 (24 Reviews)
                </div>
              </div>

              <div className="flex gap-4">
                <a href="#" className="p-2 bg-white/5 rounded-full hover:bg-white/10 transition-colors">
                  <Github size={20} />
                </a>
                <a href="#" className="p-2 bg-white/5 rounded-full hover:bg-white/10 transition-colors">
                  <Linkedin size={20} />
                </a>
              </div>
            </div>
          </div>

          {/* Two Column Layout */}
          <div className="grid md:grid-cols-3 gap-8">
            {/* Left Col */}
            <div className="md:col-span-2 space-y-8">
              {/* About */}
              <section>
                <h2 className="text-xl font-bold mb-4 flex items-center gap-2">
                  <User size={20} className="text-violet-400" /> About Me
                </h2>
                <p className="text-gray-300 leading-relaxed">
                  I am a passionate graphic designer with 3+ years of experience creating visual content for brands. I
                  specialize in logo design, social media graphics, and UI/UX. I love turning complex ideas into simple,
                  beautiful visual stories. Open to freelance gigs and micro-internships.
                </p>
              </section>

              {/* Work History */}
              <section>
                <h2 className="text-xl font-bold mb-4 flex items-center gap-2">
                  <Briefcase size={20} className="text-violet-400" /> Recent Gigs
                </h2>
                <div className="space-y-4">
                  {[
                    { title: "Logo Redesign", company: "Coffee House", date: "Oct 2023", rating: 5.0 },
                    { title: "Social Media Kit", company: "TechStart", date: "Sep 2023", rating: 4.8 },
                  ].map((gig, i) => (
                    <div
                      key={i}
                      className="p-4 bg-zinc-900 border border-white/5 rounded-xl flex justify-between items-center"
                    >
                      <div>
                        <h3 className="font-bold">{gig.title}</h3>
                        <p className="text-sm text-gray-400">
                          {gig.company} • {gig.date}
                        </p>
                      </div>
                      <div className="flex items-center gap-1 text-yellow-400 font-bold text-sm">
                        <Star size={14} className="fill-current" /> {gig.rating}
                      </div>
                    </div>
                  ))}
                </div>
              </section>
            </div>

            {/* Right Col */}
            <div className="space-y-8">
              {/* Skills */}
              <section>
                <h2 className="text-xl font-bold mb-4 flex items-center gap-2">
                  <Award size={20} className="text-violet-400" /> Skills
                </h2>
                <div className="flex flex-wrap gap-2">
                  {["Photoshop", "Illustrator", "Figma", "Branding", "UI Design", "Social Media"].map((skill, i) => (
                    <span
                      key={i}
                      className="px-3 py-1.5 bg-white/5 border border-white/5 rounded-lg text-sm text-gray-300"
                    >
                      {skill}
                    </span>
                  ))}
                </div>
              </section>

              {/* Certifications */}
              <section>
                <h2 className="text-xl font-bold mb-4 flex items-center gap-2">
                  <CheckCircle2 size={20} className="text-violet-400" /> Verified
                </h2>
                <div className="p-4 bg-green-500/10 border border-green-500/20 rounded-xl">
                  <div className="flex items-center gap-3 mb-2">
                    <div className="p-2 bg-green-500 rounded-full text-black">
                      <CheckCircle2 size={16} />
                    </div>
                    <h3 className="font-bold text-green-400">Identity Verified</h3>
                  </div>
                  <p className="text-xs text-gray-400">Government ID checked & verified on 12 Aug 2023</p>
                </div>
              </section>
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}
