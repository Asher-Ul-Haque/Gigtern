import type React from "react"
import { useState } from "react"
import {
  Briefcase,
  CheckCircle2,
  LogOut,
  ArrowRight,
  User,
  DollarSign,
  LayoutDashboard,
  Search,
  MapPin,
  Clock,
  ChevronRight,
  Zap,
} from "lucide-react"
import { FloatingMoneyScene } from "./components/FloatingMoney"
import { LoginModal } from "./components/LoginModal"
import { BackedBySection } from "./Carousel"
import { StudentDashboard } from "./components/StudentDashboard"
import { StartupDashboard } from "./components/StartupDashboard"
import { StudentProfile } from "./components/StudentProfile"
import BlobCursor from "./components/BlobCursor"
import Aurora from "./components/Aurora"

// DashboardProps interface definition
interface DashboardProps {
  onNavigate: (page: string) => void
}

// Logo URL
const APP_LOGO = "/images/logo.png"

// ... existing useIntersectionObserver ...

const GigDetails: React.FC<DashboardProps> = ({ onNavigate }) => {
  return (
    <div className="bg-black text-white min-h-screen">
      {/* Sticky Apply Button for Mobile */}
      <div className="fixed bottom-0 left-0 right-0 p-4 bg-zinc-950 border-t border-white/10 z-50 md:hidden">
        <button className="w-full py-4 bg-gradient-to-r from-violet-600 to-indigo-600 rounded-xl font-bold text-lg shadow-lg shadow-violet-900/20 flex items-center justify-center gap-2 animate-pulse">
          Swipe to Apply <ArrowRight size={20} />
        </button>
      </div>

      <div className="max-w-4xl mx-auto pb-24 md:pb-10 relative">
        <button
          onClick={() => onNavigate("/find-gigs")}
          className="absolute top-6 left-6 z-20 p-2 bg-black/50 backdrop-blur-md rounded-full border border-white/10 hover:bg-white/10 transition-colors"
        >
          <ArrowRight className="rotate-180 text-white" size={24} />
        </button>

        {/* Header Image */}
        <div className="relative h-80 w-full overflow-hidden">
          <div className="absolute inset-0 bg-[url('https://images.unsplash.com/photo-1497215728101-856f4ea42174?auto=format&fit=crop&q=80')] bg-cover bg-center"></div>
          <div className="absolute inset-0 bg-gradient-to-b from-transparent via-black/60 to-black"></div>
        </div>

        <div className="px-6 md:px-10 -mt-20 relative z-10">
          <div className="mb-8">
            <span className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-violet-500/10 border border-violet-500/20 text-violet-300 text-xs font-bold mb-4">
              <Zap size={12} className="fill-current" /> Fast Hire
            </span>
            <h1 className="text-4xl md:text-5xl font-bold mb-2 leading-tight">Graphic Designer for Coffee Shop</h1>
            <div className="flex items-center gap-2 text-gray-400 text-lg">
              <span className="font-medium text-white">Startups Inc.</span>
              <span className="w-1 h-1 bg-gray-500 rounded-full"></span>
              <span>0.5km away</span>
            </div>
          </div>

          {/* Key Info Grid */}
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-10">
            {[
              { icon: DollarSign, label: "Pay", value: "₹500/day", color: "text-green-400" },
              { icon: Clock, label: "Duration", value: "3 Days", color: "text-blue-400" },
              { icon: MapPin, label: "Location", value: "On-site", color: "text-red-400" },
              { icon: Zap, label: "Start", value: "Immediate", color: "text-yellow-400" },
            ].map((item, i) => (
              <div key={i} className="bg-zinc-900/50 border border-white/5 p-4 rounded-2xl">
                <item.icon className={`w-6 h-6 ${item.color} mb-2`} />
                <p className="text-xs text-gray-500 uppercase tracking-wider mb-1">{item.label}</p>
                <p className="font-bold text-lg">{item.value}</p>
              </div>
            ))}
          </div>

          <div className="grid md:grid-cols-3 gap-10">
            <div className="md:col-span-2 space-y-8">
              <section>
                <h2 className="text-xl font-bold mb-4">About the Gig</h2>
                <p className="text-gray-300 leading-relaxed text-lg">
                  We are looking for a creative Graphic Designer to help us rebrand our coffee shop menu and social
                  media assets. You will work directly with the owner to create engaging visuals that attract more
                  customers. This is a short-term project with potential for future work.
                </p>
              </section>

              <section>
                <h2 className="text-xl font-bold mb-4">Responsibilities</h2>
                <ul className="space-y-3 text-gray-300">
                  {[
                    "Design a new menu layout",
                    "Create 5 social media posts",
                    "Edit photos of our coffee products",
                    "Deliver source files upon completion",
                  ].map((item, i) => (
                    <li key={i} className="flex items-start gap-3">
                      <CheckCircle2 className="w-5 h-5 text-violet-500 shrink-0 mt-0.5" />
                      {item}
                    </li>
                  ))}
                </ul>
              </section>
            </div>

            <div className="space-y-6">
              <div className="bg-zinc-900 border border-white/10 p-6 rounded-2xl">
                <h3 className="font-bold mb-4 text-lg">Gig Location</h3>
                <div className="h-40 bg-zinc-800 rounded-xl mb-4 flex items-center justify-center border border-white/5">
                  <MapPin className="w-8 h-8 text-gray-500" />
                </div>
                <p className="text-gray-400 text-sm mb-1">Connaught Place, New Delhi</p>
                <p className="text-violet-400 text-xs font-bold cursor-pointer">Open in Maps</p>
              </div>

              <div className="hidden md:block p-6 bg-gradient-to-b from-violet-900/20 to-zinc-900 border border-violet-500/20 rounded-2xl text-center">
                <h3 className="font-bold text-xl mb-2">Ready to Apply?</h3>
                <p className="text-gray-400 text-sm mb-6">No resume required. Instant Apply.</p>
                <button className="w-full py-4 bg-white text-black rounded-xl font-bold text-lg hover:bg-gray-100 transition-all transform hover:scale-105 shadow-lg">
                  Apply Now
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

const GigDiscovery: React.FC<DashboardProps> = ({ onNavigate }) => {
  const categories = ["All", "Tech", "Design", "Events", "Marketing", "Admin", "Education"]

  return (
    <div className="bg-black text-white min-h-screen flex">
      {/* Sidebar */}
      <aside className="w-72 bg-zinc-950 border-r border-white/5 hidden lg:flex flex-col fixed h-full z-10 shadow-2xl">
        {/* Reuse Sidebar Content */}
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
            { icon: Search, label: "Find Gigs", active: true, page: "/find-gigs" },
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

      <main className="flex-1 lg:ml-72 bg-black min-h-screen">
        <header className="sticky top-0 z-20 bg-black/80 backdrop-blur-xl border-b border-white/5 px-6 py-4 flex items-center justify-between">
          <div className="flex-1 max-w-xl relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-500" size={18} />
            <input
              type="text"
              placeholder="Find gigs nearby..."
              className="w-full bg-zinc-900 border border-white/10 rounded-xl pl-10 pr-4 py-2.5 focus:outline-none focus:border-violet-500/50 transition-all"
            />
          </div>
          <button className="p-2.5 bg-zinc-900 border border-white/10 rounded-xl ml-4 hover:bg-zinc-800 transition-colors">
            <MapPin size={20} className="text-white" />
          </button>
        </header>

        <div className="p-6 max-w-7xl mx-auto">
          {/* Filters */}
          <div className="flex gap-2 mb-8 overflow-x-auto pb-2 custom-scrollbar">
            {["Hyperlocal (<5km)", "1-3 Weeks", "Weekend Only", "High Pay"].map((filter, i) => (
              <button
                key={i}
                className="px-4 py-2 bg-zinc-900 border border-white/10 rounded-full text-sm whitespace-nowrap hover:border-violet-500/50 transition-colors"
              >
                {filter}
              </button>
            ))}
          </div>

          {/* Categories */}
          <div className="flex gap-4 mb-8 overflow-x-auto pb-2 custom-scrollbar">
            {categories.map((cat, i) => (
              <button
                key={i}
                className={`px-6 py-3 rounded-xl font-medium transition-all whitespace-nowrap ${i === 0 ? "bg-white text-black" : "bg-zinc-900 text-gray-400 hover:text-white"}`}
              >
                {cat}
              </button>
            ))}
          </div>

          {/* Gig List */}
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {[
              {
                title: "Event Photographer",
                company: "Flash Events",
                pay: "₹2,000",
                dist: "1.2km",
                time: "2 Days",
                img: "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?auto=format&fit=crop&q=80",
              },
              {
                title: "Social Media Content",
                company: "Cafe Bliss",
                pay: "₹5,000",
                dist: "0.8km",
                time: "1 Week",
                img: "https://images.unsplash.com/photo-1611162617474-5b21e879e113?auto=format&fit=crop&q=80",
              },
              {
                title: "Store Assistant",
                company: "Retail Hub",
                pay: "₹800/day",
                dist: "2.5km",
                time: "Weekend",
                img: "https://images.unsplash.com/photo-1486312338219-ce68d2c6f44d?auto=format&fit=crop&q=80",
              },
              {
                title: "Graphic Designer",
                company: "Startups Inc",
                pay: "₹500/day",
                dist: "0.5km",
                time: "3 Days",
                img: "https://images.unsplash.com/photo-1626785774573-4b79931256a6?auto=format&fit=crop&q=80",
              },
              {
                title: "Data Entry",
                company: "Tech Corp",
                pay: "₹1,500",
                dist: "Remote",
                time: "2 Days",
                img: "https://images.unsplash.com/photo-1486312338219-ce68d2c6f44d?auto=format&fit=crop&q=80",
              },
            ].map((gig, i) => (
              <div
                key={i}
                className="group bg-zinc-900 border border-white/5 rounded-2xl overflow-hidden hover:border-violet-500/30 transition-all hover:translate-y-[-4px]"
              >
                <div className="h-32 bg-cover bg-center relative" style={{ backgroundImage: `url(${gig.img})` }}>
                  <div className="absolute inset-0 bg-gradient-to-t from-black/80 to-transparent"></div>
                  <div className="absolute bottom-3 left-3">
                    <h3 className="font-bold text-lg text-white group-hover:text-violet-300 transition-colors">
                      {gig.title}
                    </h3>
                    <p className="text-xs text-gray-300">{gig.company}</p>
                  </div>
                </div>
                <div className="p-4">
                  <div className="flex flex-wrap gap-2 mb-4">
                    <span className="px-2 py-1 bg-violet-500/10 text-violet-300 text-xs rounded font-bold">
                      {gig.pay}
                    </span>
                    <span className="px-2 py-1 bg-white/5 text-gray-400 text-xs rounded flex items-center gap-1">
                      <MapPin size={10} /> {gig.dist}
                    </span>
                    <span className="px-2 py-1 bg-white/5 text-gray-400 text-xs rounded flex items-center gap-1">
                      <Clock size={10} /> {gig.time}
                    </span>
                  </div>
                  <button
                    onClick={() => onNavigate("/gig-details")}
                    className="w-full py-2.5 bg-white text-black rounded-xl font-bold text-sm hover:bg-gray-200 transition-colors flex items-center justify-center gap-1 group-hover:shadow-lg group-hover:shadow-white/10"
                  >
                    View Details <ChevronRight size={16} />
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </main>
    </div>
  )
}

const LandingPage: React.FC<DashboardProps> = ({ onNavigate }) => {
  const [isLoginOpen, setIsLoginOpen] = useState(false)

  const handleLogin = (role: "student" | "organization") => {
    setIsLoginOpen(false)
    if (role === "student") {
      onNavigate("/student-dashboard")
    } else {
      onNavigate("/startup-dashboard")
    }
  }

  return (
    <div className="bg-black min-h-screen text-white selection:bg-violet-500/30 relative overflow-hidden">
      <BlobCursor
        blobType="circle"
        fillColor="#5227FF"
        trailCount={3}
        sizes={[60, 125, 75]}
        innerSizes={[20, 35, 25]}
        innerColor="rgba(255,255,255,0.8)"
        opacities={[0.6, 0.6, 0.6]}
        shadowColor="rgba(0,0,0,0.75)"
        shadowBlur={5}
        shadowOffsetX={10}
        shadowOffsetY={10}
        filterStdDeviation={30}
        useFilter={true}
        fastDuration={0.1}
        slowDuration={0.5}
        zIndex={100}
      />

      <div className="absolute inset-0 z-0 opacity-50 pointer-events-none">
        <Aurora colorStops={["#3A29FF", "#FF94B4", "#FF3232"]} blend={0.5} amplitude={1.0} speed={0.5} />
      </div>

      <LoginModal isOpen={isLoginOpen} onClose={() => setIsLoginOpen(false)} onLogin={handleLogin} />

      <nav className="fixed top-0 w-full z-50 bg-black/50 backdrop-blur-lg border-b border-white/5">
        <div className="max-w-7xl mx-auto px-6 h-20 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <img src={APP_LOGO || "/placeholder.svg"} alt="GigTern Logo" className="h-10 w-10 rounded-xl" />
            <span className="text-xl font-bold tracking-tight bg-clip-text text-transparent bg-gradient-to-r from-white to-gray-400">
              GigTern
            </span>
          </div>
          <div className="hidden md:flex items-center gap-8">
            <a href="#features" className="text-sm font-medium text-gray-400 hover:text-white transition-colors">
              Features
            </a>
            <a href="#how-it-works" className="text-sm font-medium text-gray-400 hover:text-white transition-colors">
              How it Works
            </a>
            <button
              onClick={() => setIsLoginOpen(true)}
              className="px-5 py-2.5 bg-white text-black rounded-full text-sm font-bold hover:bg-gray-200 transition-all transform hover:scale-105"
            >
              Log In
            </button>
          </div>
        </div>
      </nav>

      <main className="pt-32 pb-20 px-6 relative z-10">
        <div className="max-w-7xl mx-auto grid lg:grid-cols-2 gap-12 items-center">
          <div className="space-y-8">
            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-violet-500/10 border border-violet-500/20 text-violet-300 text-xs font-bold">
              <Zap size={12} className="fill-current" /> #1 Gig Platform for Students
            </div>
            <h1 className="text-5xl md:text-7xl font-bold leading-tight tracking-tight">
              Earn while you{" "}
              <span className="text-transparent bg-clip-text bg-gradient-to-r from-violet-400 to-indigo-400">
                Learn
              </span>
            </h1>
            <p className="text-lg text-gray-400 max-w-xl leading-relaxed">
              GigTern connects ambitious students with startups for micro-internships and gigs. Build your portfolio,
              earn money, and gain real-world experience.
            </p>
            <div className="flex flex-col sm:flex-row gap-4">
              <button
                onClick={() => setIsLoginOpen(true)}
                className="px-8 py-4 bg-white text-black rounded-full font-bold text-lg hover:bg-gray-200 transition-all transform hover:scale-105 flex items-center justify-center gap-2"
              >
                Get Started <ArrowRight size={20} />
              </button>
              <button className="px-8 py-4 bg-white/5 text-white border border-white/10 rounded-full font-bold text-lg hover:bg-white/10 transition-all flex items-center justify-center gap-2">
                View Gigs
              </button>
            </div>

            <div className="pt-8 flex items-center gap-4 text-sm text-gray-500">
              <div className="flex -space-x-2">
                {[1, 2, 3, 4].map((i) => (
                  <div
                    key={i}
                    className="w-8 h-8 rounded-full bg-zinc-800 border-2 border-black flex items-center justify-center text-xs font-medium text-white"
                  >
                    {i === 4 ? "+" : ""}
                  </div>
                ))}
              </div>
              <p>Join 10,000+ students already earning</p>
            </div>
          </div>

          <div className="relative h-[500px] w-full hidden lg:block">
            <FloatingMoneyScene />
          </div>
        </div>
      </main>

      <BackedBySection />
    </div>
  )
}

function App() {
  const [currentPage, setCurrentPage] = useState("/")

  const navigate = (page: string) => {
    window.scrollTo(0, 0)
    setCurrentPage(page)
  }

  return (
    <>
      {currentPage === "/" && <LandingPage onNavigate={navigate} />}
      {currentPage === "/student-dashboard" && <StudentDashboard onNavigate={navigate} />}
      {currentPage === "/startup-dashboard" && <StartupDashboard onNavigate={navigate} />}
      {currentPage === "/profile" && <StudentProfile onNavigate={navigate} />}
      {currentPage === "/find-gigs" && <GigDiscovery onNavigate={navigate} />}
      {currentPage === "/gig-details" && <GigDetails onNavigate={navigate} />}
      {/* Legacy route support */}
      {currentPage === "/home" && <StudentDashboard onNavigate={navigate} />}
      {currentPage === "/my-gigs" && <StudentDashboard onNavigate={navigate} />}
      {currentPage === "/schedule" && <StudentDashboard onNavigate={navigate} />}
    </>
  )
}

export default App
