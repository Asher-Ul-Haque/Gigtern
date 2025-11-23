import type React from "react"
import { useRef, useEffect, useState } from "react"
import { X, User, Lock, ArrowRight, Building2 } from "lucide-react"
import gsap from "gsap"
import { registerUser } from "../api" // adjust path if needed

interface LoginModalProps {
  isOpen: boolean
  onClose: () => void
  onLogin: (type: "student" | "organization") => void
}

export const LoginModal: React.FC<LoginModalProps> = ({ isOpen, onClose, onLogin }) => {
  const [userType, setUserType] = useState<"student" | "organization">("student")
  const [mode, setMode] = useState<"login" | "signup">("login")

  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [name, setName] = useState("")
  const [loading, setLoading] = useState(false)

  const modalRef = useRef<HTMLDivElement>(null)
  const overlayRef = useRef<HTMLDivElement>(null)
  const contentRef = useRef<HTMLDivElement>(null)

  /* ---------------- ANIMATIONS ---------------- */
  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = "hidden"

      const tl = gsap.timeline()

      tl.to(overlayRef.current, {
        opacity: 1,
        duration: 0.3,
        ease: "power2.out",
      }).fromTo(
        contentRef.current,
        { y: 50, opacity: 0, scale: 0.95 },
        { y: 0, opacity: 1, scale: 1, duration: 0.4, ease: "back.out(1.7)" },
        "-=0.2",
      )
    } else {
      document.body.style.overflow = "unset"
    }
  }, [isOpen])

  const handleClose = () => {
    const tl = gsap.timeline({
      onComplete: onClose,
    })

    tl.to(contentRef.current, {
      y: 20,
      opacity: 0,
      scale: 0.95,
      duration: 0.2,
      ease: "power2.in",
    }).to(
      overlayRef.current,
      {
        opacity: 0,
        duration: 0.2,
        ease: "power2.in",
      },
      "-=0.1",
    )
  }

  /* ---------------- SUBMIT HANDLER ---------------- */
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)

    try {
      if (mode === "signup") {
        // REGISTER
        const res = await registerUser({
          email,
          password,
          name,
          role: userType === "student" ? "STUDENT" : "EMPLOYER",
        })

        console.log("Signup success:", res)
      } else {
        // LOGIN (implement once you show me login endpoint)
        console.log("LOGIN not implemented yet")
      }

      handleClose()
      setTimeout(() => onLogin(userType), 300)
    } catch (err: any) {
      alert(err.message || "Something went wrong")
    } finally {
      setLoading(false)
    }
  }

  if (!isOpen) return null

  /* ---------------- RENDER ---------------- */

  return (
    <div ref={modalRef} className="fixed inset-0 z-[100] flex items-center justify-center">
      {/* Backdrop */}
      <div ref={overlayRef} className="absolute inset-0 bg-black/60 backdrop-blur-sm opacity-0" onClick={handleClose} />

      {/* Modal Content */}
      <div
        ref={contentRef}
        className="relative w-full max-w-md mx-4 bg-[#0a0a0a] border border-white/10 rounded-2xl shadow-2xl p-8 opacity-0"
      >
        <button
          onClick={handleClose}
          className="absolute top-4 right-4 text-gray-400 hover:text-white transition-colors"
        >
          <X size={20} />
        </button>

        <div className="text-center mb-6">
          <h2 className="text-3xl font-bold text-white mb-2">
            {mode === "login" ? "Welcome Back" : "Create Account"}
          </h2>
          <p className="text-gray-400">
            {mode === "login"
              ? `Sign in to access your ${userType === "student" ? "opportunities" : "dashboard"}`
              : `Join as a ${userType === "student" ? "student" : "organization"}`}
          </p>
        </div>

        {/* User Type Toggle */}
        <div className="flex p-1 bg-white/5 rounded-xl mb-8 border border-white/10">
          <button
            onClick={() => setUserType("student")}
            className={`flex-1 flex items-center justify-center gap-2 py-2.5 text-sm font-medium rounded-lg transition-all duration-200 ${
              userType === "student"
                ? "bg-violet-600 text-white shadow-lg shadow-violet-900/20"
                : "text-gray-400 hover:text-white hover:bg-white/5"
            }`}
          >
            <User size={16} />
            Student
          </button>
          <button
            onClick={() => setUserType("organization")}
            className={`flex-1 flex items-center justify-center gap-2 py-2.5 text-sm font-medium rounded-lg transition-all duration-200 ${
              userType === "organization"
                ? "bg-violet-600 text-white shadow-lg shadow-violet-900/20"
                : "text-gray-400 hover:text-white hover:bg-white/5"
            }`}
          >
            <Building2 size={16} />
            Organization
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* NAME INPUT (Signup only) */}
          {mode === "signup" && (
            <div className="space-y-2">
              <label className="text-sm font-medium text-gray-300 ml-1">Full Name</label>
              <input
                type="text"
                required
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="block w-full px-3 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-violet-500/50 focus:border-violet-500 transition-all"
                placeholder="John Doe"
              />
            </div>
          )}

          {/* EMAIL */}
          <div className="space-y-2">
            <label className="text-sm font-medium text-gray-300 ml-1">
              {userType === "student" ? "Student Email" : "Work Email"}
            </label>
            <div className="relative group">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                {userType === "student" ? (
                  <User className="h-5 w-5 text-gray-500 group-focus-within:text-violet-500 transition-colors" />
                ) : (
                  <Building2 className="h-5 w-5 text-gray-500 group-focus-within:text-violet-500 transition-colors" />
                )}
              </div>
              <input
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="block w-full pl-10 pr-3 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-violet-500/50 focus:border-violet-500 transition-all"
                placeholder={userType === "student" ? "student@university.edu" : "hr@company.com"}
              />
            </div>
          </div>

          {/* PASSWORD */}
          <div className="space-y-2">
            <div className="flex justify-between items-center ml-1">
              <label className="text-sm font-medium text-gray-300">Password</label>
              {mode === "login" && (
                <a href="#" className="text-xs text-violet-400 hover:text-violet-300">
                  Forgot password?
                </a>
              )}
            </div>
            <div className="relative group">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <Lock className="h-5 w-5 text-gray-500 group-focus-within:text-violet-500 transition-colors" />
              </div>
              <input
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="block w-full pl-10 pr-3 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-violet-500/50 focus:border-violet-500 transition-all"
                placeholder="••••••••"
              />
            </div>
          </div>

          {/* SUBMIT BUTTON */}
          <button
            type="submit"
            disabled={loading}
            className="w-full bg-gradient-to-r from-violet-600 to-indigo-600 hover:from-violet-500 hover:to-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed text-white font-semibold py-3 px-4 rounded-xl transition-all transform hover:scale-[1.02] active:scale-[0.98] shadow-lg shadow-violet-500/20 flex items-center justify-center gap-2"
          >
            {loading
              ? mode === "signup"
                ? "Creating Account..."
                : "Signing In..."
              : mode === "signup"
                ? `Create ${userType === "student" ? "Student" : "Organization"} Account`
                : `Sign In as ${userType === "student" ? "Student" : "Organization"}`}
            {!loading && <ArrowRight size={18} />}
          </button>
        </form>

        {/* FOOTER SWITCH */}
        <div className="mt-8 text-center text-sm text-gray-400">
          {mode === "login" ? (
            <>
              Don't have an account?{" "}
              <button onClick={() => setMode("signup")} className="text-violet-400 hover:text-violet-300 font-medium">
                Create {userType === "student" ? "student" : "organization"} account
              </button>
            </>
          ) : (
            <>
              Already have an account?{" "}
              <button onClick={() => setMode("login")} className="text-violet-400 hover:text-violet-300 font-medium">
                Login instead
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  )
}